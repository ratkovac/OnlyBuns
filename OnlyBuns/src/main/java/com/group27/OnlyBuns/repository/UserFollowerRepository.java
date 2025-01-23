package com.group27.OnlyBuns.repository;

import com.group27.OnlyBuns.model.UserFollower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserFollowerRepository extends JpaRepository<UserFollower, Long> {
    long countByFollowerId(Long followerId);

    List<UserFollower> getAllByFolloweeId(Long userId);

    List<UserFollower> getAllByFollowerId(Long followerId);

}