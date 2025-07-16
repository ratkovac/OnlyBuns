package dto;

import com.group27.OnlyBuns.model.MemberRole;

import java.time.LocalDateTime;

public class ChatMemberDTO {
    private Long id;
    private Long userId;
    private String username;
    private String fullName;
    private Long chatId;
    private LocalDateTime joinedAt;
    private MemberRole role;
    private boolean isActive;

    // Konstruktori
    public ChatMemberDTO() {}

    // Getteri i setteri
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public Long getChatId() { return chatId; }
    public void setChatId(Long chatId) { this.chatId = chatId; }

    public LocalDateTime getJoinedAt() { return joinedAt; }
    public void setJoinedAt(LocalDateTime joinedAt) { this.joinedAt = joinedAt; }

    public MemberRole getRole() { return role; }
    public void setRole(MemberRole role) { this.role = role; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
}