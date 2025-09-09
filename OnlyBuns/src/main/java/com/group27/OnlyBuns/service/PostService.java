package com.group27.OnlyBuns.service;

import com.group27.OnlyBuns.model.Comment;
import com.group27.OnlyBuns.model.Like;
import com.group27.OnlyBuns.model.Post;
import com.group27.OnlyBuns.model.User;
import com.group27.OnlyBuns.repository.CommentRepository;
import com.group27.OnlyBuns.repository.LikeRepository;
import com.group27.OnlyBuns.repository.PostRepository;
import com.group27.OnlyBuns.utils.RateLimiter;
import dto.LocationDto;
import com.group27.OnlyBuns.repository.UserFollowerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.Map;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private UserFollowerRepository userFollowerRepository;

    @Autowired
    private RateLimiter rateLimiter;

    public Map<String, Long> getPostCounts() {
        LocalDateTime now = LocalDateTime.now();
        Map<String, Long> counts = new HashMap<>();
        counts.put("weekly",  postRepository.countByCreatedAtAfter(now.minusWeeks(1)));
        counts.put("monthly", postRepository.countByCreatedAtAfter(now.minusMonths(1)));
        counts.put("yearly",  postRepository.countByCreatedAtAfter(now.minusYears(1)));
        return counts;
    }

    public Map<String, Long> getCommentCounts() {
        LocalDateTime now = LocalDateTime.now();
        Map<String, Long> counts = new HashMap<>();
        counts.put("weekly",  commentRepository.countByCreatedAtAfter(now.minusWeeks(1)));
        counts.put("monthly", commentRepository.countByCreatedAtAfter(now.minusMonths(1)));
        counts.put("yearly",  commentRepository.countByCreatedAtAfter(now.minusYears(1)));
        return counts;
    }

    // Kreiranje nove objave
    public Post createPost(Post post) {
        post.setCreatedAt(LocalDateTime.now()); // postavljanje vremena kreiranja
        return postRepository.save(post);
    }

    @Transactional
    public Comment addComment(Long postId, Comment comment) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (!rateLimiter.allowRequest(comment.getUserId())) {
            throw new RuntimeException("You have exceeded the limit (max 5 per minute)");
        }

        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
        long commentCount = commentRepository.findByUserIdAndCreatedAtAfter(comment.getUserId(), oneHourAgo).size();

        if (commentCount >= 60) {
            throw new RuntimeException("You have exceeded the limit of 60 comments per hour.");
        }

        comment.setPostId(postId);
        comment.setCreatedAt(LocalDateTime.now());
        return commentRepository.save(comment);
    }

    @Transactional
    public Like addLike(Long postId, Long userId) {
        Post post = postRepository.lockPostForUpdate(postId);
        if (post == null) {
            throw new RuntimeException("Post not found");
        }

        Optional<Like> existingLike = likeRepository.findByPostIdAndUserId(postId, userId);
        if (!existingLike.isPresent()) {
            Like like = new Like();
            like.setPostId(postId);
            like.setUserId(userId);
            like.setCreatedAt(LocalDateTime.now());
            return likeRepository.save(like);
        }

        return null;
    }

    public long getLikeCount(Long postId) {
        return likeRepository.countByPostId(postId);
    }

    public List<Comment> getComments(Long postId) {
        return commentRepository.findByPostId(postId);
    }

    @Cacheable(value = "posts", key = "#postId")
    public Post getPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found")); // Throws an exception if post doesn't exist
    }

    @Cacheable(value = "allPosts")
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post updatePost(Long postId, Long userId, String description, String imageUrl) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Post not found"));

        if (!post.getUserId().equals(userId)) {
            throw new SecurityException("You are not authorized to update this post");
        }

        post.setDescription(description);
        post.setImageUrl(imageUrl);

        return postRepository.save(post);
    }

    @Transactional
    public void deletePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        if (!post.getUserId().equals(userId)) {
            throw new SecurityException("You are not authorized to delete this post");
        }

        commentRepository.deleteByPostId(postId);
        likeRepository.deleteByPostId(postId);
        postRepository.delete(post);
    }
    
    public List<Post> getPostsByUserId(Long userId) {
        return postRepository.findByUserId(userId);
    }

    public List<Post> getPostsFor1M() {
        List<Post> posts = postRepository.findAll();
        List<Post> postsFor1M = new ArrayList<>();
        for (Post post : posts) {
            if(isPostCreatedWithin(post, "1m")){
                postsFor1M.add(post);
            }
        }
        return postsFor1M;
    }

    public boolean isPostCreatedWithin(Post post, String duration) {
        if (duration == null || duration.length() < 2) {
            throw new IllegalArgumentException("Invalid duration format. Example: '1d', '1m', '1y'.");
        }

        int amount;
        try {
            amount = Integer.parseInt(duration.substring(0, duration.length() - 1));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid numeric value in duration.");
        }
        char unit = duration.charAt(duration.length() - 1);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime threshold;

        switch (unit) {
            case 'd':
                threshold = now.minus(amount, ChronoUnit.DAYS);
                break;
            case 'm':
                threshold = now.minus(amount, ChronoUnit.MONTHS);
                break;
            case 'y':
                threshold = now.minus(amount, ChronoUnit.YEARS);
                break;
            default:
                throw new IllegalArgumentException("Invalid time unit. Use 'd' for days, 'm' for months, 'y' for years.");
        }

        return post.getCreatedAt().isAfter(threshold);
    }

    @Cacheable(value = "postLocations", key = "#postId")
    public LocationDto getLocationForPost(Long postId) {
        return postRepository.findLocationByPostId(postId);
    }

  public List<Post> getPostsFromFollowedUsers(Long userId) {
        List<User> followedUsers = userFollowerRepository.findFolloweesByUserId(userId);

        if (followedUsers.isEmpty()) {
            return new ArrayList<>();
        }

        List<Long> followedUserIds = followedUsers.stream()
                .map(User::getId)
                .toList();

        return postRepository.findByUserIdIn(followedUserIds);
    }
}
