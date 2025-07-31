package com.group27.OnlyBuns.service;

import com.group27.OnlyBuns.model.Comment;
import com.group27.OnlyBuns.model.Like;
import com.group27.OnlyBuns.model.Post;
import com.group27.OnlyBuns.repository.CommentRepository;
import com.group27.OnlyBuns.repository.LikeRepository;
import com.group27.OnlyBuns.repository.PostRepository;
import dto.LocationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;


@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private LikeRepository likeRepository;

    // Kreiranje nove objave
    public Post createPost(Post post) {
        post.setCreatedAt(LocalDateTime.now()); // postavljanje vremena kreiranja
        return postRepository.save(post);
    }

    @Transactional
    public Comment addComment(Long postId, Comment comment) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
        long commentCount = commentRepository.findByUserIdAndCreatedAtAfter(comment.getUserId(), oneHourAgo).size();

        if (commentCount >= 10) {
            throw new RuntimeException("You have exceeded the limit of 60 comments per hour.");
        }

        comment.setPostId(postId);
        comment.setCreatedAt(LocalDateTime.now());
        return commentRepository.save(comment);
    }

    @Transactional
    public Like addLike(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        postRepository.lockPostForUpdate(postId);

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

        // Provera da li je korisnik koji pokušava da ažurira objavu isti kao korisnik koji je postavio objavu
        if (!post.getUserId().equals(userId)) {
            throw new SecurityException("You are not authorized to update this post");
        }

        // Ažuriranje samo description i imageUrl
        post.setDescription(description);
        post.setImageUrl(imageUrl);

        // Čuvanje ažurirane objave
        return postRepository.save(post);
    }

    @Transactional
    public void deletePost(Long postId, Long userId) {
        // Dohvati post koji želimo da obrišemo
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        // Provera da li je korisnik koji pokušava da obriše objavu isti kao korisnik koji je postavio objavu
        if (!post.getUserId().equals(userId)) {
            throw new SecurityException("You are not authorized to delete this post");
        }

        // Prvo obriši sve komentare koji su vezani za ovu objavu
        commentRepository.deleteByPostId(postId);

        // Zatim obriši sve lajkove koji su vezani za ovu objavu
        likeRepository.deleteByPostId(postId);

        // Na kraju obriši samu objavu
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
            case 'd': // Dani
                threshold = now.minus(amount, ChronoUnit.DAYS);
                break;
            case 'm': // Meseci
                threshold = now.minus(amount, ChronoUnit.MONTHS);
                break;
            case 'y': // Godine
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
}
