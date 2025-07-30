package com.example.aabhaa.controllers;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.aabhaa.auth.AuthResponse;
import com.example.aabhaa.auth.RegisterRequest;
import com.example.aabhaa.auth.SharedPrefManager;
import com.example.aabhaa.data.repository.AuthRepository;
import com.example.aabhaa.databinding.ActivityRegisterBinding;
import com.example.aabhaa.services.AuthService;
import com.example.aabhaa.services.RetrofitClient;
import com.example.aabhaa.views.LoginActivity;

public class RegisterController {
    private final ActivityRegisterBinding binding;
    private final Context context;
    private final SharedPrefManager sharedPrefManager;
    private final AuthRepository authRepository;

    public RegisterController(Context context, ActivityRegisterBinding binding) {
        this.context = context;
        this.binding = binding;

        this.sharedPrefManager = new SharedPrefManager(context);
        AuthService apiService = RetrofitClient
                .getClient(null)
                .create(AuthService.class);
        this.authRepository = new AuthRepository(apiService);
    }

    public void handleRegister(String fullName, String email, String password, String confirmPassword) {
        // Clear previous errors
        binding.fullNameLayout.setError(null);
        binding.emailLayout.setError(null);
        binding.passwordLayout.setError(null);
        binding.confirmPasswordLayout.setError(null);

        boolean isValid = true;

        if (fullName.isEmpty()) {
            binding.fullNameLayout.setError("Full name is required");
            isValid = false;
        }
        if (email.isEmpty()) {
            binding.emailLayout.setError("Email is required");
            isValid = false;
        }
        if (password.isEmpty()) {
            binding.passwordLayout.setError("Password is required");
            isValid = false;
        }
        if (confirmPassword.isEmpty()) {
            binding.confirmPasswordLayout.setError("Confirm password is required");
            isValid = false;
        }
        if (!password.equals(confirmPassword)) {
            binding.confirmPasswordLayout.setError("Passwords do not match");
            isValid = false;
        }

        if (!isValid) return;

        binding.progressBar.setVisibility(android.view.View.VISIBLE);

        RegisterRequest request = new RegisterRequest(fullName, email, password, confirmPassword);

        authRepository.register(request, new AuthRepository.AuthCallback() {
            @Override
            public void onSuccess(AuthResponse response) {
                sharedPrefManager.saveTokens(response.access_token, response.refresh_token);
                binding.progressBar.setVisibility(android.view.View.GONE);
                context.startActivity(new Intent(context, LoginActivity.class));
            }

            @Override
            public void onError(String message) {
                binding.progressBar.setVisibility(android.view.View.GONE);
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
        });
    }
}
