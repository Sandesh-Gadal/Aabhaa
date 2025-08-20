package com.example.aabhaa.services;

import com.example.aabhaa.models.ApiResponse;
import com.example.aabhaa.models.CropResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CropService {
    @GET("crops/season")
    Call<CropResponse> getCrops();

}
