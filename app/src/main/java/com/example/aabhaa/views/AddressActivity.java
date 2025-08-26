package com.example.aabhaa.views;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aabhaa.controllers.AddressController;
import com.example.aabhaa.databinding.ActivityAddressBinding;




public class AddressActivity extends AppCompatActivity {

    private ActivityAddressBinding binding;
    private AddressController addressController;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MyApplication.updateLocale(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddressBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set title text or other logic
        binding.headerTitle.setText("Add Address");

        addressController = new AddressController(this , getApplicationContext());

        addressController.populateAddressRecyclerView(this,binding.recyclerViewAddresses);


        binding.backButton.btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("navigate_to", "profile");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        binding.btnAddAddress.setOnClickListener(v->{
 Intent intent = new Intent(getApplicationContext() , AddAddressActivity.class);
            startActivity(intent);
        });

        // TODO: Load address data or initialize RecyclerView if you have one
    }
}

