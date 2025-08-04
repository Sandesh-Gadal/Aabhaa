package com.example.aabhaa.views;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aabhaa.databinding.ActivitySoillistBinding;


public class SoilListActivity extends AppCompatActivity {

    private ActivitySoillistBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySoillistBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set header title if using a TextView with ID `headerTitle`
        binding.headerTitle.setText("Soil Data");

        binding.backButton.btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("navigate_to", "profile");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        binding.btnAddSoilData.setOnClickListener(v->{
            Intent intent = new Intent(getApplicationContext() , SoilActivity.class);
            startActivity(intent);
        });

        // TODO: Load soil data list, setup RecyclerView if needed
    }
}
