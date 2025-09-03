package com.group27.OnlyBuns.service;

import com.group27.OnlyBuns.model.*;
import com.group27.OnlyBuns.repository.ChatMemberRepository;
import com.group27.OnlyBuns.repository.ChatRepository;
import com.group27.OnlyBuns.repository.UserRepository;
import dto.ChatDTO;
import dto.ChatMemberDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ChatService {

    private final ChatRepository chatRepository;
    private final ChatMemberRepository chatMemberRepository;
    private final UserRepository userRepository;
    private final MessageService messageService;

    public ChatService(ChatRepository chatRepository,
                       ChatMemberRepository chatMemberRepository,
                       UserRepository userRepository,
                       MessageService messageService) {
        this.chatRepository = chatRepository;
        this.chatMemberRepository = chatMemberRepository;
        this.userRepository = userRepository;
        this.messageService = messageService;
    }

    public ChatDTO createChat(String name, String description, Long adminId) {
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        Chat chat = new Chat();
        chat.setName(name);
        chat.setDescription(description);
        chat.setAdmin(admin);

        Chat savedChat = chatRepository.save(chat);

        ChatMember adminMember = new ChatMember();
        adminMember.setChat(savedChat);
        adminMember.setUser(admin);
        adminMember.setRole(MemberRole.ADMIN);
        chatMemberRepository.save(adminMember);

        messageService.sendSystemMessage(savedChat.getId(), "Chat created by " + admin.getUsername());

        return convertToDTO(savedChat);
    }

    public List<ChatDTO> getUserChats(Long userId) {
        List<Chat> chats = chatRepository.findActiveChatsByUserId(userId);
        return chats.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ChatDTO getChatById(Long chatId) {
        Chat chat = chatRepository.findByIdAndIsActiveTrue(chatId)
                .orElseThrow(() -> new RuntimeException("Chat not found"));
        return convertToDTO(chat);
    }

    public void addUserToChatByUsername(Long chatId, String username, Long adminId) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Chat not found"));

        if (!chat.getAdmin().getId().equals(adminId)) {
            throw new RuntimeException("Only admin can add members");
        }

        User user = userRepository.findOptionalByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Optional<ChatMember> existingMember = chatMemberRepository.findByUserIdAndChatId(user.getId(), chatId);
        if (existingMember.isPresent()) {
            throw new RuntimeException("User is already a member");
        }

        ChatMember member = new ChatMember();
        member.setChat(chat);
        member.setUser(user);
        member.setRole(MemberRole.MEMBER);
        chatMemberRepository.save(member);

        messageService.broadcastUserJoined(chatId, user.getUsername());
    }

    public void removeUserFromChatByUsername(Long chatId, String username, Long adminId) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Chat not found"));

        if (!chat.getAdmin().getId().equals(adminId)) {
            throw new RuntimeException("Only admin can remove members");
        }

        User user = userRepository.findOptionalByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ChatMember member = chatMemberRepository.findByUserIdAndChatId(user.getId(), chatId)
                .filter(ChatMember::isActive)
                .orElseThrow(() -> new RuntimeException("User not found in chat"));

        member.setActive(false);
        chatMemberRepository.save(member);

        messageService.broadcastUserLeft(chatId, user.getUsername());
    }

    public List<ChatMemberDTO> searchUsersByUsernamePrefix(String prefix) {
        return userRepository.findByUsernameStartingWithIgnoreCase(prefix).stream()
                .map(user -> {
                    ChatMemberDTO dto = new ChatMemberDTO();
                    dto.setUserId(user.getId());
                    dto.setUsername(user.getUsername());
                    dto.setFullName(user.getFirstName() + " " + user.getLastName());
                    return dto;
                }).collect(Collectors.toList());
    }

    public List<ChatMemberDTO> getActiveMembers(Long chatId) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Chat not found"));

        return chat.getMembers().stream()
                .filter(ChatMember::isActive)
                .map(this::convertMemberToDTO)
                .collect(Collectors.toList());
    }

    public void leaveChat(Long chatId, Long userId) {
        ChatMember member = chatMemberRepository.findByUserIdAndChatId(userId, chatId)
                .filter(ChatMember::isActive)
                .orElseThrow(() -> new RuntimeException("User not found in chat"));

        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Chat not found"));

        if (chat.getAdmin().getId().equals(userId)) {
            throw new RuntimeException("Admin cannot leave the chat. Transfer admin rights first.");
        }

        member.setActive(false);
        chatMemberRepository.save(member);

        messageService.broadcastUserLeft(chatId, member.getUser().getUsername());
    }

    @Transactional
    public void deleteChat(Long chatId, Long adminId) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Chat not found"));

        if (!chat.getAdmin().getId().equals(adminId)) {
            throw new RuntimeException("Only admin can delete the chat");
        }

        messageService.broadcastChatUpdate(chatId, "Chat has been deleted by admin");

        messageService.deleteAllMessagesForChat(chatId);
        chatMemberRepository.deleteByChatId(chatId);
        chatRepository.delete(chat);
    }

    public void updateChatInfo(Long chatId, String newName, String newDescription, Long adminId) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Chat not found"));

        if (!chat.getAdmin().getId().equals(adminId)) {
            throw new RuntimeException("Only admin can update chat information");
        }

        boolean updated = false;
        StringBuilder updateMessage = new StringBuilder("Chat updated: ");

        if (newName != null && !newName.equals(chat.getName())) {
            chat.setName(newName);
            updateMessage.append("name changed to '").append(newName).append("' ");
            updated = true;
        }

        if (newDescription != null && !newDescription.equals(chat.getDescription())) {
            chat.setDescription(newDescription);
            updateMessage.append("description updated ");
            updated = true;
        }

        if (updated) {
            chatRepository.save(chat);
            messageService.broadcastChatUpdate(chatId, updateMessage.toString());
        }
    }

    private ChatDTO convertToDTO(Chat chat) {
        ChatDTO dto = new ChatDTO();
        dto.setId(chat.getId());
        dto.setName(chat.getName());
        dto.setDescription(chat.getDescription());
        dto.setAdminId(chat.getAdmin().getId());
        dto.setAdminUsername(chat.getAdmin().getUsername());
        dto.setCreatedAt(chat.getCreatedAt());
        dto.setActive(chat.isActive());

        List<ChatMemberDTO> memberDTOs = chat.getMembers().stream()
                .filter(ChatMember::isActive)
                .map(this::convertMemberToDTO)
                .collect(Collectors.toList());

        dto.setMembers(memberDTOs);
        dto.setMemberCount(memberDTOs.size());

        return dto;
    }

    private ChatMemberDTO convertMemberToDTO(ChatMember member) {
        ChatMemberDTO dto = new ChatMemberDTO();
        dto.setId(member.getId());
        dto.setUserId(member.getUser().getId());
        dto.setUsername(member.getUser().getUsername());
        dto.setFullName(member.getUser().getFirstName() + " " + member.getUser().getLastName());
        dto.setChatId(member.getChat().getId());
        dto.setJoinedAt(member.getJoinedAt());
        dto.setRole(member.getRole());
        dto.setActive(member.isActive());
        return dto;
    }
}