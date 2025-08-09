package com.example.aabhaa.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.fragment.app.FragmentActivity;

import com.example.aabhaa.controllers.AddressController;
import com.example.aabhaa.databinding.ActivityAddAddressBinding;


public class AddAddressActivity extends FragmentActivity {

    private ActivityAddAddressBinding binding;
    private AddressController addressController;

    public  boolean isEditMode = false;
    int addressId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddAddressBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Init controller with UI references
        addressController = new AddressController(this, getApplicationContext());
        addressController.setBinding(binding);  // Pass view to controller
        addressController.initSmallMap();
        Intent intent = getIntent();
        Log.d("AddressActivity", "Intent data - address_id: " + intent.getIntExtra("id", 0)
                + ", province: " + intent.getStringExtra("province")
                + ", district: " + intent.getStringExtra("district")
                + ", latitude: " + intent.getStringExtra("latitude")
                + ", longitude: " + intent.getStringExtra("longitude")
                + ", description: " + intent.getStringExtra("description"));



        if (intent != null) {
            addressId = intent.getIntExtra("id", -1);
            String title = intent.getStringExtra("title");
            String province = intent.getStringExtra("province");
            String district = intent.getStringExtra("district");
            double latitude = intent.getDoubleExtra("latitude", 0.0);
            double longitude = intent.getDoubleExtra("longitude", 0.0);
            String description = intent.getStringExtra("description");

            // Set values to views
            binding.etAddresstitle.setText(title);
            binding.acvCategoryProvince.setText(province);
            binding.acvCategoryDistrict.setText(district);
            binding.etLatitude.setText(String.valueOf(latitude));
            binding.etLongitude.setText(String.valueOf(longitude));
            binding.etDescription.setText(description);

            isEditMode = addressId != -1;

            if (isEditMode) {
                binding.btnSaveAddress.setText("Update Address");
                binding.headerTitle.setText("Update Address");
                // Populate fields (you already do this)
            } else {
                binding.btnSaveAddress.setText("Add Address");
                binding.headerTitle.setText("Add Address");
            }

        }




        // UI Logic: Expand Map
        binding.btnExpandMap.setOnClickListener(v -> {
            binding.fullscreenMapContainer.setVisibility(View.VISIBLE);
            addressController.loadFullMap(); // Just load it
        });


        // UI Logic: Close Map
        binding.btnCloseFullMap.setOnClickListener(v -> {
            binding.fullscreenMapContainer.setVisibility(View.GONE);
            addressController.removefullMapMarker();
        });

        binding.btnSaveAddress.setOnClickListener(v -> {

            addressController.handleSaveAddress(
                    addressId,
                    isEditMode,
                    binding.etAddresstitle,
                    binding.acvCategoryProvince,
                    binding.acvCategoryDistrict,
                    binding.etLatitude,
                    binding.etLongitude,
                    binding.etDescription
            );

        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        addressController.handlePermissionResult(requestCode, grantResults);
    }
}

