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

    public boolean isEditMode = false;

    int soilId;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MyApplication.updateLocale(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySoilDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        soilController = new SoilController(getApplicationContext()); // ✅ pass context properly
        addressController = new AddressController(this, getApplicationContext());

        // ✅ Fetch and populate addresses with callback
        addressController.fetchAddressIntoSpinner(this, binding.spinnerAddress, new AddressFetchCallback() {
            @Override
            public void onAddressesFetched(List<Address> fetchedList) {
                addressList = fetchedList; // ✅ store into the class-level variable
            }
        });

        Intent intent = getIntent();

        if (intent != null) {
            // Get extras from intent
            soilId = intent.getIntExtra("id", -1); // Soil record ID
            int addressId = intent.getIntExtra("address_id", -1);
            double nitrogen = intent.getDoubleExtra("N", 0.0);
            double phosphorus = intent.getDoubleExtra("P", 0.0);
            double potassium = intent.getDoubleExtra("K", 0.0);
            double phLevel = intent.getDoubleExtra("pH", 0.0);


            // Prefill fields
            selectAddressWhenupdate(addressId);
            binding.etNitrogen.setText(String.valueOf(nitrogen));
            binding.etPhosphorus.setText(String.valueOf(phosphorus));
            binding.etPotassium.setText(String.valueOf(potassium));
            binding.etPh.setText(String.valueOf(phLevel));

            // Detect edit mode
            isEditMode = soilId != -1;

            if (isEditMode) {
                binding.btnSaveSoil.setText("Update Soil Data");
                binding.headerTitle.setText("Update Soil Data");
            } else {
                binding.btnSaveSoil.setText("Add Soil Data");
                binding.headerTitle.setText("Add Soil Data");
            }
        }

        binding.backButton.btnBack.setOnClickListener(v -> {
            Intent intent1 = new Intent(this, MainActivity.class);
            intent1.putExtra("navigate_to", "profile");
            intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent1);
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


    public void selectAddressWhenupdate(int addressId) {
        // ✅ Fetch and populate addresses with callback
        addressController.fetchAddressIntoSpinner(this, binding.spinnerAddress, new AddressFetchCallback() {
            @Override
            public void onAddressesFetched(List<Address> fetchedList) {
                addressList = fetchedList;

                // Only do selection if editing existing data
                if (isEditMode && addressId != -1) {
                    int position = 0;
                    for (int i = 0; i < addressList.size(); i++) {
                        if (addressList.get(i).getId() == addressId) {
                            position = i;
                            break;
                        }
                    }
                    binding.spinnerAddress.setSelection(position);
                    binding.spinnerAddress.setEnabled(false);
                }

            }
        });
    }
}

