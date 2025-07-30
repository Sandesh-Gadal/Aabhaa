package com.example.aabhaa.auth;

public class AuthResponse {
    public boolean status;
    public String message;
    public String access_token;
    public String refresh_token;
    public User user;


    public String getAccessToken() {
        return access_token;
    }

    public String getRefreshToken() {
        return refresh_token;
    }
    public static class User {
        public int id;
        public String full_name;
        public String email;
    }
}

