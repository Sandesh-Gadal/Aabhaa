package com.example.aabhaa.views;


import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aabhaa.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {


     private ActivityLoginBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.btnLogin.setOnClickListener(view ->{
            Intent intent = new Intent(LoginActivity.this , MainActivity.class);
            startActivity(intent);
            finish();
        });

        binding.forgotPassword.setOnClickListener(view ->{
            Intent intent = new Intent(LoginActivity.this , ForgotpasswordActivity.class);
            startActivity(intent);
        });

        binding.signUpLink.setOnClickListener((view->{
            Intent intent = new Intent(LoginActivity.this , RegisterActivity.class);
            startActivity(intent);
        }));





    }
}
