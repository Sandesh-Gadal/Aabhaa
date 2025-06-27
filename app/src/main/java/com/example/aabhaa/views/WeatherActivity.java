package com.example.aabhaa.views;


import android.os.Bundle;

import com.example.aabhaa.R;

import com.example.aabhaa.databinding.ActivityWeatherBinding;

public class WeatherActivity extends BaseActivity<ActivityWeatherBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityWeatherBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize the BottomNavigationView with current selected item

        setupBottomNav(binding.bottomNavigationView, R.id.nav_weather);



    }
}