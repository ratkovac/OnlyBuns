package com.group27.OnlyBuns.controller;

import com.group27.OnlyBuns.model.Comment;
import com.group27.OnlyBuns.model.Like;
import com.group27.OnlyBuns.model.Post;
import com.group27.OnlyBuns.service.PostService;
import com.group27.OnlyBuns.service.LikeService;
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

    @Autowired
    private LikeService likeService;

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

    @GetMapping("/{postId}/likes/check")
    public ResponseEntity<Boolean> checkIfUserLiked(
            @PathVariable Long postId,
            @RequestParam Long userId) {

        boolean isLiked = likeService.isPostLikedByUser(postId, userId);

        // Vraćamo true ili false u zavisnosti od toga da li je korisnik lajkovao
        return new ResponseEntity<>(isLiked, HttpStatus.OK);
    }

    // Dohvat svih objava
    @GetMapping
    public List<PostDTO> getAllPosts() {
        List<PostDTO> postDTOs = new ArrayList<>();
        List<Post> posts = postService.getAllPosts();

        for (Post post : posts) {
            long likeCount = postService.getLikeCount(post.getId());
            List<Comment> comments = postService.getComments(post.getId());

            PostDTO postDTO = new PostDTO(post, likeCount, comments);
            postDTOs.add(postDTO);
        }
        return postDTOs;
    }

    @GetMapping("/{postId}")
    public Post getPostById(@PathVariable Long postId) {

        return postService.getPostById(postId);
    }

    @PutMapping("/{postId}")
    public Post updatePost(
            @PathVariable Long postId,
            @RequestBody Post updatedPost,
            @RequestParam Long userId) {

        // Pozivamo servis za ažuriranje objave
        return postService.updatePost(postId, userId, updatedPost.getDescription(), updatedPost.getImageUrl());
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long postId,
            @RequestParam Long userId) {

        // Pozivamo servis za brisanje objave
        postService.deletePost(postId, userId);

        // Vraćamo HTTP 204 status (No Content)
        return ResponseEntity.noContent().build();
    }
}
