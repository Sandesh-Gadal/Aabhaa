package com.example.aabhaa.services;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class WeatherQueryInterceptor implements Interceptor {
    private final double latitude;
    private final double longitude;

    public WeatherQueryInterceptor(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        HttpUrl originalUrl = original.url();

        // Add lat and lon only if the URL contains 'weather'
        if (originalUrl.encodedPath().contains("/weather/")) {
            HttpUrl newUrl = originalUrl.newBuilder()
                    .addQueryParameter("lat", String.valueOf(latitude))
                    .addQueryParameter("lon", String.valueOf(longitude))
                    .build();

            Request newRequest = original.newBuilder()
                    .url(newUrl)
                    .build();

            return chain.proceed(newRequest);
        }

        return chain.proceed(original);
    }
}
