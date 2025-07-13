package com.group27.OnlyBuns.repository;

import com.group27.OnlyBuns.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    long countByUserId(Long userId);

    List<Post> findByUserId(Long userId);
    long countByCreatedAtAfter(LocalDateTime dateTime);
}
