package com.group27.AdAgency.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public class AdPostDTO implements Serializable {
    private Long postId;
    private String description;
    private LocalDateTime publishTime;
    private String username;

    public AdPostDTO() {}

    public AdPostDTO(Long postId, String description, LocalDateTime publishTime, String username) {
        this.postId = postId;
        this.description = description;
        this.publishTime = publishTime;
        this.username = username;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(LocalDateTime publishTime) {
        this.publishTime = publishTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "AdPostDTO{" +
                "description='" + description + '\'' +
                ", publishTime=" + publishTime +
                ", username='" + username + '\'' +
                '}';
    }
}
