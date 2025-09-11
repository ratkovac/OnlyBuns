package com.group27.OnlyBuns.repository;

import com.group27.OnlyBuns.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE " +
            "(:firstName IS NULL OR u.firstName LIKE %:firstName%) AND " +
            "(:lastName IS NULL OR u.lastName LIKE %:lastName%) AND " +
            "(:email IS NULL OR u.email LIKE %:email%) AND " +
            "(:username IS NULL OR u.username LIKE %:username%) AND " +
            "(:minPosts IS NULL OR (SELECT COUNT(p) FROM Post p WHERE p.userId = u.id) >= :minPosts) AND " +
            "(:maxPosts IS NULL OR (SELECT COUNT(p) FROM Post p WHERE p.userId = u.id) <= :maxPosts) AND " +
            "u.role = 'user' AND u.id <> 9999")
    Page<User> findUsersByCriteria(@Param("firstName") String firstName,
                                   @Param("lastName") String lastName,
                                   @Param("email") String email,
                                   @Param("username") String username,
                                   @Param("minPosts") Long minPosts,
                                   @Param("maxPosts") Long maxPosts,
                                   Pageable pageable);


    @Query("SELECT u FROM User u LEFT JOIN UserFollower f ON u.id = f.follower.id " +
            "WHERE u.role = 'user' AND u.id <> 9999" +
            "GROUP BY u.id " +
            "ORDER BY " +
            "CASE WHEN :sortDirection = 'DESC' THEN COUNT(f.followee.id) END DESC, " +
            "CASE WHEN :sortDirection = 'ASC' THEN COUNT(f.followee.id) END ASC")
    Page<User> findUsersSortedByFollowingCount(@Param("sortDirection") String sortDirection, Pageable pageable);


    @Query("SELECT u FROM User u WHERE u.role = 'user' AND u.id <> 9999" +
            "ORDER BY " +
            "CASE WHEN :sortDirection = 'DESC' THEN u.email END DESC, " +
            "CASE WHEN :sortDirection = 'ASC' THEN u.email END ASC")
    Page<User> findUsersSortedByEmail(@Param("sortDirection") String sortDirection, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.role <> 'admin' AND u.id <> 9999")
    Page<User> findAllNonAdminUsers(Pageable pageable);


    User findByUsername(String username);
    User findByEmail(String email);
    List<User> findByUsernameStartingWithIgnoreCase(String prefix);
    Optional<User> findOptionalByUsername(String username);

    @Query("SELECT COALESCE(MAX(u.id), 0) FROM User u")
    Long findMaxId();

    User getUsersById(long userId);
    User findById(long id);

    @Query("SELECT u FROM User u WHERE u.isActive = false AND u.role = 'user' AND u.id <> 9999")
    List<User> findInactiveUsers();

    @Query("SELECT u FROM User u WHERE u.lastLoginTime < :cutoffDate")
    List<User> findInactiveUsersSince(@Param("cutoffDate") LocalDateTime cutoffDate);

    @Query("SELECT COUNT(u) FROM User u WHERE u.isActive = true AND u.role = 'user' AND u.id <> 9999")
    long countActiveUsers();

    @Query("SELECT u.username FROM User u")
    List<String> findAllUsernames();
}