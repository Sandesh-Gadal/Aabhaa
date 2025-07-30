package com.example.aabhaa.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import lombok.Getter;

public class Weather {
    private double latitude;
    private double longitude;
    private String province;
    private String district;
    @Getter
    private String date;


    private double min_temp;
    private double max_temp;
    @Getter
    private double avg_temp;
    private double feels_like;

    private int humidity;
    private int pressure;

    private double wind_speed;
    private int wind_deg;
    private double wind_gust;

    private String weather_main;
    private String weather_description;
    @Getter
    private String weather_icon;

    private int cloudiness;
    private double pop;
    private double rain;
    private double snow;

    public Weather(
            String date, double avg_temp, double feels_like, double min_temp, double max_temp,
            int humidity, int pressure, double wind_speed, int wind_deg, Double wind_gust,
            String weather_main, String weather_description, String weather_icon,
            int cloudiness, double pop, Double rain, Double snow
    ) {
        this.date = date;
        this.avg_temp = avg_temp;
        this.feels_like = feels_like;
        this.min_temp = min_temp;
        this.max_temp = max_temp;
        this.humidity = humidity;
        this.pressure = pressure;
        this.wind_speed = wind_speed;
        this.wind_deg = wind_deg;
        this.wind_gust = wind_gust;
        this.weather_main = weather_main;
        this.weather_description = weather_description;
        this.weather_icon = weather_icon;
        this.cloudiness = cloudiness;
        this.pop = pop;
        this.rain = rain;
        this.snow = snow;
    }
    // Optional helper method to convert datetime string to day name (e.g., "Monday")
    public String getDayName() {
        try {
            java.text.SimpleDateFormat inputFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            java.util.Date date = inputFormat.parse(this.date);
            java.text.SimpleDateFormat outputFormat = new java.text.SimpleDateFormat("EEEE"); // Full day name
            return outputFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    // Getters
    public String getLocation() {
        // Combine district and province, or use one if the other is null
        if (district != null && province != null) {
            return district + ", " + province;
        } else if (district != null) {
            return district;
        } else if (province != null) {
            return province;
        } else {
            return "Unknown";
        }
    }

    public double getAvg_temp() {
        return avg_temp;
    }

    public double getFeels_like() {
        return feels_like;
    }

    public String getWeather_description() {
        return weather_description;
    }

    public String getWeather_icon() {
        return weather_icon;
    }

    public double getHumidity() {
        return humidity;
    }

    public double getPrecipitation() {
        // Sum rain and snow, convert from mm to inches (1 mm = 0.0393701 inches)
        return (rain + snow) * 0.0393701;
    }

    public double getWind_speed() {
        return wind_speed;
    }



    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }


    // Optional: Extract just time, like "3 PM"
    public String getFormattedTime() {
        try {
            java.text.SimpleDateFormat inputFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            java.util.Date date = inputFormat.parse(this.date);
            java.text.SimpleDateFormat outputFormat = new java.text.SimpleDateFormat("h a");
            return outputFormat.format(date);
        } catch (Exception e) {
            return "";
        }
    }

    // ✅ Add this static method here
    public static List<ForecastDay> groupByDay(List<Weather> weatherList) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Map<String, List<Weather>> groupedMap = new LinkedHashMap<>();

        for (Weather w : weatherList) {
            try {
                Date date = inputFormat.parse(w.getDate()); // example: "2025-07-29 12:00:00"
                String dayKey = dayFormat.format(date); // "2025-07-29"

                groupedMap.computeIfAbsent(dayKey, k -> new ArrayList<>()).add(w);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        List<ForecastDay> forecastDays = new ArrayList<>();
        for (Map.Entry<String, List<Weather>> entry : groupedMap.entrySet()) {
            forecastDays.add(new ForecastDay(entry.getKey(), entry.getValue()));
        }

        return forecastDays.subList(0, Math.min(7, forecastDays.size())); // Limit to 5 days
    }


}

