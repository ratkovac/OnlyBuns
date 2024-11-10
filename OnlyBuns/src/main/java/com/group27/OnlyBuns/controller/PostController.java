package com.group27.OnlyBuns.controller;

import com.group27.OnlyBuns.model.Comment;
import com.group27.OnlyBuns.model.Like;
import com.group27.OnlyBuns.model.Post;
import com.group27.OnlyBuns.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }
}
