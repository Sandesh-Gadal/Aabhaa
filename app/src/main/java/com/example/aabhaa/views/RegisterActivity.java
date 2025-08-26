package com.example.aabhaa.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aabhaa.R;
import com.example.aabhaa.controllers.RegisterController;
import com.example.aabhaa.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private RegisterController registerController;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MyApplication.updateLocale(newBase));
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        registerController = new RegisterController(this, binding);



        binding.btnRegister.setOnClickListener(view ->{
            String fullName = binding.etFullName.getText().toString().trim();
            String email = binding.etEmail.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();
            String confirmPassword = binding.etConfirmPassword.getText().toString().trim();

            registerController.handleRegister(fullName, email, password, confirmPassword);
        });

        binding.tvSignIn.setOnClickListener(v->{
            Intent intent = new Intent(RegisterActivity.this , LoginActivity.class);
            startActivity(intent);
            finish();
        });


        binding.btnBack.btnBack.setOnClickListener(v->{
            Intent intent = new Intent(RegisterActivity.this , LoginActivity.class);
            startActivity(intent);
            finish();
        });

    }
}
