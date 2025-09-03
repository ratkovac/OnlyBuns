package com.group27.OnlyBuns.controller;

import com.group27.OnlyBuns.service.ChatService;
import com.group27.OnlyBuns.service.MessageService;
import dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chats")
@CrossOrigin(origins = "http://localhost:4200")
public class ChatController {

    private final ChatService chatService;
    private final MessageService messageService;

    public ChatController(ChatService chatService, MessageService messageService) {
        this.chatService = chatService;
        this.messageService = messageService;
    }

    @PostMapping
    public ResponseEntity<ChatDTO> createChat(@RequestBody CreateChatRequest request) {
        try {
            ChatDTO chat = chatService.createChat(
                    request.getName(),
                    request.getDescription(),
                    request.getAdminId()
            );
            return ResponseEntity.ok(chat);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ChatDTO>> getUserChats(@PathVariable Long userId) {
        try {
            List<ChatDTO> chats = chatService.getUserChats(userId);
            return ResponseEntity.ok(chats);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{chatId}")
    public ResponseEntity<ChatDTO> getChatById(@PathVariable Long chatId) {
        try {
            ChatDTO chat = chatService.getChatById(chatId);
            return ResponseEntity.ok(chat);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{chatId}/members/by-username")
    public ResponseEntity<String> addMemberByUsername(
            @PathVariable Long chatId,
            @RequestParam String username,
            @RequestParam Long adminId) {
        try {
            chatService.addUserToChatByUsername(chatId, username, adminId);
            return ResponseEntity.ok(username + " added successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{chatId}/members/by-username")
    public ResponseEntity<String> removeMemberByUsername(
            @PathVariable Long chatId,
            @RequestParam String username,
            @RequestParam Long adminId) {
        try {
            chatService.removeUserFromChatByUsername(chatId, username, adminId);
            return ResponseEntity.ok(username + " removed successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/search-users")
    public ResponseEntity<List<ChatMemberDTO>> searchUsers(@RequestParam String prefix) {
        return ResponseEntity.ok(chatService.searchUsersByUsernamePrefix(prefix));
    }

    @GetMapping("/{chatId}/messages")
    public ResponseEntity<List<MessageDTO>> getChatMessages(
            @PathVariable Long chatId,
            @RequestParam Long userId) {
        try {
            List<MessageDTO> messages = messageService.getChatMessages(chatId, userId);
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{chatId}/messages/page")
    public ResponseEntity<List<MessageDTO>> getChatMessagesWithPagination(
            @PathVariable Long chatId,
            @RequestParam Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        try {
            List<MessageDTO> messages = messageService.getChatMessagesWithPagination(chatId, userId, page, size);
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{chatId}/messages")
    public ResponseEntity<MessageDTO> sendMessage(
            @PathVariable Long chatId,
            @RequestBody SendMessageRequest request) {
        try {
            MessageDTO message = messageService.sendMessage(chatId, request.getSenderId(), request.getContent());
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{chatId}/members/active")
    public ResponseEntity<List<ChatMemberDTO>> getActiveMembers(@PathVariable Long chatId) {
        try {
            List<ChatMemberDTO> activeMembers = chatService.getActiveMembers(chatId);
            return ResponseEntity.ok(activeMembers);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{chatId}/leave")
    public ResponseEntity<String> leaveChat(
            @PathVariable Long chatId,
            @RequestParam Long userId) {
        try {
            chatService.leaveChat(chatId, userId);
            return ResponseEntity.ok("Successfully left the chat");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{chatId}")
    public ResponseEntity<String> deleteChat(
            @PathVariable Long chatId,
            @RequestParam Long adminId) {
        try {
            chatService.deleteChat(chatId, adminId);
            return ResponseEntity.ok("Chat deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{chatId}")
    public ResponseEntity<String> updateChatInfo(
            @PathVariable Long chatId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam Long adminId) {
        try {
            chatService.updateChatInfo(chatId, name, description, adminId);
            return ResponseEntity.ok("Chat updated successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}