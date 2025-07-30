package com.example.aabhaa.controllers;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bumptech.glide.Glide;
import com.example.aabhaa.R;
import com.example.aabhaa.auth.SharedPrefManager;
import com.example.aabhaa.data.repository.WeatherRepository;
import com.example.aabhaa.models.ForecastDay;
import com.example.aabhaa.models.Weather;
import com.example.aabhaa.services.RetrofitClient;
import com.example.aabhaa.services.WeatherService;

import java.util.ArrayList;
import java.util.List;

public class WeatherController  {

    private final WeatherRepository weatherRepository;


    public WeatherController(Context context) {

        // Create WeatherService using RetrofitClient with token (token auto included internally)
        WeatherService weatherService = RetrofitClient.getClient(context).create(WeatherService.class);
        this.weatherRepository = new WeatherRepository(context,weatherService);
    }

    public void fetchWeatherFromAPI() {
        weatherRepository.getLocationAndFetchWeather();
    }

    public LiveData<List<Weather>> getWeatherFromDB(double lat, double lon) {
        return weatherRepository.getWeatherFromDB(lat, lon);
    }

    public LiveData<List<ForecastDay>> getGroupedForecastDays(double lat, double lon) {
        // Transform Weather List → Grouped ForecastDay List
        MutableLiveData<List<ForecastDay>> groupedData = new MutableLiveData<>();

        getWeatherFromDB(lat, lon).observeForever(weatherList -> {
            if (weatherList != null && !weatherList.isEmpty()) {
                List<ForecastDay> grouped = Weather.groupByDay(weatherList);
                groupedData.postValue(grouped);
            } else {
                groupedData.postValue(new ArrayList<>());
            }
        });

        return groupedData;
    }

    public LiveData<List<Weather>> getWeatherForUser() {
        SharedPrefManager sharedPrefManager = weatherRepository.getSharedPrefManager();

        double lat = sharedPrefManager.getLatitude();  // Add getter method in SharedPrefManager
        double lon = sharedPrefManager.getLongitude(); // Add getter method in SharedPrefManager

        return weatherRepository.getWeatherFromDB(lat, lon);
    }

    // Get the latest weather data (first Weather object from DB)
    public LiveData<Weather> getLatestWeatherForUser() {
        MutableLiveData<Weather> latestWeather = new MutableLiveData<>();
        getWeatherForUser().observeForever(weatherList -> {
            if (weatherList != null && !weatherList.isEmpty()) {
                latestWeather.postValue(weatherList.get(0)); // First interval is latest
            } else {
                latestWeather.postValue(null);
            }
        });
        return latestWeather;
    }

    // Bind latest weather data to section_Weather_card views
    public void bindLatestWeather(Weather weather, TextView tvLocation, TextView tvTemperature,
                                  TextView tvCondition, TextView tvHumidity, TextView tvPrecipitation,
                                  TextView tvWindSpeed, ImageView weatherIcon, Context context) {
        if (weather == null) {
            tvCondition.setText("No data available");
            return;
        }

        tvLocation.setText(weather.getLocation() != null ? weather.getLocation() : "Unknown");
        tvTemperature.setText(String.format("%.0f°C", weather.getAvg_temp()));
        tvCondition.setText(String.format("%s, feels like %.0f°C",
                weather.getWeather_description() != null ? weather.getWeather_description() : "N/A",
                weather.getFeels_like()));
        tvHumidity.setText(String.format("%.0f%%", weather.getHumidity()));
        tvPrecipitation.setText(String.format("%.2f in", weather.getPrecipitation()));
        tvWindSpeed.setText(String.format("%.0f mph", weather.getWind_speed()));

        // Load weather icon using Glide
        String iconCode = weather.getWeather_icon();
        if (iconCode != null && !iconCode.isEmpty()) {
            String iconUrl = "https://openweathermap.org/img/wn/" + iconCode + "@4x.png";
            Glide.with(context)
                    .load(iconUrl)
                    .placeholder(R.drawable.ic_partly_sunny)
                    .error(R.drawable.ic_partly_sunny)
                    .into(weatherIcon);
        } else {
            weatherIcon.setImageResource(R.drawable.ic_partly_sunny);
        }
    }

}
