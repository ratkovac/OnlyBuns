package com.group27.OnlyBuns.service;

import com.group27.OnlyBuns.model.*;
import com.group27.OnlyBuns.repository.ChatMemberRepository;
import com.group27.OnlyBuns.repository.ChatRepository;
import com.group27.OnlyBuns.repository.MessageRepository;
import com.group27.OnlyBuns.repository.UserRepository;
import dto.MessageDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MessageService {

    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    private final ChatMemberRepository chatMemberRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public MessageService(MessageRepository messageRepository,
                          ChatRepository chatRepository,
                          ChatMemberRepository chatMemberRepository,
                          UserRepository userRepository,
                          SimpMessagingTemplate messagingTemplate) {
        this.messageRepository = messageRepository;
        this.chatRepository = chatRepository;
        this.chatMemberRepository = chatMemberRepository;
        this.userRepository = userRepository;
        this.messagingTemplate = messagingTemplate;
    }

    public MessageDTO sendMessage(Long chatId, Long senderId, String content) {
        chatMemberRepository.findByUserIdAndChatId(senderId, chatId)
                .orElseThrow(() -> new RuntimeException("User is not a member of this chat"));

        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Chat not found"));

        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        Message message = new Message();
        message.setContent(content);
        message.setSender(sender);
        message.setChat(chat);
        message.setType(ChatMessageType.TEXT);

        Message savedMessage = messageRepository.save(message);

        MessageDTO messageDTO = convertToDTO(savedMessage);

        messagingTemplate.convertAndSend(
                "/topic/chat/" + chatId,
                messageDTO
        );

        return messageDTO;
    }

    public void sendSystemMessage(Long chatId, String content) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Chat not found"));

        User systemUser = userRepository.findOptionalByUsername("SystemBot")
                .orElseThrow(() -> new RuntimeException("System user not found"));

        Message message = new Message();
        message.setChat(chat);
        message.setContent(content);
        message.setType(ChatMessageType.SYSTEM);
        message.setSender(systemUser);

        Message saved = messageRepository.save(message);
        MessageDTO dto = convertToDTO(saved);

        messagingTemplate.convertAndSend("/topic/chat/" + chatId, dto);
    }

    public List<MessageDTO> getChatMessages(Long chatId, Long userId) {
        ChatMember member = chatMemberRepository.findByUserIdAndChatId(userId, chatId)
                .orElseThrow(() -> new RuntimeException("User is not a member"));

        Pageable pageable = PageRequest.of(0, 100);
        return messageRepository.findByChatIdAndTimestampAfterOrderByTimestampDesc(
                        chatId, member.getJoinedAt(), pageable)
                .getContent()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    private MessageDTO convertToDTO(Message message) {
        MessageDTO dto = new MessageDTO();
        dto.setId(message.getId());
        dto.setContent(message.getContent());
        dto.setSenderId(message.getSender().getId());
        dto.setSenderUsername(message.getSender().getUsername());
        dto.setSenderFullName(message.getSender().getFirstName() + " " + message.getSender().getLastName());
        dto.setChatId(message.getChat().getId());
        dto.setTimestamp(message.getTimestamp());
        dto.setType(message.getType());
        return dto;
    }

    public void deleteAllMessagesForChat(Long chatId) {
        messageRepository.deleteByChatId(chatId);
    }
}