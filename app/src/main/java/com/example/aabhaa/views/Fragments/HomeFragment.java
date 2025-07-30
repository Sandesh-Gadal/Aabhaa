package com.example.aabhaa.views.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.example.aabhaa.controllers.MainController;
import com.example.aabhaa.controllers.WeatherController;
import com.example.aabhaa.databinding.FragmentHomeBinding;
import com.example.aabhaa.views.NotificationActivity;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        WeatherController weatherController = new WeatherController(requireContext());


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
        return binding.getRoot(); // Inflates activity_home.xml
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.includeHeader.notificationContainer.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), NotificationActivity.class);
            startActivity(intent);
        });

        // No BottomNavigationView here
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Prevent memory leaks
    }
}
