package com.example.aabhaa.adapters;

import android.content.Context;
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

import java.util.List;

public class SuggestionAdapter extends RecyclerView.Adapter<SuggestionAdapter.SuggestionViewHolder> {

    private final Context context;
    private final List<Content> suggestions;
    private final View.OnClickListener itemClickListener;
    private ContentDetailsActivity contentDetailsActivity;

    public SuggestionAdapter(Context context, List<Content> suggestions, View.OnClickListener itemClickListener) {
        this.context = context;
        this.suggestions = suggestions;
        this.itemClickListener = itemClickListener;
        this.contentDetailsActivity = new ContentDetailsActivity();
    }

    @NonNull
    @Override
    public SuggestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post_suggestion, parent, false);
        return new SuggestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SuggestionViewHolder holder, int position) {
        Content content = suggestions.get(position);
        holder.tvSuggestionTitle.setText(content.title_en);
        holder.tvDate.setText(contentDetailsActivity.formatDateTime(content.published_at));

        try {
            int resId = Integer.parseInt(content.image_url);
            holder.imageSuggestion.setImageResource(resId);
        } catch (NumberFormatException e) {
            Glide.with(context)
                    .load(content.image_url)
                    .placeholder(R.drawable.bg_otp_box_active)
                    .into(holder.imageSuggestion);
        }

        holder.itemView.setTag(content); // Pass content via tag
        holder.itemView.setOnClickListener(itemClickListener);
    }

    @Override
    public int getItemCount() {
        return suggestions.size();
    }

    public static class SuggestionViewHolder extends RecyclerView.ViewHolder {
        ImageView imageSuggestion;
        TextView tvSuggestionTitle, tvDate;

        public SuggestionViewHolder(@NonNull View itemView) {
            super(itemView);
            imageSuggestion = itemView.findViewById(R.id.imageSuggestion);
            tvSuggestionTitle = itemView.findViewById(R.id.tvSuggestionTitle);
            tvDate = itemView.findViewById(R.id.tvDate);
        }
    }
}

