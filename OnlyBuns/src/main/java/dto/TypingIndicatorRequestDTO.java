package dto;

public class TypingIndicatorRequestDTO {
    private Long userId;
    private String username;
    private boolean isTyping;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public boolean isTyping() { return isTyping; }

    public void setIsTyping(boolean isTyping) { this.isTyping = isTyping; }
}