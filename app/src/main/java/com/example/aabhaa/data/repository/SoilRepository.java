package com.example.aabhaa.data.repository;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.aabhaa.models.Soil;
import com.example.aabhaa.services.SoilService;
import com.example.aabhaa.views.MainActivity;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SoilRepository {
    private SoilService soilService;

    public SoilRepository(SoilService soilService) {
        this.soilService = soilService;
    }

    public void sendSoilData(Context context, Soil soil, boolean forceUpdate) {
        Call<ResponseBody> call = soilService.submitSoilData(soil, forceUpdate);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // Parse JSON response
                    try {
                        JSONObject json = new JSONObject(response.body().string());
                        if (json.optBoolean("exists", false)) {
                            // Show dialog because data exists
                            new AlertDialog.Builder(context)
                                    .setTitle("Data Exists")
                                    .setMessage("Data already exists for this address. Update it?")
                                    .setPositiveButton("Yes", (dialog, which) -> {
                                        // Retry with forceUpdate = true
                                        sendSoilData(context, soil, true);
                                    })
                                    .setNegativeButton("No", (dialog, which) -> {
                                        Toast.makeText(context, "Update cancelled", Toast.LENGTH_SHORT).show();
                                    })
                                    .show();
                        } else {
                            Toast.makeText(context, json.getString("message"), Toast.LENGTH_SHORT).show();
                            // Handle success (navigate or close activity)

                            // ✅ Navigate back to MainActivity
                            Intent intent = new Intent(context, MainActivity.class);
                            intent.putExtra("navigate_to", "profile");
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);

                            if (context instanceof Activity) {
                                ((Activity) context).finish();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(context, "Response parse error", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Submission failed: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


}

