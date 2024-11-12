package com.group27.OnlyBuns.repository;

import com.group27.OnlyBuns.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE " +
            "(:firstName IS NULL OR u.firstName LIKE %:firstName%) AND " +
            "(:lastName IS NULL OR u.lastName LIKE %:lastName%) AND " +
            "(:email IS NULL OR u.email LIKE %:email%) AND " +
            "(:minPosts IS NULL OR (SELECT COUNT(p) FROM Post p WHERE p.userId = u.id) >= :minPosts) AND " +
            "(:maxPosts IS NULL OR (SELECT COUNT(p) FROM Post p WHERE p.userId = u.id) <= :maxPosts) AND " +
            "u.role = 'user'")
    List<User> findUsersByCriteria(@Param("firstName") String firstName,
                                   @Param("lastName") String lastName,
                                   @Param("email") String email,
                                   @Param("minPosts") Long minPosts,
                                   @Param("maxPosts") Long maxPosts);

    @Query("SELECT u FROM User u LEFT JOIN UserFollower f ON u.id = f.follower.id " +
            "WHERE u.role = 'user' " +
            "GROUP BY u.id " +
            "ORDER BY " +
            "CASE WHEN :sortDirection = 'DESC' THEN COUNT(f.followee.id) END DESC, " +
            "CASE WHEN :sortDirection = 'ASC' THEN COUNT(f.followee.id) END ASC")
    List<User> findUsersSortedByFollowingCount(@Param("sortDirection") String sortDirection);

    @Query("SELECT u FROM User u WHERE u.role = 'user' " +
            "ORDER BY " +
            "CASE WHEN :sortDirection = 'DESC' THEN u.email END DESC, " +
            "CASE WHEN :sortDirection = 'ASC' THEN u.email END ASC")
    List<User> findUsersSortedByEmail(@Param("sortDirection") String sortDirection);

    User findByUsername(String username);
    User findByEmail(String email);

    @Query("SELECT COALESCE(MAX(u.id), 0) FROM User u")
    Long findMaxId();

    User getUsersById(long userId);
    User findById(long id);
}
