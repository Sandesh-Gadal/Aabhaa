package com.example.aabhaa.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.aabhaa.auth.SharedPrefManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class RetrofitClient {
    private static final String BASE_URL = "http://192.168.1.64:8000/api/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient(Context context) {
        if (retrofit == null) {
            SharedPrefManager prefManager = SharedPrefManager.getInstance();

            OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS);

            // Logging
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            clientBuilder.addInterceptor(logging);

            // Add TokenInterceptor for token refresh logic
            clientBuilder.addInterceptor(new TokenInterceptor(prefManager, getRefreshRetrofit()));

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(clientBuilder.build())
                    .build();
        }

        return retrofit;
    }

    // Use a separate retrofit for refresh token calls (no interceptor to avoid recursion)
    private static Retrofit getRefreshRetrofit() {
        Log.d("tokendebug" , "this is the refresh call to refrech token " );
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}

