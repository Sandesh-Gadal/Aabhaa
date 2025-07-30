package com.example.aabhaa.auth;

public class RefreshRequest {
    private String refresh_token;

    public RefreshRequest(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getRefreshToken() {
        return refresh_token;
    }
}
