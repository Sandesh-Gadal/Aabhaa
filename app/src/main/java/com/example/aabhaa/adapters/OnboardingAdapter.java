package com.example.aabhaa.adapters;





import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aabhaa.R;
import com.example.aabhaa.models.OnboardingItem;

import java.util.List;

public class OnboardingAdapter extends RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder> {

    private final List<OnboardingItem> items;

    public OnboardingAdapter(List<OnboardingItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public OnboardingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OnboardingViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_onboarding, parent, false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull OnboardingViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class OnboardingViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final TextView title, subtitle , titleNp , subtitleNp;

        public OnboardingViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgIllustration);
            title = itemView.findViewById(R.id.tvTitleEnglish);
            subtitle = itemView.findViewById(R.id.tvSubtitleNepali);
            titleNp = itemView.findViewById(R.id.tvTitleNepali);
            subtitleNp = itemView.findViewById(R.id.tvSubtitleNepali);
        }

        void bind(OnboardingItem item) {
            imageView.setImageResource(item.getImage());
            title.setText(item.getTitle());
            subtitle.setText(item.getSubtitle());
            titleNp.setText(item.getTitleNp());
            subtitleNp.setText(item.getSubtitleNp());
        }
    }
}
