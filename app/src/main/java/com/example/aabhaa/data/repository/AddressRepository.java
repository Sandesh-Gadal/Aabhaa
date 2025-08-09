package com.example.aabhaa.data.repository;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.aabhaa.controllers.AddressFetchCallback;
import com.example.aabhaa.models.Address;
import com.example.aabhaa.services.AddressService;
import com.example.aabhaa.services.RetrofitClient;
import com.example.aabhaa.views.Fragments.ProfileFragment;
import com.example.aabhaa.views.MainActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import kotlin.reflect.KFunction;
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

    public void fetchAddresses(Context context, AddressFetchCallback callback) {
        AddressService service = RetrofitClient.getClient(context).create(AddressService.class);

        service.getUserAddresses().enqueue(new Callback<List<Address>>() {
            @Override
            public void onResponse(Call<List<Address>> call, Response<List<Address>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("RESPONSE_BODY", new Gson().toJson(response.body()));

                    for (Address addr : response.body()) {
                        Log.d("PARSED_ADDRESS", "Lat: " + addr.getLatitude() + ", Lng: " + addr.getLongitude());
                    }

                    callback.onAddressesFetched(response.body());
                } else {
                    Toast.makeText(context, "No address found", Toast.LENGTH_SHORT).show();
                    callback.onAddressesFetched(new ArrayList<>()); // empty list fallback
                }
            }

            @Override
            public void onFailure(Call<List<Address>> call, Throwable t) {
                Toast.makeText(context, "Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                callback.onAddressesFetched(new ArrayList<>()); // empty list fallback
            }
        });
    }




    public void deleteAddress(int id, Callback<Void> callback) {
        Call<Void> call = addressService.deleteAddress(id);
        call.enqueue(callback);
    }

    public void updateAddress(Address address, Context context) {
        Call<Void> call = addressService.updateAddress(address.getId(), address);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Address updated successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.putExtra("navigate_to", "profile");
                    
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

                    context.startActivity(intent);

                    if (context instanceof Activity) {
                        ((Activity) context).finish();
                    }
                } else {
                    Toast.makeText(context, "Failed to update address", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}

