package com.example.aabhaa.views;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aabhaa.R;
import com.example.aabhaa.databinding.ActivityProfileBinding;

public class ProfileActivity extends AppCompatActivity {

    ActivityProfileBinding binding;
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


    }

}
