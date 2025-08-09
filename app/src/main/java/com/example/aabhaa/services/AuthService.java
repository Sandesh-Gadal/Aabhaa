package com.example.aabhaa.services;

import com.example.aabhaa.auth.AuthResponse;
import com.example.aabhaa.auth.ChangePasswordRequest;
import com.example.aabhaa.auth.LoginRequest;
import com.example.aabhaa.auth.RefreshRequest;
import com.example.aabhaa.auth.RegisterRequest;
import com.example.aabhaa.models.ApiResponse;
import com.example.aabhaa.models.Weather;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface AuthService {
    @POST("register")
    Call<AuthResponse> register(@Body RegisterRequest request);

    @POST("login")
    Call<AuthResponse> login(@Body LoginRequest request);

    @POST("refresh")
    Call<AuthResponse> refresh(@Body RefreshRequest refresh);

    @GET("user")
    Call<AuthResponse.User> getProfile(@Header("Authorization") String token);

    @POST("weather/fetch")
    Call<List<Weather>> getWeatherData();

    @POST("user/change-password")
    Call<ApiResponse> changePassword(@Body ChangePasswordRequest request);



}

