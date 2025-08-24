// Updated ContentAdapter.java to match your existing layout
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

import com.bumptech.glide.Glide;
import com.example.aabhaa.R;
import com.example.aabhaa.controllers.ExploreController;
import com.example.aabhaa.models.Content;
import com.example.aabhaa.views.ContentDetailsActivity;

import java.util.ArrayList;
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

        // Load image using Glide or fallback to drawable resource
        if (content.image_url != null && !content.image_url.isEmpty()) {
            try {
                // Check if it's a drawable resource ID (stored as string)
                int resId = Integer.parseInt(content.image_url);
                holder.imagePost.setImageResource(resId);
            } catch (NumberFormatException e) {
                // It's a URL, use Glide to load it
                Glide.with(context)
                        .load(content.image_url)
                        .placeholder(R.drawable.bg_wheat)
                        .error(R.drawable.bg_wheat)
                        .into(holder.imagePost);
            }
        } else {
            holder.imagePost.setImageResource(R.drawable.bg_wheat);
        }

        // Show play icon overlay based on content type
        String contentType = content.getContentType();
        if ("video".equalsIgnoreCase(contentType)) {
            holder.ivPlayIcon.setVisibility(View.VISIBLE);
            // Using your existing ic_camera drawable as play icon
            holder.ivPlayIcon.setImageResource(R.drawable.ic_camera);
        } else if ("audio".equalsIgnoreCase(contentType)) {
            holder.ivPlayIcon.setVisibility(View.VISIBLE);
            // You can change this to an audio icon if you have one
            holder.ivPlayIcon.setImageResource(R.drawable.ic_camera);
        } else {
            holder.ivPlayIcon.setVisibility(View.GONE);
        }

        // Handle click to open ContentDetailsActivity
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ContentDetailsActivity.class);
            intent.putExtra("content_item", content); // Pass the full object
            intent.putParcelableArrayListExtra("content_list", new ArrayList<>(contentList));
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

            // Overlay play icon
            ivPlayIcon = itemView.findViewById(R.id.ivPlayIcon);
        }
    }

    // Method to update the list and notify adapter
    public void updateContentList(List<Content> newContentList) {
        this.contentList.clear();
        this.contentList.addAll(newContentList);
        notifyDataSetChanged();
    }

    // Method to add more content (for pagination)
    public void addContent(List<Content> moreContent) {
        int startPosition = this.contentList.size();
        this.contentList.addAll(moreContent);
        notifyItemRangeInserted(startPosition, moreContent.size());
    }

    // Filter content by category
    public void filterByCategory(String category) {
        // This method can be called from the fragment when chips are clicked
        // You'll need to implement this based on your filtering requirements
    }
}