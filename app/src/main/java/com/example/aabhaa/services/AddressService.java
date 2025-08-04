package com.example.aabhaa.services;

import com.example.aabhaa.models.Address;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface AddressService {
    @POST("address/add")
    Call<ResponseBody> storeAddress(@Body Address address);

    @GET("user/addresses")
    Call<List<Address>> getUserAddresses();
}
