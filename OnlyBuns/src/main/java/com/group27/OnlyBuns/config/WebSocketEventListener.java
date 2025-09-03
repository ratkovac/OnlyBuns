package com.group27.OnlyBuns.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.*;

import java.util.Optional; // ADDED
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class WebSocketEventListener {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

    private final SimpMessageSendingOperations messagingTemplate;

    private final ConcurrentMap<String, String> sessionUserMap = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Integer> chatUserCount = new ConcurrentHashMap<>();

    public WebSocketEventListener(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        logger.info("Received a new web socket connection. Waiting for user registration...");
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();

        String username = sessionUserMap.remove(sessionId);
        if (username != null) {
            logger.info("User '{}' disconnected", username);
        }
    }

    @EventListener
    public void handleSubscribeEvent(SessionSubscribeEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        extractChatIdFromDestination(headerAccessor.getDestination()).ifPresent(chatId -> {
            logger.info("User subscribed to chat: {}", chatId);
            String userCountKey = "chat_" + chatId;

            int newCount = chatUserCount.merge(userCountKey, 1, Integer::sum);

            messagingTemplate.convertAndSend(
                    "/topic/chat/" + chatId + "/usercount",
                    newCount
            );
        });
    }

    @EventListener
    public void handleUnsubscribeEvent(SessionUnsubscribeEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        extractChatIdFromDestination(headerAccessor.getDestination()).ifPresent(chatId -> {
            logger.info("User unsubscribed from chat: {}", chatId);
            String userCountKey = "chat_" + chatId;

            int newCount = chatUserCount.computeIfPresent(userCountKey, (key, count) -> Math.max(0, count - 1));

            messagingTemplate.convertAndSend(
                    "/topic/chat/" + chatId + "/usercount",
                    newCount
            );
        });
    }

    public void registerUserSession(String sessionId, String username) {
        logger.info("Registering user '{}' with session ID {}", username, sessionId);
        sessionUserMap.put(sessionId, username);
    }

    private Optional<String> extractChatIdFromDestination(String destination) {
        if (destination != null && destination.startsWith("/topic/chat/")) {
            String[] parts = destination.split("/");
            if (parts.length >= 4) {
                return Optional.of(parts[3]);
            }
        }
        return Optional.empty();
    }
}