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
        String accessToken = sharedPrefManager.getAccessToken();
        Log.d("TokenInterceptor", "Old Access Token: " + accessToken);

        Request originalRequest = chain.request();

        // Attach the access token to the original request
        Request requestWithToken = originalRequest.newBuilder()
                .header("Authorization", "Bearer " + accessToken)
                .build();

        Response response = chain.proceed(requestWithToken);

        if ((response.code() == 500)) {
            response.close(); // Close the old response to prevent leaks

            String refreshToken = sharedPrefManager.getRefreshToken();
            Log.d("TokenInterceptor", "Old Refresh Token: " + refreshToken);

            if (refreshToken == null || refreshToken.isEmpty()) {
                Log.e("TokenInterceptor", "No refresh token available.");
                throw new IOException("Refresh token missing. Please login again.");
            }

            // Attempt to refresh token
            try {
                Call<AuthResponse> refreshCall = authService.refresh(new RefreshRequest(refreshToken));
                retrofit2.Response<AuthResponse> refreshResponse = refreshCall.execute();

                if (refreshResponse.isSuccessful() && refreshResponse.body() != null) {
                    AuthResponse newTokens = refreshResponse.body();

                    Log.d("TokenInterceptor", "New Access Token: " + newTokens.getAccessToken());

                    // Save new tokens
                    sharedPrefManager.saveTokens(
                            newTokens.getAccessToken(),
                            newTokens.getRefreshToken()
                    );

                    // Retry original request with new token
                    Request newRequest = originalRequest.newBuilder()
                            .header("Authorization", "Bearer " + newTokens.getAccessToken())
                            .build();

                    return chain.proceed(newRequest);
                } else {
                    String errorBody = refreshResponse.errorBody() != null ?
                            refreshResponse.errorBody().string() : "No error body";
                    Log.e("TokenInterceptor", "Token refresh failed: " + errorBody);

                    sharedPrefManager.clearTokens();
                    throw new IOException("Token refresh failed. Please login again.");
                }

            } catch (Exception e) {
                Log.e("TokenInterceptor", "Exception during token refresh", e);
                sharedPrefManager.clearTokens();
                throw new IOException("Token refresh error: " + e.getMessage());
            }
        }

        return response;
    }
}
