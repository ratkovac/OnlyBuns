package com.group27.OnlyBuns.controller;

import com.group27.OnlyBuns.model.Comment;
import com.group27.OnlyBuns.model.Like;
import com.group27.OnlyBuns.model.Post;
import com.group27.OnlyBuns.service.PostService;
import dto.PostDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/posts")
@CrossOrigin(origins = "http://localhost:4200")
public class PostController {

    @Autowired
    private PostService postService;

    // Kreiranje nove objave
    @PostMapping
    public Post createPost(@RequestBody Post post) {
        return postService.createPost(post);
    }

    // Dodavanje komentara na post
    @PostMapping("/{postId}/comments")
    public Comment addComment(@PathVariable Long postId, @RequestBody Comment comment) {
        return postService.addComment(postId, comment);
    }

    // Lajkovanje posta
    @PostMapping("/{postId}/likes")
    public Like addLike(@PathVariable Long postId, @RequestParam Long userId) {
        return postService.addLike(postId, userId);
    }

    // Dohvat svih objava
    @GetMapping
    public List<PostDTO> getAllPosts() {
        try {
            List<PostDTO> postDTOs = new ArrayList<>();
            List<Post> posts = postService.getAllPosts();

            for (Post post : posts) {
                long likeCount = postService.getLikeCount(post.getId());
                List<Comment> comments = postService.getComments(post.getId());

                PostDTO postDTO = new PostDTO(post, likeCount, comments);
                postDTOs.add(postDTO);
            }
            return postDTOs;
        } catch (Exception e) {
            // Log the error for debugging
            System.err.println("Error fetching posts: " + e.getMessage());
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch posts", e);
        }
    }

    @GetMapping("/{postId}")
    public PostDTO getPostById(@PathVariable Long postId) {
        try {
            Post post = postService.getPostById(postId);  // Fetch the post by ID
            long likeCount = postService.getLikeCount(postId);  // Get like count for the post
            List<Comment> comments = postService.getComments(postId);  // Get comments for the post

            // Map the Post to PostDTO
            return new PostDTO(post, likeCount, comments);
        } catch (Exception e) {
            // Log the error for debugging
            System.err.println("Error fetching post with ID " + postId + ": " + e.getMessage());
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found", e);
        }
    }
}
