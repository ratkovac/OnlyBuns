package com.group27.OnlyBuns.repository;

import com.group27.OnlyBuns.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    User findByEmail(String email);

    @Query("SELECT COALESCE(MAX(u.id), 0) FROM User u")
    Long findMaxId();

    User getUsersById(long userId);
    User findById(long id);
}
