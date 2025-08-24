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
import com.example.aabhaa.models.Content;
import com.example.aabhaa.views.ContentDetailsActivity;

import java.util.ArrayList;
import java.util.List;

public class FarmingTipsAdapter extends RecyclerView.Adapter<FarmingTipsAdapter.AlertViewHolder> {

    private Context context;
    private List<Content> alertList;

    private ContentDetailsActivity contentDetailsActivity;

    public FarmingTipsAdapter(Context context, List<Content> alertList) {
        this.context = context;
        this.alertList = alertList;
        this.contentDetailsActivity = new ContentDetailsActivity();
    }

    @NonNull
    @Override
    public AlertViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_pf_post, parent, false);
        return new AlertViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlertViewHolder holder, int position) {
        Content content = alertList.get(position);

        holder.tvTitle.setText(content.title_en != null ? content.title_en : "No Title");
        holder.tvDescription.setText(content.description_en != null ? content.description_en : "");

        // Set arning icon (you can customize based on content type)

        // Load image using Glide or fallback to drawable resource
        if (content.image_url != null && !content.image_url.isEmpty()) {
            try {
                // Check if it's a drawable resource ID (stored as string)
                int resId = Integer.parseInt(content.image_url);
                holder.ivWarning.setImageResource(resId);
            } catch (NumberFormatException e) {
                // It's a URL, use Glide to load it
                Glide.with(context)
                        .load(content.image_url)
                        .placeholder(R.drawable.bg_wheat)
                        .error(R.drawable.bg_wheat)
                        .into(holder.ivWarning);
            }
        } else {
            holder.ivWarning.setImageResource(R.drawable.bg_wheat);
        }

        // Optional click listener
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ContentDetailsActivity.class);
            intent.putExtra("content_item", content); // Pass the full object
            intent.putParcelableArrayListExtra("content_list", new ArrayList<>(alertList));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return alertList.size();
    }

    public static class AlertViewHolder extends RecyclerView.ViewHolder {
        ImageView ivWarning;
        TextView tvTitle, tvDescription;

        public AlertViewHolder(@NonNull View itemView) {
            super(itemView);
            ivWarning = itemView.findViewById(R.id.ivWarning);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
//            tvPublished = itemView.findViewById(R.id.tvPublished);
        }
    }

    // Helper to update list
    public void setAlertList(List<Content> newList) {
        alertList.clear();
        alertList.addAll(newList);
        notifyDataSetChanged();
    }
}
