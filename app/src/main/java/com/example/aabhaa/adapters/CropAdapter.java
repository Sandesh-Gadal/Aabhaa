package com.example.aabhaa.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.aabhaa.R;
import com.example.aabhaa.models.Crop;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CropAdapter extends RecyclerView.Adapter<CropAdapter.CropViewHolder> {

    private Context context;
    private List<Crop> cropList;
    private OnCropClickListener listener;

    public interface OnCropClickListener {
        void onCropClick(Crop crop);
    }

    public CropAdapter(Context context, List<Crop> cropList, OnCropClickListener listener) {
        this.context = context;
        this.cropList = cropList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CropViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_crop_suggestion, parent, false);
        return new CropViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CropViewHolder holder, int position) {
        Crop crop = cropList.get(position);

        holder.tvSuggestionTitle.setText(crop.getName());

        // Load image
        Glide.with(context)
                .load(crop.getImage_url())
                .placeholder(R.drawable.bg_wheat)
                .into(holder.profileImage);

        // Handle click
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCropClick(crop);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cropList != null ? cropList.size() : 0;
    }

    public void updateList(List<Crop> newList) {
        this.cropList = newList;
        notifyDataSetChanged();
    }

    public static class CropViewHolder extends RecyclerView.ViewHolder {
        CircleImageView profileImage;
        TextView tvSuggestionTitle;

        public CropViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profile_image);
            tvSuggestionTitle = itemView.findViewById(R.id.tvSuggestionTitle);
        }
    }
}
