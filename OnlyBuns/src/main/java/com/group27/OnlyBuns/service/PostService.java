package com.group27.OnlyBuns.service;

import com.group27.OnlyBuns.model.Comment;
import com.group27.OnlyBuns.model.Like;
import com.group27.OnlyBuns.model.Post;
import com.group27.OnlyBuns.repository.CommentRepository;
import com.group27.OnlyBuns.repository.LikeRepository;
import com.group27.OnlyBuns.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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

    // Dodavanje komentara
    public Comment addComment(Long postId, Comment comment) {
        comment.setPostId(postId); // post koji je komentarisao
        comment.setCreatedAt(LocalDateTime.now()); // postavljanje vremena kreiranja
        return commentRepository.save(comment);
    }

    // Lajkovanje objave
    public Like addLike(Long postId, Long userId) {
        Like like = new Like();
        like.setPostId(postId);
        like.setUserId(userId);
        return likeRepository.save(like);
    }

    // Dohvat svih objava
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }
}
