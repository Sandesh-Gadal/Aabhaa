package com.example.aabhaa.models;

public class ApiResponse {
    private String status;   // e.g. "success" or "error"
    private String message;  // e.g. "Password updated successfully."

    // Optional: add other fields your API might return, like data

    public ApiResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    // Getter methods
    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    // Optionally, setter methods if you need them
}

