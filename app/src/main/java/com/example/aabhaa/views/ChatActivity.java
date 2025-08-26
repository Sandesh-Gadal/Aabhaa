package com.example.aabhaa.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.aabhaa.adapters.ChatAdapter;
import com.example.aabhaa.auth.SharedPrefManager;
import com.example.aabhaa.databinding.ActivityChatBinding;

import com.example.aabhaa.models.ChatMessage;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;


public class ChatActivity extends AppCompatActivity {

    private ActivityChatBinding binding;
    private ChatAdapter adapter;
    private final List<com.example.aabhaa.models.ChatMessage> messages = new ArrayList<>();

    // IDs
    private static final int ADMIN_ID = 5;
    private int currentUserId ; // TODO: set from login/session


    // Firebase
    private DatabaseReference roomAdminRef; // room_5 (admin room)
    private DatabaseReference roomUserRef;  // room_2 (compat mode)


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MyApplication.updateLocale(newBase));
    }


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        currentUserId = SharedPrefManager.getInstance().getUserId();

    binding.backButton.btnBack.setOnClickListener(v->{
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("navigate_to", "home");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    });

        // RecyclerView
        // RecyclerView
        adapter = new ChatAdapter(messages, currentUserId);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setStackFromEnd(true);
        binding.rvChatMessages.setLayoutManager(lm);
        binding.rvChatMessages.setAdapter(adapter);


        // Firebase
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        roomAdminRef = db.getReference("chats").child("room_" + ADMIN_ID);   // where user -> admin writes
        roomUserRef  = db.getReference("chats").child("room_" + currentUserId); // where admin -> user writes (your current backend)

        // Listen to both rooms (compat with current backend)
        ChildEventListener listener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
                Map<String, Object> map = (Map<String, Object>) snapshot.getValue();
                ChatMessage m = new ChatMessage(
                        (String) map.get("firebase_id"),
                        (String) map.get("room"),
                        map.get("sender_id"),
                        map.get("receiver_id"),
                        (String) map.get("message"),
                        (String) map.get("message_type"),
                        map.get("is_read"),
                        map.get("sent_at")
                );


                // Only show messages between these two users
                boolean belongsToThisConversation =
                        (m.sender_id == currentUserId && m.receiver_id == ADMIN_ID) ||
                                (m.sender_id == ADMIN_ID && m.receiver_id == currentUserId);
                if (!belongsToThisConversation) return;

                messages.add(m);

                // Keep messages in time order
                Collections.sort(messages, Comparator.comparingLong(mm -> mm.sent_at));
                adapter.notifyDataSetChanged();
                binding.rvChatMessages.scrollToPosition(messages.size() - 1);
            }


            @Override public void onChildChanged(DataSnapshot snapshot, String previousChildName) {}
            @Override public void onChildRemoved(DataSnapshot snapshot) {}
            @Override public void onChildMoved(DataSnapshot snapshot, String previousChildName) {}
            @Override public void onCancelled(DatabaseError error) {}
        };

        // Order by sent_at so Firebase delivers in time order
        roomAdminRef.orderByChild("sent_at").addChildEventListener(listener);
        roomUserRef.orderByChild("sent_at").addChildEventListener(listener);

        // Send button
        binding.btnSend.setOnClickListener(v -> sendMessage());
    }


    private void sendMessage() {
        String text = String.valueOf(binding.etMessage.getText()).trim();
        if (text.isEmpty()) return;

        long now = System.currentTimeMillis();

        // Write to admin room so admin sees it in room_5
        DatabaseReference newRef = roomAdminRef.push();
        String key = newRef.getKey();

        com.example.aabhaa.models.ChatMessage msg = new com.example.aabhaa.models.ChatMessage();
        msg.firebase_id = key;
        msg.room = "room_" + ADMIN_ID;
        msg.sender_id = currentUserId;     // user
        msg.receiver_id = ADMIN_ID;        // admin
        msg.message = text;
        msg.message_type = "text";
        msg.is_read = false;
        msg.sent_at = now;

        newRef.setValue(msg);
        binding.etMessage.setText("");
    }



}
