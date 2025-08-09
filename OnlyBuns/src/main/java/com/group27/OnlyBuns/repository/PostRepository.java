package com.group27.OnlyBuns.repository;

import com.group27.OnlyBuns.model.Post;
import dto.LocationDto;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    long countByUserId(Long userId);

    List<Post> findByUserId(Long userId);

    @Query("SELECT new dto.LocationDto(p.latitude, p.longitude) FROM Post p WHERE p.id = :postId")
    LocationDto findLocationByPostId(Long postId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Post p WHERE p.id = :postId")
    Post lockPostForUpdate(@Param("postId") Long postId);
    long countByCreatedAtAfter(LocalDateTime dateTime);
    List<Post> findByUserIdIn(List<Long> userIds);
}