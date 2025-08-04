package com.example.aabhaa.utils;

import android.util.Base64;

import org.json.JSONObject;

public class JwtUtils {

    public static boolean isTokenExpired(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) return true;

            String payload = new String(Base64.decode(parts[1], Base64.URL_SAFE));
            JSONObject json = new JSONObject(payload);

            long exp = json.getLong("exp"); // expiration time in seconds
            long now = System.currentTimeMillis() / 1000;

            return exp < now;
        } catch (Exception e) {
            return true; // If decoding fails, treat as expired
        }
    }
}
