package com.example.aabhaa.controllers;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aabhaa.adapters.SoilAdapter;
import com.example.aabhaa.data.repository.SoilRepository;
import com.example.aabhaa.models.Address;
import com.example.aabhaa.models.Soil;
import com.example.aabhaa.models.SoilResponse;
import com.example.aabhaa.services.RetrofitClient;
import com.example.aabhaa.services.SoilService;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SoilController {

    private SoilRepository soilRepository;

    private SoilAdapter adapter;

    private  Context context;

    public SoilController(Context context) {
        SoilService soilService = RetrofitClient.getClient(context).create(SoilService.class);
        this.soilRepository = new SoilRepository(soilService);
        this.context = context;

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

        Soil soil = new Soil(-1 ,addressId, -1, -1, nitrogen, phosphorus, potassium, ph);

           soilRepository.sendSoilData(context, soil , false);

    }

    public void populateSoilRecyclerView(Context context, RecyclerView recyclerView) {
        soilRepository.fetchSoilData(context, new SoilFetchCallback() {

            @Override
            public void onSoilFetched(List<Soil> soilList) {
                if (soilList == null ) {

                    Toast.makeText(context, "No address to show", Toast.LENGTH_SHORT).show();
                    return;
                }

                SoilAdapter adapter = new SoilAdapter(context , soilList);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                recyclerView.setAdapter(adapter);
            }
        });
    }

    public void deleteSoilData(int position, Soil soil) {
        new AlertDialog.Builder(context)
                .setTitle("Delete Soil Data")
                .setMessage("Are you sure you want to delete this Soil Data")
                .setPositiveButton("Yes", (dialog, which) -> {
                    // User confirmed deletion
                    soilRepository.deleteSoilData(soil.getId(), new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(context, "Address deleted", Toast.LENGTH_SHORT).show();
                                adapter.removeSoilDataAt(position);
                            } else {
                                Toast.makeText(context, "Failed to delete address", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.dismiss(); // Just close the dialog
                })
                .show();
    }

    public void setAdapter(SoilAdapter adapter) {
        this.adapter = adapter;
    }


}
