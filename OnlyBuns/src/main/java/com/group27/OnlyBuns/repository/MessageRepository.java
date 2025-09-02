package com.group27.OnlyBuns.repository;

import com.group27.OnlyBuns.model.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    Page<Message> findByChatIdOrderByTimestampDesc(Long chatId, Pageable pageable);

    @Modifying
    @Query("DELETE FROM Message m WHERE m.chat.id = :chatId")
    void deleteByChatId(@Param("chatId") Long chatId);
}