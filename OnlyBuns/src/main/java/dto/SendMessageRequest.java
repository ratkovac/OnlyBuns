package dto;

public class SendMessageRequest {
    private Long senderId;
    private String content;

    // Konstruktori
    public SendMessageRequest() {}

    // Getteri i setteri
    public Long getSenderId() { return senderId; }
    public void setSenderId(Long senderId) { this.senderId = senderId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
