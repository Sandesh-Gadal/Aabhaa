package com.example.aabhaa.views;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.FragmentActivity;

import com.example.aabhaa.controllers.AddressController;
import com.example.aabhaa.databinding.ActivityAddressBinding;

public class AddressActivity extends FragmentActivity {

    private ActivityAddressBinding binding;
    private AddressController addressController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddressBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Init controller with UI references
        addressController = new AddressController(this);
        addressController.setBinding(binding);  // Pass view to controller
        addressController.initSmallMap();

        // UI Logic: Expand Map
        binding.btnExpandMap.setOnClickListener(v -> {
            binding.fullscreenMapContainer.setVisibility(View.VISIBLE);
            addressController.loadFullMap(); // Just load it
        });

        // UI Logic: Close Map
        binding.btnCloseFullMap.setOnClickListener(v -> {
            binding.fullscreenMapContainer.setVisibility(View.GONE);
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        addressController.handlePermissionResult(requestCode, grantResults);
    }
}

