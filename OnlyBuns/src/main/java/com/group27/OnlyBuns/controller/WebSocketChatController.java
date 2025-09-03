package com.group27.OnlyBuns.controller;

import com.group27.OnlyBuns.config.WebSocketEventListener;
import com.group27.OnlyBuns.service.MessageService;
import dto.SendMessageRequest;
import dto.TypingIndicatorRequestDTO;
import dto.RegisterRequestDTO; // Use existing DTO
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketChatController {

    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;
    private final WebSocketEventListener webSocketEventListener;

    public WebSocketChatController(MessageService messageService,
                                   SimpMessagingTemplate messagingTemplate,
                                   WebSocketEventListener webSocketEventListener) {
        this.messageService = messageService;
        this.messagingTemplate = messagingTemplate;
        this.webSocketEventListener = webSocketEventListener;
    }

    @MessageMapping("/chat/{chatId}/send")
    public void sendMessage(@DestinationVariable Long chatId,
                            @Payload SendMessageRequest request,
                            SimpMessageHeaderAccessor headerAccessor) {
        try {
            messageService.sendMessage(chatId, request.getSenderId(), request.getContent());
        } catch (RuntimeException e) {
            String sessionId = headerAccessor.getSessionId();
            if (sessionId != null) {
                messagingTemplate.convertAndSendToUser(
                        sessionId,
                        "/queue/errors",
                        "Error sending message: " + e.getMessage()
                );
            }
        }
    }

    @MessageMapping("/chat/{chatId}/typing")
    public void handleTyping(@DestinationVariable Long chatId,
                             @Payload TypingIndicatorRequestDTO request) {
        try {
            messageService.broadcastTypingIndicator(chatId, request.getUserId(), request.getUsername(), request.isTyping());
        } catch (RuntimeException e) {
            System.err.println("Error handling typing indicator: " + e.getMessage());
        }
    }

    @MessageMapping("/register")
    public void registerUser(@Payload RegisterRequestDTO request, SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();
        if (sessionId != null && request.getUsername() != null) {
            webSocketEventListener.registerUserSession(sessionId, request.getUsername());
        }
    }
}