package com.example.aabhaa.auth;

import lombok.Getter;
import lombok.Setter;

public class ChangePasswordRequest {
    @Getter
    @Setter
    private String current_password;

    @Getter
    @Setter
    private String new_password;
    @Getter
    @Setter
    private String new_password_confirmation;

    public ChangePasswordRequest(String currentPassword, String newPassword, String newPasswordConfirmation) {
        this.current_password = currentPassword;
        this.new_password = newPassword;
        this.new_password_confirmation = newPasswordConfirmation;
    }
    // getters and setters if needed
}
