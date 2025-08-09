package com.example.aabhaa.auth;

public class ChangePasswordRequest {
    private String current_password;
    private String new_password;
    private String new_password_confirmation;

    public ChangePasswordRequest(String currentPassword, String newPassword, String newPasswordConfirmation) {
        this.current_password = currentPassword;
        this.new_password = newPassword;
        this.new_password_confirmation = newPasswordConfirmation;
    }
    // getters and setters if needed
}
