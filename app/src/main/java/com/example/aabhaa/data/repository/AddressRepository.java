package com.example.aabhaa.data.repository;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.aabhaa.models.Address;
import com.example.aabhaa.services.AddressService;
import com.example.aabhaa.services.RetrofitClient;
import com.example.aabhaa.views.Fragments.ProfileFragment;
import com.example.aabhaa.views.MainActivity;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AddressRepository {
    private final AddressService addressService;

    public AddressRepository(Context context) {
        Retrofit retrofit = RetrofitClient.getClient(context);
        addressService = retrofit.create(AddressService.class);
    }

    public void sendAddress(Address address, Context context) {
        Call<ResponseBody> call = addressService.storeAddress(address);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Address submitted successfully", Toast.LENGTH_SHORT).show();
                    // Use context instead of 'this'
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.putExtra("navigate_to", "profile");

// Add NEW_TASK flag to handle non-Activity context
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

                    context.startActivity(intent);

// Optional: finish if context is activity
                    if (context instanceof Activity) {
                        ((Activity) context).finish();
                    }



                } else {
                    Toast.makeText(context, "Failed: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}

