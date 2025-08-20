package com.example.aabhaa.models;

import java.util.List;

public class CropResponse {
    private String status;
    private List<Crop> crops;  // Matches "crops" field from API

    // Constructor
    public CropResponse(String status, List<Crop> crops) {
        this.status = status;
        this.crops = crops;
    }

    // Getters
    public String getStatus() {
        return status;
    }

    public List<Crop> getCrops() {
        return crops;
    }
}
