package com.integradora.AssetTrackerUtez.security.dto;

public class AuthResponse {
    private String jwt;
    private Long userId;
    private String username;
    private long expiration;
    private String role;

    public AuthResponse(String jwt, Long userId, String username, String role, long expiration) {
        this.jwt = jwt;
        this.userId = userId;
        this.username = username;
        this.role = role;
        this.expiration = expiration;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getExpiration() {
        return expiration;
    }

    public void setExpiration(long expiration) {
        this.expiration = expiration;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
