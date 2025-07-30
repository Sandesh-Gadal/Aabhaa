package com.example.aabhaa.controllers;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.aabhaa.auth.AuthResponse;
import com.example.aabhaa.auth.LoginRequest;
import com.example.aabhaa.auth.SharedPrefManager;
import com.example.aabhaa.data.repository.AuthRepository;
import com.example.aabhaa.data.repository.WeatherRepository;
import com.example.aabhaa.databinding.ActivityLoginBinding;
import com.example.aabhaa.services.AuthService;
import com.example.aabhaa.services.RetrofitClient;
import com.example.aabhaa.services.WeatherService;
import com.example.aabhaa.views.MainActivity;

public class LoginController {

    private final Context context;
    private final ActivityLoginBinding binding;
    private final AuthRepository authRepository;
    private final SharedPrefManager sharedPrefManager;

//    private final WeatherController weatherController;


    public LoginController(Context context, ActivityLoginBinding binding) {
        this.context = context;
        this.binding = binding;
//        this.weatherController = weatherController;

        this.sharedPrefManager = new SharedPrefManager(context);
        AuthService apiService = RetrofitClient
                .getClient(context)
                .create(AuthService.class);
        this.authRepository = new AuthRepository(apiService);

    }

    public void handleLogin(String email, String password) {
        // Clear previous errors
        binding.emailLayout.setError(null);
        binding.passwordLayout.setError(null);

        boolean isValid = true;

        if (email.isEmpty()) {
            binding.emailLayout.setError("Email is required");
            isValid = false;
        }

        if (password.isEmpty()) {
            binding.passwordLayout.setError("Password is required");
            isValid = false;
        }

        if (!isValid) return;

        binding.progressBar.setVisibility(View.VISIBLE);

        LoginRequest request = new LoginRequest(email, password);
        authRepository.login(request, new AuthRepository.AuthCallback() {
            @Override
            public void onSuccess(AuthResponse response) {
                sharedPrefManager.saveTokens(response.access_token, response.refresh_token);
                binding.progressBar.setVisibility(View.GONE);
                context.startActivity(new Intent(context, MainActivity.class));


            }

            @Override
            public void onError(String message) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
        });
    }
}
