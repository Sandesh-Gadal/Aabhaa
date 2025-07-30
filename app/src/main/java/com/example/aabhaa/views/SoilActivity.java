package com.example.aabhaa.views;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aabhaa.databinding.ActivityAddressBinding;
import com.example.aabhaa.databinding.ActivitySoilDataBinding;

public class SoilActivity extends AppCompatActivity {

    private ActivitySoilDataBinding binding ;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySoilDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());




        binding.backButton.btnBack.setOnClickListener(v->{
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("navigate_to", "profile");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }


}
