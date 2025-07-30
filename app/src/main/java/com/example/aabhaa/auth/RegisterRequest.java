package com.example.aabhaa.auth;

public class RegisterRequest {
    private String full_name;
    private String email;
    private String password;
    private String password_confirmation;

    public RegisterRequest(String name, String email, String password, String password_confirmation) {
        this.full_name = name;
        this.email = email;
        this.password = password;
        this.password_confirmation = password_confirmation;
    }
}

