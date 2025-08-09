package com.example.aabhaa.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class Soil {

    private int id;
    private int address_id;

    private float latitude;
    private float longitude;
    private double n_kg;
    private double p_kg;
    private double k_kg;
    private double ph;

    @Getter
    private List<Soil> data;

    @Getter
    @Setter
    @SerializedName("address_title")
    private String addressTitle;


    public Soil(int id, int address_id, float latitude, float longitude, double n_kg, double p_kg, double k_kg, double ph) {
        this.id = id;
        this.address_id = address_id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.n_kg = n_kg;
        this.p_kg = p_kg;
        this.k_kg = k_kg;
        this.ph = ph;
    }

    public Soil(List<Soil> data){
        this.data = data;
    }

    // Getters only (if using Retrofit with serialization)

    public int getId() {
        return id;
    }

    public int getAddress_id() {
        return address_id;
    }

    public double getN_kg() {
        return n_kg;
    }

    public double getP_kg() {
        return p_kg;
    }

    public double getK_kg() {
        return k_kg;
    }

    public double getPh() {
        return ph;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }



}


