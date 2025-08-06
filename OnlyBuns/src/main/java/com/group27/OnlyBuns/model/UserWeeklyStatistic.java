package com.group27.OnlyBuns.model;

import jakarta.persistence.*;

@Entity
public class UserWeeklyStatistic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private int likes;

    @Column(nullable = false)
    private int posts;

    @Column(nullable = false)
    private int followers;

    public UserWeeklyStatistic() {
        this.userId = 0L;
        this.likes = 0;
        this.followers = 0;
        this.posts = 0;
    }

    public UserWeeklyStatistic(Long userId, int likes, int posts, int followers) {
        this.userId = userId;
        this.likes = likes;
        this.posts = posts;
        this.followers = followers;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getPosts() {
        return posts;
    }

    public void setPosts(int posts) {
        this.posts = posts;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }
}
