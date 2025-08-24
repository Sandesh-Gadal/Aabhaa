package com.example.aabhaa.data.repository;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.example.aabhaa.data.CropDao;
import com.example.aabhaa.data.CropDatabase;
import com.example.aabhaa.models.Crop;
import com.example.aabhaa.models.CropResponse;
import com.example.aabhaa.services.CropService;
import com.example.aabhaa.services.RetrofitClient;

import java.util.List;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CropRepository {

    private CropService cropService;
    private CropDao cropDao;
    private Handler mainHandler = new Handler(Looper.getMainLooper());

    public CropRepository(Context context) {
        cropService = RetrofitClient.getClient(context).create(CropService.class);
        cropDao = CropDatabase.getInstance(context).cropDao();
    }

    public void getCropsBySeason(final CropCallback callback) {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Crop> cachedCrops = cropDao.getAllCrops();
            if (cachedCrops != null && !cachedCrops.isEmpty()) {
                // Post cached data to UI thread safely
                mainHandler.post(() -> callback.onCached(cachedCrops));
            }
        });

        Call<CropResponse> call = cropService.getCrops();
        call.enqueue(new Callback<CropResponse>() {
            @Override
            public void onResponse(Call<CropResponse> call, Response<CropResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CropResponse cropResponse = response.body();

                    Executors.newSingleThreadExecutor().execute(() -> {
                        cropDao.deleteAll();
                        cropDao.insertAll(cropResponse.getCrops());
                    });

                    mainHandler.post(() -> callback.onSuccess(cropResponse));
                } else {
                    mainHandler.post(() -> callback.onError("Failed to fetch crops from API"));
                }
            }

            @Override
            public void onFailure(Call<CropResponse> call, Throwable t) {
                mainHandler.post(() -> callback.onError(t.getMessage()));
            }
        });
    }

    public interface CropCallback {
        void onSuccess(CropResponse cropResponse);
        void onCached(List<Crop> cachedCrops);
        void onError(String errorMessage);
    }
}
