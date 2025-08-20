package com.example.aabhaa.data.repository;

import android.content.Context;
import android.util.Log;

import com.example.aabhaa.models.CropResponse;
import com.example.aabhaa.services.CropService;
import com.example.aabhaa.services.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CropRepository {

    private CropService cropService;

    private Context context;

    public CropRepository(Context context) {
        this.context = context;
        cropService = RetrofitClient.getClient(context).create(CropService.class);
    }

    public void getCropsBySeason( final CropCallback callback) {
        Call<CropResponse> call = cropService.getCrops(); // pass season param
        call.enqueue(new Callback<CropResponse>() {
            @Override
            public void onResponse(Call<CropResponse> call, Response<CropResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Failed to fetch crops");
                }
            }

            @Override
            public void onFailure(Call<CropResponse> call, Throwable t) {
                Log.e("CropRepository", "Error: " + t.getMessage());
                callback.onError(t.getMessage());
            }
        });
    }

    public interface CropCallback {
        void onSuccess(CropResponse cropResponse);
        void onError(String errorMessage);
    }
}
