package com.example.aabhaa.views.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.aabhaa.R;
import com.example.aabhaa.adapters.ForecastDayAdapter;
import com.example.aabhaa.auth.SharedPrefManager;
import com.example.aabhaa.controllers.WeatherController;
import com.example.aabhaa.databinding.FragmentWeatherBinding;
import com.example.aabhaa.models.ForecastDay;
import com.example.aabhaa.models.Weather;

import java.util.List;

public class WeatherFragment extends Fragment {

    private FragmentWeatherBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentWeatherBinding.inflate(inflater, container, false);

        binding.rvForecastDays.setLayoutManager(new LinearLayoutManager(getContext()));

        WeatherController weatherController = new WeatherController(requireContext());


        weatherController.getWeatherForUser().observe(getViewLifecycleOwner(), weatherList -> {
            Log.d("WeatherDebug", "Observer triggered for getWeatherForUser()");

            if (weatherList == null) {
                Log.d("WeatherDebug", "weatherList is NULL");
            } else if (weatherList.isEmpty()) {
                Log.d("WeatherDebug", "weatherList is EMPTY");
            } else {
                Log.d("WeatherDebug", "weatherList received with size: " + weatherList.size());

                List<ForecastDay> grouped = Weather.groupByDay(weatherList);
                Log.d("WeatherDebug", "Grouped into ForecastDay size: " + grouped.size());

                ForecastDayAdapter adapter = new ForecastDayAdapter(grouped);

                binding.rvForecastDays.setAdapter(adapter);
                Log.d("WeatherDebug", "Adapter set to RecyclerView");
            }
        });

        weatherController.getLatestWeatherForUser().observe(getViewLifecycleOwner(), weather -> {
            weatherController.bindLatestWeather(
                    weather,
                    binding.weatherCard.tvLocation,
                    binding.weatherCard.tvTemperature,
                    binding.weatherCard.tvCondition,
                    binding.weatherCard.tvHumidity,
                    binding.weatherCard.tvPrecipitation,
                    binding.weatherCard.tvWindSpeed,
                    binding.weatherCard.weatherIcon,
                    requireContext()
            );
        });


        return binding.getRoot(); // Inflates fragment_weather.xml


    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // You can access views like: binding.tvForecastTitle, etc.
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Prevent memory leaks
    }
}
