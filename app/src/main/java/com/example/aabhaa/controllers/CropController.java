package com.example.aabhaa.controllers;

import android.content.Context;
import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.example.aabhaa.adapters.CropAdapter;
import com.example.aabhaa.data.repository.CropRepository;
import com.example.aabhaa.models.Crop;
import com.example.aabhaa.models.CropResponse;

import java.util.ArrayList;
import java.util.List;

public class CropController {

    private Context context;
    private RecyclerView recyclerView;
    private CropAdapter cropAdapter;
    private CropRepository cropRepository;

    public CropController(Context context, RecyclerView recyclerView, CropRepository repository) {
        this.context = context;
        this.recyclerView = recyclerView;
        this.cropAdapter = new CropAdapter(context, new ArrayList<>());
        this.recyclerView.setAdapter(cropAdapter);
        this.cropRepository = repository;
    }

    public void fetchCropsBySeason() {
        cropRepository.getCropsBySeason( new CropRepository.CropCallback() {
            @Override
            public void onSuccess(CropResponse cropResponse) {
                List<Crop> crops = cropResponse.getCrops();
                cropAdapter.updateList(crops);
            }

            @Override
            public void onCached(List<Crop> cachedCrops) {
                // Show cached crops immediately
                cropAdapter.updateList(cachedCrops);
            }

            @Override
            public void onError(String errorMessage) {
                Log.e("CropController", errorMessage);
            }
        });
    }
}
