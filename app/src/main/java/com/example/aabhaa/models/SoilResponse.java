package com.example.aabhaa.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class SoilResponse {
    @SerializedName("user_id")
    private int userId;

    @SerializedName("count")
    private int count;

    @SerializedName("data")
    private List<Soil> data;

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public int getCount() {
        return count;
    }

    public List<Soil> getData() {
        return data;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setData(List<Soil> data) {
        this.data = data;
    }
}
