package com.example.aabhaa.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.aabhaa.R;
import com.example.aabhaa.models.Weather;

import java.util.List;

public class ForecastIntervalAdapter extends RecyclerView.Adapter<ForecastIntervalAdapter.IntervalViewHolder> {

    private List<Weather> intervalList;

    public ForecastIntervalAdapter(List<Weather> intervalList) {
        this.intervalList = intervalList;
    }

    @NonNull
    @Override
    public IntervalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_forecast_interval, parent, false);
        return new IntervalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IntervalViewHolder holder, int position) {
        Weather weather = intervalList.get(position);

        holder.tvTime.setText(weather.getFormattedTime());
        holder.tvTemp.setText(String.format("%.0f°", weather.getAvg_temp()));



        // Load OpenWeatherMap icon using Glide
        String iconCode = weather.getWeather_icon(); // Assumes Weather class has getWeather_icon()
        if (iconCode != null && !iconCode.isEmpty()) {
            String iconUrl = "https://openweathermap.org/img/wn/" + iconCode + "@4x.png";
            Glide.with(holder.itemView.getContext())
                    .load(iconUrl)
                    .placeholder(R.drawable.ic_partly_sunny) // Add placeholder in res/drawable
                    .error(R.drawable.ic_partly_sunny) // Add error image in res/drawable
                    .into(holder.imgIcon);
        } else {
            holder.imgIcon.setImageResource(R.drawable.ic_partly_sunny); // Fallback image
        }
    }

    @Override
    public int getItemCount() {
        return intervalList.size();
    }

    static class IntervalViewHolder extends RecyclerView.ViewHolder {
        TextView tvTime, tvTemp;
        ImageView imgIcon;

        public IntervalViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvTemp = itemView.findViewById(R.id.tvTemp);
            imgIcon = itemView.findViewById(R.id.imgWeatherIcon);
        }
    }
}

