package com.group27.OnlyBuns.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = false)
    private Long userId; // ID korisnika koji je postavio sliku

    @Column(nullable = true, unique = false)
    private String description; // opis objave

    @Column(nullable = false, unique = false)
    private String imageUrl; // URL slike zeca

    @Column(nullable = false, unique = false)
    private double latitude; // geografska širina

    @Column(nullable = false, unique = false)
    private double longitude; // geografska dužina

    @Column(nullable = false, unique = false)
    private LocalDateTime createdAt; // vreme kreiranja objave

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
