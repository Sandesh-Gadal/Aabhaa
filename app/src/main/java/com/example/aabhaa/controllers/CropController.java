package com.example.aabhaa.controllers;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.example.aabhaa.adapters.CropAdapter;
import com.example.aabhaa.data.repository.CropRepository;
import com.example.aabhaa.models.Crop;
import com.example.aabhaa.models.CropResponse;
import com.example.aabhaa.views.CropDetailsActivity;

import java.util.ArrayList;
import java.util.List;

public class CropController {

    private RecyclerView recyclerView;
    private CropAdapter cropAdapter;
    private CropRepository cropRepository;



    // The listener is passed from the Activity/Fragment
    public CropController(RecyclerView recyclerView, CropRepository repository, CropAdapter.OnCropClickListener listener) {
        this.recyclerView = recyclerView;

        // Adapter uses the listener provided
        this.cropAdapter = new CropAdapter(recyclerView.getContext(), new ArrayList<>(), listener);
        this.recyclerView.setAdapter(cropAdapter);
        this.cropRepository = repository;
    }

    public void fetchCropsBySeason() {
        cropRepository.getCropsBySeason(new CropRepository.CropCallback() {
            @Override
            public void onSuccess(CropResponse cropResponse) {
                cropAdapter.updateList(cropResponse.getCrops());
            }

            @Override
            public void onCached(List<Crop> cachedCrops) {
                cropAdapter.updateList(cachedCrops);
            }

            @Override
            public void onError(String errorMessage) {
                Log.e("CropController", errorMessage);
            }
        });
    }
}
