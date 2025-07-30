package com.example.aabhaa.auth;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.aabhaa.data.repository.AuthRepository;
import com.example.aabhaa.databinding.ActivityRegisterBinding;
import com.example.aabhaa.services.AuthService;
import com.example.aabhaa.services.RetrofitClient;

public class SharedPrefManager {
    private static final String PREF_NAME = "auth_prefs";
    private static final String KEY_ACCESS_TOKEN = "access_token";
    private static final String KEY_REFRESH_TOKEN = "refresh_token";

    private final SharedPreferences sharedPreferences;
    private static SharedPrefManager instance;

    public SharedPrefManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }


    // Call this once in Application class
    public static void init(Context context) {
        if (instance == null) {
            instance = new SharedPrefManager(context);
        }
    }

    public static SharedPrefManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("SharedPrefManager is not initialized. Call init(context) in Application class.");
        }
        return instance;
    }
    public void saveTokens(String accessToken, String refreshToken) {
        sharedPreferences.edit()
                .putString(KEY_ACCESS_TOKEN, accessToken)
                .putString(KEY_REFRESH_TOKEN, refreshToken)
                .apply();
    }

    public String getAccessToken() {
        return sharedPreferences.getString(KEY_ACCESS_TOKEN, null);
    }

    public String getRefreshToken() {
        return sharedPreferences.getString(KEY_REFRESH_TOKEN, null);
    }

    public void clearTokens() {
        sharedPreferences.edit().clear().apply();
    }

    public boolean isLoggedIn() {
        return getAccessToken() != null;
    }

    public void saveLocation(double lat, double lon) {
        sharedPreferences.edit()
                .putString("latitude", String.valueOf(lat))
                .putString("longitude", String.valueOf(lon))
                .apply();
    }

    public double getLatitude() {
        return Double.parseDouble(sharedPreferences.getString("latitude", "0.0"));
    }

    public double getLongitude() {
        return Double.parseDouble(sharedPreferences.getString("longitude", "0.0"));
    }
}



