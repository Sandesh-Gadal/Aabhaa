package com.example.aabhaa.controllers;

import com.example.aabhaa.models.Soil;
import com.example.aabhaa.models.SoilResponse;

import java.util.List;

public interface SoilFetchCallback {


   void onSoilFetched(List<Soil> soilList);
}
