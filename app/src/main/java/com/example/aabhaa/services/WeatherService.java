package com.example.aabhaa.services;

import com.example.aabhaa.models.Weather;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherService {

    // 1. Call backend to fetch & store from OpenWeatherMap into DB
    @GET("weather/fetch")
    Call<ResponseBody> fetchWeatherFromAPI(
            @Query("lat") double lat,
            @Query("lon") double lon
    );

    // 2. Get the stored weather data from your own backend DB
    @GET("weather/data")
    Call<List<Weather>> getWeatherFromDB(
            @Query("lat") double latitude,
            @Query("lon") double longitude
    );
}
