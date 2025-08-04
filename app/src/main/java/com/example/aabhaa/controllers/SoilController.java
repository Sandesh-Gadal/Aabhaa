package com.example.aabhaa.controllers;

import android.content.Context;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.aabhaa.data.repository.SoilRepository;
import com.example.aabhaa.models.Address;
import com.example.aabhaa.models.Soil;
import com.example.aabhaa.services.RetrofitClient;
import com.example.aabhaa.services.SoilService;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class SoilController {

    private SoilRepository soilRepository;

    public SoilController(Context context) {
        SoilService soilService = RetrofitClient.getClient(context).create(SoilService.class);
        this.soilRepository = new SoilRepository(soilService);
    }

    public void handleSaveSoilData(
            Context context,
            List<Address> addressList,
            Spinner spinnerAddress,
            TextInputEditText etN,
            TextInputEditText etP,
            TextInputEditText etK,
            TextInputEditText etPh
    ) {
        boolean isValid = true;

        int selectedIndex = spinnerAddress.getSelectedItemPosition();
        if (selectedIndex < 0 || selectedIndex >= addressList.size()) {
            Toast.makeText(context, "Please select a valid address", Toast.LENGTH_SHORT).show();
            return;
        }
        int addressId = addressList.get(selectedIndex).getId();

        String nStr = etN.getText().toString().trim();
        String pStr = etP.getText().toString().trim();
        String kStr = etK.getText().toString().trim();
        String phStr = etPh.getText().toString().trim();

        if (nStr.isEmpty()) {
            etN.setError("Required");
            isValid = false;
        }
        if (pStr.isEmpty()) {
            etP.setError("Required");
            isValid = false;
        }
        if (kStr.isEmpty()) {
            etK.setError("Required");
            isValid = false;
        }
        if (phStr.isEmpty()) {
            etPh.setError("Required");
            isValid = false;
        }

        if (!isValid) return;

        double nitrogen = Double.parseDouble(nStr);
        double phosphorus = Double.parseDouble(pStr);
        double potassium = Double.parseDouble(kStr);
        double ph = Double.parseDouble(phStr);

        Soil soil = new Soil(addressId, -1, -1, nitrogen, phosphorus, potassium, ph);
        soilRepository.sendSoilData(context, soil , false);
    }
}
