package com.group27.OnlyBuns.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "verificationToken")
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long userId;

    @Column
    private String code;

    @Column
    private LocalDateTime dateOfRegistration;

    public VerificationToken() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getDateOfRegistration() {
        return dateOfRegistration;
    }

    public void setDateOfRegistration(LocalDateTime dateOfRegistration) {
        this.dateOfRegistration = dateOfRegistration;
    }
    public VerificationToken(Long userId, String code) {
        this.userId = userId;
        this.code = code;
        this.dateOfRegistration = LocalDateTime.now();
    }
}
