package com.group27.OnlyBuns.repository;

import com.group27.OnlyBuns.model.User;
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

    @Query("SELECT uf.follower FROM UserFollower uf WHERE uf.followee.id = :userId")
    List<User> findFollowersByUserId(@Param("userId") Long userId);

    // Lista korisnika koje prati korisnik (followees)
    @Query("SELECT uf.followee FROM UserFollower uf WHERE uf.follower.id = :userId")
    List<User> findFolloweesByUserId(@Param("userId") Long userId);

    // Provera da li follower prati followee
    boolean existsByFollowerIdAndFolloweeId(Long followerId, Long followeeId);

    // Brisanje veze praćenja između follower i followee
    void deleteByFollowerIdAndFolloweeId(Long followerId, Long followeeId);

}