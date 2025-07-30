package com.example.aabhaa.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aabhaa.R;
import com.example.aabhaa.models.ForecastDay;

import java.util.List;

public class ForecastDayAdapter extends RecyclerView.Adapter<ForecastDayAdapter.ForecastDayViewHolder> {

    private List<ForecastDay> forecastDays;

    public ForecastDayAdapter(List<ForecastDay> forecastDays) {
        this.forecastDays = forecastDays;
    }

    @NonNull
    @Override
    public ForecastDayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_forecast_day, parent, false);
        return new ForecastDayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastDayViewHolder holder, int position) {
        ForecastDay day = forecastDays.get(position);
        holder.tvDay.setText(day.getDayName());
        Log.d("WeatherDebug", "Binding day: " + day.getDate() + " with " + day.getIntervals().size() + " intervals");

        ForecastIntervalAdapter intervalAdapter = new ForecastIntervalAdapter(day.getIntervals());
        holder.rvIntervals.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        holder.rvIntervals.setAdapter(intervalAdapter);
    }

    @Override
    public int getItemCount() {
        Log.d("WeatherDebug", "ForecastDayAdapter item count: " + forecastDays.size());
        return forecastDays.size();
    }

    static class ForecastDayViewHolder extends RecyclerView.ViewHolder {
        TextView tvDay;
        RecyclerView rvIntervals;

        public ForecastDayViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDay = itemView.findViewById(R.id.tvDay);
            rvIntervals = itemView.findViewById(R.id.rvThreeHourIntervals);
        }
    }
}
