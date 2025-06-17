package com.rafael.spring.security.postgresql.SpringBootSecurityPostgresqlApplication.payload.response;

public class CommentResponse {
    private String content;
    private String createdAt;
    private String username;

    public CommentResponse(String content, String createdAt, String username) {
        this.content = content;
        this.createdAt = createdAt;
        this.username = username;
    }

    // Getters
    public String getContent() {
        return content;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUsername() {
        return username;
    }
}