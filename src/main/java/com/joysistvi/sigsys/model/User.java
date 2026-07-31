package com.joysistvi.sigsys.model;

import java.time.LocalDateTime; // latest class to map timestamp

public class User {
    private int userId;
    private String username;
    private String passwordHash;
    private String role; // Enum values: 'STUDENT', 'FACULTY', 'REGISTRAR', 'ADMIN'
    private String email;
    private boolean isActive;
    private LocalDateTime createdAt;

    public User(int userId, String username, String passwordHash, String role, String email, boolean isActive, LocalDateTime createdAt) {
        this.userId = userId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
        this.email = email;
        this.isActive = isActive;
        this.createdAt = createdAt;
    }

    public User(String username, String passwordHash, String role, String email, boolean isActive, LocalDateTime createdAt) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
        this.email = email;
        this.isActive = isActive;
        this.createdAt = createdAt;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
