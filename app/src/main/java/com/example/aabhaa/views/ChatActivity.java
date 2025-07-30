package com.example.aabhaa.views;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.aabhaa.adapters.ChatAdapter;
import com.example.aabhaa.data.StaticContentProvider;
import com.example.aabhaa.databinding.ActivityChatBinding;

public class ChatActivity extends AppCompatActivity {

    private ActivityChatBinding binding;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    binding.backButton.btnBack.setOnClickListener(v->{
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("navigate_to", "home");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    });

        ChatAdapter adapter = new ChatAdapter(StaticContentProvider.chatMessages);
        binding.rvChatMessages.setLayoutManager(new LinearLayoutManager(this));
        binding.rvChatMessages.setAdapter(adapter);

    }


}
