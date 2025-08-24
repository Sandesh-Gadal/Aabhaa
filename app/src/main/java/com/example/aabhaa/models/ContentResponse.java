package com.example.aabhaa.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ContentResponse<T> {
    @SerializedName("success")
    public boolean success;

    @SerializedName("message")
    public String message;

    @SerializedName("data")
    public List<T> data;
    public int count;
}

