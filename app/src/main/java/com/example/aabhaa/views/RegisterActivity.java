package com.example.aabhaa.views;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aabhaa.R;
import com.example.aabhaa.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.btnRegister.setOnClickListener(view ->{
            Intent intent = new Intent(RegisterActivity.this , LoginActivity.class);
            startActivity(intent);
            finish();
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
