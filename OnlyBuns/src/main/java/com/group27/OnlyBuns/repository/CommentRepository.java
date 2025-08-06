package com.group27.OnlyBuns.repository;

import com.group27.OnlyBuns.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostId(Long postId);
    void deleteByPostId(Long postId);
    List<Comment> findByUserIdAndCreatedAtAfter(Long userId, LocalDateTime timeLimit);
    long countByCreatedAtAfter(LocalDateTime dateTime);
    long countByUserId(Long userId);
}
