package com.example.aabhaa.services;

import com.example.aabhaa.models.Soil;
import com.example.aabhaa.models.SoilResponse;
import com.google.gson.JsonObject;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SoilService {

    @POST("soil/formdata")
    @Headers("Content-Type: application/json")
    Call<ResponseBody> submitSoilData(@Body Soil soil, @Query("force_update") boolean forceUpdate);

    @GET("user/soildata")
    Call<SoilResponse> getUserSoilData();

    @DELETE("soil/delete/{id}")
    Call<Void> deleteSoilData(@Path("id") int id);

}
