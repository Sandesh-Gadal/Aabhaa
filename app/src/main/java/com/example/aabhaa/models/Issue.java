package com.example.aabhaa.models;

public class Issue {
    private String title;
    private String category;
    private String message;
    private String image_url; // Optional if you handle uploads
    private String status;    // default: "open"
    private String priority;  // optional: "low", "medium", "high"

    public Issue(String title, String category, String message, String image_url, String status, String priority) {
        this.title = title;
        this.category = category;
        this.message = message;
        this.image_url = image_url;
        this.status = status;
        this.priority = priority;
    }

    // Getters and setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getImage_url() { return image_url; }
    public void setImage_url(String image_url) { this.image_url = image_url; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
}
