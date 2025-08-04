package com.example.aabhaa.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aabhaa.controllers.AddressController;
import com.example.aabhaa.controllers.AddressFetchCallback;
import com.example.aabhaa.controllers.SoilController;
import com.example.aabhaa.databinding.ActivitySoilDataBinding;
import com.example.aabhaa.models.Address;

import java.util.List;

public class SoilActivity extends AppCompatActivity {

    private ActivitySoilDataBinding binding;
    private AddressController addressController;
    private SoilController soilController;
    private List<Address> addressList; // ✅ MISSING VARIABLE

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySoilDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        soilController = new SoilController(getApplicationContext()); // ✅ pass context properly
        addressController = new AddressController(this ,getApplicationContext());

        // ✅ Fetch and populate addresses with callback
        addressController.fetchAddressIntoSpinner(this, binding.spinnerAddress, new AddressFetchCallback() {
            @Override
            public void onAddressesFetched(List<Address> fetchedList) {
                addressList = fetchedList; // ✅ store into the class-level variable
            }
        });

        binding.backButton.btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("navigate_to", "profile");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        binding.btnSaveSoil.setOnClickListener(v -> {
            if (addressList == null || addressList.isEmpty()) {
                Toast.makeText(this, "Address list is empty. Please try again.", Toast.LENGTH_SHORT).show();
                return;
            }

            soilController.handleSaveSoilData(
                    this,
                    addressList,
                    binding.spinnerAddress,
                    binding.etNitrogen,
                    binding.etPhosphorus,
                    binding.etPotassium,
                    binding.etPh
            );
        });
    }
}
