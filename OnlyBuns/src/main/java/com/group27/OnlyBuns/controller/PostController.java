package com.group27.OnlyBuns.controller;

import com.group27.OnlyBuns.model.Comment;
import com.group27.OnlyBuns.model.Like;
import com.group27.OnlyBuns.model.Post;
import com.group27.OnlyBuns.model.User;
import com.group27.OnlyBuns.service.PostService;
import com.group27.OnlyBuns.service.LikeService;
import com.group27.OnlyBuns.service.UserService;
import dto.LocationDto;
import dto.PostDTO;
import io.micrometer.core.annotation.Timed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/posts")
@CrossOrigin(origins = "http://localhost:4200")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private UserService userService;

    // Kreiranje nove objave
    @Timed(value = "http.create_post.duration", description = "Duration of create post HTTP request")
    @PostMapping("/createPost")
    public Post createPost(@RequestBody Post post) {
        return postService.createPost(post);
    }

    @PostMapping("/{postId}/comments")
    public ResponseEntity<Comment> addComment(@PathVariable Long postId, @RequestBody Comment comment) {
        try {
            Comment addedComment = postService.addComment(postId, comment);
            return new ResponseEntity<>(addedComment, HttpStatus.CREATED);
        } catch (RuntimeException ex) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/{postId}/likes")
    public Like addLike(@PathVariable Long postId, @RequestParam Long userId) {
        boolean isLiked = likeService.isPostLikedByUser(postId, userId);
        if(!isLiked) {
            return postService.addLike(postId, userId);
        }
        return null;
    }

    @GetMapping("/{postId}/likes/check")
    public ResponseEntity<Boolean> checkIfUserLiked(
            @PathVariable Long postId,
            @RequestParam Long userId) {

        boolean isLiked = likeService.isPostLikedByUser(postId, userId);

        return new ResponseEntity<>(isLiked, HttpStatus.OK);
    }

    @GetMapping
    public List<PostDTO> getAllPosts() {
        List<PostDTO> postDTOs = new ArrayList<>();
        List<Post> posts = postService.getAllPosts();

        for (Post post : posts) {
            long likeCount = postService.getLikeCount(post.getId());
            List<Comment> comments = postService.getComments(post.getId())
                    .stream()
                    .sorted(Comparator.comparing(Comment::getCreatedAt).reversed())
                    .collect(Collectors.toList());
            PostDTO postDTO = new PostDTO(post, likeCount, comments, post.getCreatedAt());
            postDTOs.add(postDTO);
        }
        return postDTOs;
    }

    @GetMapping("/getAllUsersPosts")
    public List<Post> getAllUsersPosts() {
        return postService.getAllPosts();
    }

    @GetMapping("/mostPopularLast7Days")
    public List<PostDTO> getMostPopularPostsLast7Days() {
        List<Long> postIds = likeService.getTop5LikedPostIdsInLast7Days();
        List<PostDTO> posts = new ArrayList<>();
        for (Long postId : postIds) {
            Post post = postService.getPostById(postId);

            long likeCount = postService.getLikeCount(post.getId());
            List<Comment> comments = postService.getComments(post.getId());

            PostDTO postDTO = new PostDTO(post, likeCount, comments, post.getCreatedAt());
            posts.add(postDTO);
        }
        return posts;
    }

    @GetMapping("/mostPopularAllTime")
    public List<PostDTO> getMostPopularPostsAllTime() {
        List<Long> postIds = likeService.getTop10LikedPostIdsAllTime();
        List<PostDTO> posts = new ArrayList<>();
        for (Long postId : postIds) {
            Post post = postService.getPostById(postId);

            long likeCount = postService.getLikeCount(post.getId());
            List<Comment> comments = postService.getComments(post.getId());

            PostDTO postDTO = new PostDTO(post, likeCount, comments, post.getCreatedAt());
            posts.add(postDTO);
        }
        return posts;
    }

    @GetMapping("/mostActiveUsersLast7Days")
    public List<User> getMostActiveUsersLast7Days() {
        List<Long> userIds = likeService.getMostActiveUserIdsInLast7Days();
        List<User> users = new ArrayList<>();
        for (Long userId : userIds) {
            userService.getUserById(userId).ifPresent(users::add);
        }
        return users;
    }

    @GetMapping("/count")
    public int getAllPostsCount() {
        List<Post> posts = postService.getAllPosts();
        return posts.size();
    }

    @GetMapping("/countFor1M")
    public int getPostsFor1MonthCount(){
        List<Post> posts = postService.getPostsFor1M();
        return posts.size();
    }

    @GetMapping("/user/{userId}")
    public List<Post> getPostsByUserId(@PathVariable Long userId) {
        return postService.getPostsByUserId(userId);
    }

    @GetMapping("/{postId}")
    public PostDTO getPostById(@PathVariable Long postId) {

        Post post = postService.getPostById(postId);
        long likeCount = postService.getLikeCount(post.getId());
        List<Comment> comments = postService.getComments(post.getId());

        PostDTO postDTO = new PostDTO(post, likeCount, comments, post.getCreatedAt());
        return postDTO;
    }


    @PutMapping("/{postId}")
    public Post updatePost(
            @PathVariable Long postId,
            @RequestBody Post updatedPost,
            @RequestParam Long userId) {

        return postService.updatePost(postId, userId, updatedPost.getDescription(), updatedPost.getImageUrl());
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long postId,
            @RequestParam Long userId) {

        postService.deletePost(postId, userId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{postId}/location")
    public LocationDto getPostLocation(@PathVariable Long postId) {
        return postService.getLocationForPost(postId);
    }

    @GetMapping("/stats/posts")
    public ResponseEntity<?> getPostStats() {
        return ResponseEntity.ok(postService.getPostCounts());
    }

    @GetMapping("/stats/comments")
    public ResponseEntity<?> getCommentStats() {
        return ResponseEntity.ok(postService.getCommentCounts());
    }

    @GetMapping("/fromFollowed")
    public List<PostDTO> getPostsFromFollowedUsers(@RequestParam Long userId) {
        List<Post> posts = postService.getPostsFromFollowedUsers(userId);
        List<PostDTO> postDTOs = new ArrayList<>();

        for (Post post : posts) {
            long likeCount = postService.getLikeCount(post.getId());
            List<Comment> comments = postService.getComments(post.getId());

            PostDTO postDTO = new PostDTO(post, likeCount, comments, post.getCreatedAt());
            postDTOs.add(postDTO);
        }

        return postDTOs;
    }
}
