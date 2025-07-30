package com.example.aabhaa.data.repository;



import android.util.Log;

import com.example.aabhaa.auth.AuthResponse;
import com.example.aabhaa.auth.LoginRequest;
import com.example.aabhaa.auth.RefreshRequest;
import com.example.aabhaa.auth.RegisterRequest;
import com.example.aabhaa.services.AuthService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {
    private final AuthService authService;

    public AuthRepository(AuthService authService) {
        this.authService = authService;
    }

    public void login(LoginRequest request, AuthCallback callback) {
        authService.login(request).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Login failed");
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public void register(RegisterRequest request, AuthCallback callback) {
        authService.register(request).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    try {
                        // Read error body from Laravel
                        String errorBody = response.errorBody().string();
                        Log.e("RegisterError", "Server Response: " + errorBody);
                        callback.onError(errorBody); // or parse JSON for clean message
                    } catch (Exception e) {
                        callback.onError("Registration failed. Unable to parse error.");
                    }
                }
            }


            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public void refreshToken(RefreshRequest request, AuthCallback callback) {
        authService.refresh(request).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Token refresh failed");
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public interface AuthCallback {
        void onSuccess(AuthResponse response);
        void onError(String message);
    }
}
