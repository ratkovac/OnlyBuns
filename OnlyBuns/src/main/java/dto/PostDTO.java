package dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.group27.OnlyBuns.model.Comment;
import com.group27.OnlyBuns.model.Post;

import java.util.List;

@JsonSerialize
@JsonInclude(JsonInclude.Include.NON_NULL)  // Ensure null values are excluded
public class PostDTO {
    private Long id;
    private Long userId;
    private String description;
    private String imageUrl;
    private long likeCount;
    private List<Comment> comments;

    // Default constructor for deserialization
    public PostDTO() {}

    // Constructor that initializes fields using a Post object
    public PostDTO(Post post, long likeCount, List<Comment> comments) {
        this.id = post.getId();
        this.userId = post.getUserId();
        this.description = post.getDescription();
        this.imageUrl = post.getImageUrl();
        this.likeCount = likeCount;
        this.comments = comments;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public long getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(long likeCount) {
        this.likeCount = likeCount;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
