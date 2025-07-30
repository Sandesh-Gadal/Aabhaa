package com.example.aabhaa.views;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aabhaa.controllers.LoginController;
import com.example.aabhaa.controllers.WeatherController;
import com.example.aabhaa.databinding.ActivityLoginBinding;
import com.example.aabhaa.services.RetrofitClient;
import com.example.aabhaa.services.WeatherService;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private LoginController loginController;

//    private WeatherController weatherController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        WeatherController weatherController = new WeatherController(getApplicationContext());
        LoginController loginController = new LoginController(this, binding);


        binding.btnLogin.setOnClickListener(view -> {
            String email = binding.etEmail.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();
            loginController.handleLogin(email, password);
        });

        binding.forgotPassword.setOnClickListener(view ->
                startActivity(new android.content.Intent(this, ForgotpasswordActivity.class))
        );

        binding.signUpLink.setOnClickListener(view ->
                startActivity(new android.content.Intent(this, RegisterActivity.class))
        );
    }
}
