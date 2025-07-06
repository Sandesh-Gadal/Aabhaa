package com.example.aabhaa.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aabhaa.R;
import com.example.aabhaa.controllers.ExploreController;
import com.example.aabhaa.models.Content;
import com.example.aabhaa.views.ContentDetailsActivity;

import java.util.List;

public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ContentViewHolder> {

    private final Context context;
    private final List<Content> contentList;
    private final ExploreController exploreController;

    public ContentAdapter(Context context, List<Content> contentList, ExploreController exploreController) {
        this.context = context;
        this.contentList = contentList;
        this.exploreController = exploreController;
    }

    @NonNull
    @Override
    public ContentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_explore_post, parent, false);
        return new ContentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContentViewHolder holder, int position) {
        Content content = contentList.get(position);

        holder.tvPostTitle.setText(content.title_en);
        holder.tvPostDescription.setText(content.description_en);
        String formattedTime = exploreController.formatTimeAgo(content.published_at);
        holder.tvPostTime.setText(formattedTime);

        try {
            // Assuming local drawable image (if stored as string like "2131165321")
            int resId = Integer.parseInt(content.image_url);
            holder.imagePost.setImageResource(resId);
        } catch (NumberFormatException e) {
            // Use Glide or a placeholder for remote URLs or invalid entries
            holder.imagePost.setImageResource(R.drawable.bg_wheat); // fallback image
        }

        // Show a play icon overlay if it's a video content
        if ("video".equalsIgnoreCase(content.content_type)) {
            holder.ivPlayIcon.setVisibility(View.VISIBLE);
        } else {
            holder.ivPlayIcon.setVisibility(View.GONE);
        }

        // Handle click to open ContentDetailsActivity
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ContentDetailsActivity.class);
            intent.putExtra("content_item", content); // Pass the full object
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return contentList.size();
    }

    public static class ContentViewHolder extends RecyclerView.ViewHolder {
        ImageView imagePost, ivPlayIcon;
        TextView tvPostTitle, tvPostDescription, tvPostTime;

        public ContentViewHolder(@NonNull View itemView) {
            super(itemView);
            imagePost = itemView.findViewById(R.id.imagePost);
            tvPostTitle = itemView.findViewById(R.id.tvPostTitle);
            tvPostDescription = itemView.findViewById(R.id.tvPostDescription);
            tvPostTime = itemView.findViewById(R.id.tvPostTime);

            // Overlay play icon (you need to add this in item_explore_post.xml)
            ivPlayIcon = itemView.findViewById(R.id.ivPlayIcon);
        }
    }
}
