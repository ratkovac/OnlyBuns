package dto;

public class TypingIndicatorDTO {
    private Long userId;
    private String username;
    private Long chatId;
    private boolean isTyping;

    public TypingIndicatorDTO() {}

    public TypingIndicatorDTO(Long userId, String username, Long chatId, boolean isTyping) {
        this.userId = userId;
        this.username = username;
        this.chatId = chatId;
        this.isTyping = isTyping;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public boolean getIsTyping() {
        return isTyping;
    }

    public void setTyping(boolean typing) {
        isTyping = typing;
    }

    @Override
    public String toString() {
        return "TypingIndicatorDTO{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", chatId=" + chatId +
                ", isTyping=" + isTyping +
                '}';
    }
}