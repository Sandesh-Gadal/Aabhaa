package com.example.aabhaa.data.repository;

import android.content.Context;
import android.util.Log;

import com.example.aabhaa.data.CropDao;
import com.example.aabhaa.data.CropDatabase;
import com.example.aabhaa.models.Crop;
import com.example.aabhaa.models.CropResponse;
import com.example.aabhaa.services.CropService;
import com.example.aabhaa.services.RetrofitClient;
import com.example.aabhaa.views.MainActivity;

import java.util.List;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CropRepository {

    private CropService cropService;

    private Context context;

    private CropDao cropDao;

    public CropRepository(Context context) {
        this.context = context;
        cropService = RetrofitClient.getClient(context).create(CropService.class);
        cropDao = CropDatabase.getInstance(context).cropDao();
    }

    // Fetch crops: return cached data first, then update from API
    public void getCropsBySeason(final CropCallback callback) {

        // 1️⃣ Load cached data from Room first
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Crop> cachedCrops = cropDao.getAllCrops();
            if (cachedCrops != null && !cachedCrops.isEmpty()) {
                // Return cached data on UI thread
                ((MainActivity)context).runOnUiThread(() -> callback.onCached(cachedCrops));
            }
        });

        // 2️⃣ Fetch from API and save to Room
        Call<CropResponse> call = cropService.getCrops();
        call.enqueue(new Callback<CropResponse>() {
            @Override
            public void onResponse(Call<CropResponse> call, Response<CropResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CropResponse cropResponse = response.body();

                    // Save API response to Room
                    Executors.newSingleThreadExecutor().execute(() -> {
                        cropDao.deleteAll(); // clear old data
                        cropDao.insertAll(cropResponse.getCrops()); // insert new data
                    });

                    callback.onSuccess(cropResponse);
                } else {
                    callback.onError("Failed to fetch crops from API");
                }
            }

            @Override
            public void onFailure(Call<CropResponse> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public interface CropCallback {
        void onSuccess(CropResponse cropResponse);
        void onCached(List<Crop> cachedCrops); // new method for offline data

        void onError(String errorMessage);
    }
}
