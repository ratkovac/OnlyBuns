package com.group27.OnlyBuns.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Likes")
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId; // ID korisnika koji je lajkovao

    @Column(nullable = false)
    private Long postId; // ID posta koji je lajkovan

    @Column(nullable = false, unique = false)
    private LocalDateTime createdAt; // vreme lajkovanja

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
