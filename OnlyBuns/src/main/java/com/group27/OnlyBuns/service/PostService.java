package com.group27.OnlyBuns.service;

import com.group27.OnlyBuns.model.Comment;
import com.group27.OnlyBuns.model.Like;
import com.group27.OnlyBuns.model.Post;
import com.group27.OnlyBuns.repository.CommentRepository;
import com.group27.OnlyBuns.repository.LikeRepository;
import com.group27.OnlyBuns.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
