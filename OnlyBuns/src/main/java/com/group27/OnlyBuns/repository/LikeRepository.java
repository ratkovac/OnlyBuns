package com.group27.OnlyBuns.repository;

import com.group27.OnlyBuns.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    long countByPostId(Long postId);
    void deleteByPostId(Long postId);
    Optional<Like> findByPostIdAndUserId(Long postId, Long userId);
}
