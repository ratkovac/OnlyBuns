package com.group27.OnlyBuns.repository;

import com.group27.OnlyBuns.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
    long countByPostId(Long postId);
    void deleteByPostId(Long postId);
}
