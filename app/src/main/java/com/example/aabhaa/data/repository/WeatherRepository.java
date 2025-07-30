package com.example.aabhaa.data.repository;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.aabhaa.auth.SharedPrefManager;
import com.example.aabhaa.models.ForecastDay;
import com.example.aabhaa.models.Weather;
import com.example.aabhaa.services.RetrofitClient;
import com.example.aabhaa.services.WeatherService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherRepository {

    private WeatherService weatherService;
    private SharedPrefManager sharedPrefManager;

    private final Context context;

    public WeatherRepository(Context context,WeatherService weatherService) {
        this.context = context.getApplicationContext(); // Avoid memory leaks
       this.weatherService = weatherService;
        this.sharedPrefManager = new SharedPrefManager(this.context); // Safe here
    }


    @SuppressLint("MissingPermission")
    public void getLocationAndFetchWeather() {
        // Check for location permission
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            Log.w("PermissionCheck", "Location permission not granted");
            return;
        }

        FusedLocationProviderClient fusedClient = LocationServices.getFusedLocationProviderClient(context);

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(500);
        locationRequest.setNumUpdates(1); // Only one update

        fusedClient.requestLocationUpdates(locationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    Log.w("Location", "Location callback returned null");
                    return;
                }
                android.location.Location location = locationResult.getLastLocation();
                if (location != null) {
                    double lat = location.getLatitude();
                    double lon = location.getLongitude();

                    sharedPrefManager.saveLocation(lat, lon);

                    weatherService.fetchWeatherFromAPI(lat, lon).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                Log.d("WeatherFetch", "Success");
                            } else {
                                Log.w("WeatherFetch", "Error: " + response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.e("WeatherFetch", "Failure: " + t.getMessage());
                        }
                    });
                } else {
                    Log.w("Location", "Location is null in callback");
                }
                // Remove updates after one location
                fusedClient.removeLocationUpdates(this);
            }
        }, null);
    }


    public LiveData<List<Weather>> getWeatherFromDB(double lat, double lon) {
        MutableLiveData<List<Weather>> weatherData = new MutableLiveData<>();

        weatherService.getWeatherFromDB(lat, lon).enqueue(new Callback<List<Weather>>() {
            @Override
            public void onResponse(Call<List<Weather>> call, Response<List<Weather>> response) {
//                Log.d("WeatherDebug", "Fetching weather from DB for lat: " + lat + ", lon: " + lon);

                if (response.isSuccessful() && response.body() != null) {
                    List<Weather> weatherList = response.body();
//                    Log.d("WeatherDebug", "Received weather entries: " + weatherList.size());

                    // 🧠 Log grouping result
                    List<ForecastDay> groupedDays = Weather.groupByDay(weatherList);
//                    Log.d("WeatherDebug", "Grouped Days Count: " + groupedDays.size());

                    for (ForecastDay day : groupedDays) {
//                        Log.d("WeatherDebug", "Day: " + day.getDate() + " | Entries: " + day.getWeatherList().size());
                    }

                    weatherData.setValue(weatherList);
                } else {
                    if (response.errorBody() != null) {
                        try {
                            Log.e("WeatherDebug", "Error Body: " + response.errorBody().string());
                        } catch (IOException e) {
                            Log.e("WeatherDebug", "Error reading error body", e);
                        }
                    }
                    Log.e("WeatherRepo", "Failed to load DB data and can't load view.");
                }
            }

            @Override
            public void onFailure(Call<List<Weather>> call, Throwable t) {
                Log.e("WeatherRepo", "DB call failed", t);
            }
        });

        return weatherData;
    }


    // ✅ Add this method just below the above one
    public LiveData<List<ForecastDay>> getGroupedForecastDays(double lat, double lon) {
        MutableLiveData<List<ForecastDay>> groupedLiveData = new MutableLiveData<>();

        getWeatherFromDB(lat, lon).observeForever(weatherList -> {
            if (weatherList != null && !weatherList.isEmpty()) {
                List<ForecastDay> grouped = Weather.groupByDay(weatherList);
                groupedLiveData.setValue(grouped.subList(0, Math.min(5, grouped.size()))); // only 5 days
            } else {
                groupedLiveData.setValue(new ArrayList<>());
            }
        });
        return groupedLiveData;
    }
    public SharedPrefManager getSharedPrefManager() {
        return sharedPrefManager; // Add this method inside `WeatherRepository`
    }


}
