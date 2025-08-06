package dto;

public class CreateChatRequest {
    private String name;
    private String description;
    private Long adminId;

    // Konstruktori
    public CreateChatRequest() {}

    // Getteri i setteri
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Long getAdminId() { return adminId; }
    public void setAdminId(Long adminId) { this.adminId = adminId; }
}
