// ChatAdapter.java
package com.example.aabhaa.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aabhaa.R;
import com.example.aabhaa.models.ChatMessage;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<ChatMessage> chatMessages;

    private static final int VIEW_TYPE_USER = 1;
    private static final int VIEW_TYPE_EXPERT = 2;

    private final int currentUserId; // pass admin/user ID when creating adapter


    public ChatAdapter(List<ChatMessage> chatMessages, int currentUserId) {
        this.chatMessages = chatMessages;
        this.currentUserId = currentUserId;
    }


    @Override
    public int getItemViewType(int position) {
        ChatMessage message = chatMessages.get(position);
        if (message.sender_id == currentUserId) {
            return VIEW_TYPE_USER; // message sent by admin/expert
        } else {
            return VIEW_TYPE_EXPERT;   // message sent by user
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessage message = chatMessages.get(position);
        if (holder instanceof UserMessageViewHolder) {
            ((UserMessageViewHolder) holder).bind(message.message);
        } else {
            ((ExpertMessageViewHolder) holder).bind(message.message);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_USER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_user, parent, false);
            return new UserMessageViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_expert, parent, false);
            return new ExpertMessageViewHolder(view);
        }
    }



    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    static class UserMessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageText;

        UserMessageViewHolder(View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.tvUserMessage);
        }

        void bind(String message) {
            messageText.setText(message);
        }
    }

    static class ExpertMessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageText;

        ExpertMessageViewHolder(View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.tvExpertMessage);
        }

        void bind(String message) {
            messageText.setText(message);
        }
    }
}
