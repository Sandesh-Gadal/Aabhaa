package com.example.aabhaa.models;

import java.util.HashMap;
import java.util.Map;

public class ChatMessage {
    public String firebase_id;   // push key
    public String room;          // e.g. "room_5"
    public int sender_id;
    public int receiver_id;
    public String message;
    public String message_type;
    public Boolean is_read;
    public long sent_at;

    // Optional enum for type
    public enum SenderType { USER, EXPERT }

    public ChatMessage() {} // Required for Firebase

    public ChatMessage(String firebase_id, String room, Object senderObj, Object receiverObj,
                       String message, String message_type, Object is_readObj, Object sentAtObj) {
        this.firebase_id = firebase_id;
        this.room = room;

        // Parse sender_id
        if (senderObj instanceof Long) this.sender_id = ((Long) senderObj).intValue();
        else if (senderObj instanceof String) this.sender_id = Integer.parseInt((String) senderObj);
        else this.sender_id = 0;

        // Parse receiver_id
        if (receiverObj instanceof Long) this.receiver_id = ((Long) receiverObj).intValue();
        else if (receiverObj instanceof String) this.receiver_id = Integer.parseInt((String) receiverObj);
        else this.receiver_id = 0;

        this.message = message;
        this.message_type = message_type;

        // Parse is_read
        if (is_readObj instanceof Boolean) this.is_read = (Boolean) is_readObj;
        else if (is_readObj instanceof Long) this.is_read = ((Long) is_readObj) != 0;
        else if (is_readObj instanceof String) this.is_read = !is_readObj.equals("0");
        else this.is_read = false;

        // Parse sent_at
        if (sentAtObj instanceof Long) this.sent_at = (Long) sentAtObj;
        else if (sentAtObj instanceof String) {
            try { this.sent_at = Long.parseLong((String) sentAtObj); }
            catch (NumberFormatException e) { this.sent_at = System.currentTimeMillis(); }
        } else {
            this.sent_at = System.currentTimeMillis();
        }
    }


    // Determine sender type dynamically
    public SenderType getSenderType(int currentUserId) {
        return sender_id == currentUserId ? SenderType.EXPERT : SenderType.USER;
    }

    // Optional: for Firebase deserialization if using setter
    public void setIs_read(Object value) {
        if (value instanceof Boolean) this.is_read = (Boolean) value;
        else if (value instanceof Long) this.is_read = ((Long) value) != 0;
        else this.is_read = false;
    }

    // Convert to map for Firebase write
    public Map<String, Object> toMap() {
        Map<String, Object> m = new HashMap<>();
        m.put("firebase_id", firebase_id);
        m.put("room", room);
        m.put("sender_id", sender_id);
        m.put("receiver_id", receiver_id);
        m.put("message", message);
        m.put("message_type", message_type);
        m.put("is_read", is_read != null && is_read ? 1 : 0); // store as 0/1
        m.put("sent_at", sent_at);
        return m;
    }
}
