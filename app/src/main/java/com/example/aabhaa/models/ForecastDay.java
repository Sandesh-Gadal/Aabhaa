package com.example.aabhaa.models;

import android.util.Log;

import java.util.List;

public class ForecastDay {
    private String date;
    private List<Weather> weatherList;

    public ForecastDay(String date, List<Weather> weatherList) {
        this.date = date;
        this.weatherList = weatherList;
    }

    public String getDate() {
        return date;
    }

    public List<Weather> getWeatherList() {
        return weatherList;
    }

    public String getDayName() {
        if (weatherList != null && !weatherList.isEmpty()) {
            return weatherList.get(0).getDayName(); // delegate to Weather
        }
        return "";
    }

    public List<Weather> getIntervals() {
        return weatherList;
    }



}
