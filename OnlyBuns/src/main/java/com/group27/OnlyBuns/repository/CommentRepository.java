package com.group27.OnlyBuns.repository;

import com.group27.OnlyBuns.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
