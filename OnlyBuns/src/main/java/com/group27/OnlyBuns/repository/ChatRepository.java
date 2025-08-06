package com.group27.OnlyBuns.repository;

import com.group27.OnlyBuns.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    @Query("SELECT c FROM Chat c JOIN c.members m WHERE m.user.id = :userId AND m.isActive = true AND c.isActive = true")
    List<Chat> findActiveChatsByUserId(@Param("userId") Long userId);
    Optional<Chat> findByIdAndIsActiveTrue(Long id);
}
