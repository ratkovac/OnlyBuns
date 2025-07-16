package com.group27.OnlyBuns.repository;

import com.group27.OnlyBuns.model.ChatMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatMemberRepository extends JpaRepository<ChatMember, Long> {
    @Query("SELECT cm FROM ChatMember cm WHERE cm.user.id = :userId AND cm.chat.id = :chatId AND cm.isActive = true")
    Optional<ChatMember> findByUserIdAndChatId(@Param("userId") Long userId, @Param("chatId") Long chatId);

    @Modifying
    @Query("DELETE FROM ChatMember cm WHERE cm.chat.id = :chatId")
    void deleteByChatId(@Param("chatId") Long chatId);
}
