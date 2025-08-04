package com.example.aabhaa.services;

import android.util.Log;

import com.example.aabhaa.auth.AuthResponse;
import com.example.aabhaa.auth.RefreshRequest;
import com.example.aabhaa.auth.SharedPrefManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;

public class TokenInterceptor implements Interceptor {

    private final SharedPrefManager sharedPrefManager;
    private final AuthService authService;

    public TokenInterceptor(SharedPrefManager sharedPrefManager, Retrofit refreshRetrofit) {
        this.sharedPrefManager = sharedPrefManager;
        this.authService = refreshRetrofit.create(AuthService.class);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        String path = originalRequest.url().encodedPath(); // e.g., /api/login or /api/register

//        Log.d("TokenInterceptor", "Intercepted path: " + path);

        // ✅ Skip token logic for login and register only
        if (path.equals("/api/login") || path.equals("/api/register")) {
//            Log.d("TokenInterceptor", "Skipping token for: " + path);
            return chain.proceed(originalRequest); // directly send without token
        }

        // Attach access token
        String accessToken = sharedPrefManager.getAccessToken();
        Request requestWithToken = originalRequest.newBuilder()
                .header("Authorization", "Bearer " + accessToken)
                .build();

        Response response = chain.proceed(requestWithToken);

        // Refresh logic if access token fails (e.g., 500 or 401)
        if (response.code() == 500) {
            response.close(); // prevent leaks

            String refreshToken = sharedPrefManager.getRefreshToken();
            if (refreshToken == null || refreshToken.isEmpty()) {
                sharedPrefManager.clearTokens();
                throw new IOException("Refresh token missing. Please login again.");
            }

            try {
                Call<AuthResponse> refreshCall = authService.refresh(new RefreshRequest(refreshToken));
                retrofit2.Response<AuthResponse> refreshResponse = refreshCall.execute();

                if (refreshResponse.isSuccessful() && refreshResponse.body() != null) {
                    AuthResponse newTokens = refreshResponse.body();

                    sharedPrefManager.saveTokens(newTokens.getAccessToken(), newTokens.getRefreshToken());

                    Request newRequest = originalRequest.newBuilder()
                            .header("Authorization", "Bearer " + newTokens.getAccessToken())
                            .build();

                    return chain.proceed(newRequest);
                } else {
                    sharedPrefManager.clearTokens();
                    throw new IOException("Token refresh failed. Please login again.");
                }

            } catch (Exception e) {
                sharedPrefManager.clearTokens();
                throw new IOException("Token refresh error: " + e.getMessage());
            }
        }

        return response;
    }


}
