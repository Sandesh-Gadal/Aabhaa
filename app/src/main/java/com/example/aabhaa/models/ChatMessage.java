package com.example.aabhaa.models;

import java.io.Serializable;

public class ChatMessage implements Serializable {
    public enum SenderType { USER, EXPERT }

    public int id;
    public int senderId;
    public int receiverId;
    public String message;
    public String messageType;
    public boolean isRead;
    public String sentAt;
    public SenderType senderType;

    public ChatMessage(int senderId, int receiverId, String message, String messageType, boolean isRead, String sentAt, SenderType senderType) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.message = message;
        this.messageType = messageType;
        this.isRead = isRead;
        this.sentAt = sentAt;
        this.senderType = senderType;
    }

    // Getters and setters...
}

