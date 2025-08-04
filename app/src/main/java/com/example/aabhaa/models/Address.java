package com.example.aabhaa.models;

public class Address {
    private int id;
    private String title;
    private String province;
    private String district;
    private double latitude;
    private double longitude;
    private String description;

    // Constructor
    public Address(int id,String title, String province, String district, double latitude, double longitude, String description) {
        this.id = id;
        this.title = title;
        this.province = province;
        this.district = district;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
    }

    // Required no-args constructor for Gson
    public Address() {}

    public int getId() {
        return id;
    }

    // Add getters if needed by Retrofit
    public String getTitle() { return title; }
    public String getProvince() { return province; }
    public String getDistrict() { return district; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }

    public String getDescription() { return description; }

    public String getSpinnerAddress(){
        return title + " , " + district ;
    }
}
