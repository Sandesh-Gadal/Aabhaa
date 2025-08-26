package com.example.aabhaa.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aabhaa.R;
import com.example.aabhaa.controllers.SoilController;
import com.example.aabhaa.databinding.ActivitySoillistBinding;


public class SoilListActivity extends AppCompatActivity {

    private ActivitySoillistBinding binding;
    private SoilController soilController;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MyApplication.updateLocale(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySoillistBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        this.soilController = new SoilController(this);

        // Set header title if using a TextView with ID `headerTitle`
//        binding.headerTitle.setText("Soil Data");

        TextView headerTitle = findViewById(R.id.headerTitle);
        Log.d("LANG", "Header text after setContentView: " + headerTitle.getText());

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

        soilController.populateSoilRecyclerView(this, this.binding.recyclerViewSoil);

        // TODO: Load soil data list, setup RecyclerView if neede

    }
}
