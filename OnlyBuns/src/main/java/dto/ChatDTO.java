package dto;

import java.time.LocalDateTime;
import java.util.List;

public class ChatDTO {
    private Long id;
    private String name;
    private String description;
    private Long adminId;
    private String adminUsername;
    private LocalDateTime createdAt;
    private boolean isActive;
    private List<ChatMemberDTO> members;
    private int memberCount;

    // Konstruktori
    public ChatDTO() {}

    // Getteri i setteri
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Long getAdminId() { return adminId; }
    public void setAdminId(Long adminId) { this.adminId = adminId; }

    public String getAdminUsername() { return adminUsername; }
    public void setAdminUsername(String adminUsername) { this.adminUsername = adminUsername; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public List<ChatMemberDTO> getMembers() { return members; }
    public void setMembers(List<ChatMemberDTO> members) { this.members = members; }

    public int getMemberCount() { return memberCount; }
    public void setMemberCount(int memberCount) { this.memberCount = memberCount; }
}
