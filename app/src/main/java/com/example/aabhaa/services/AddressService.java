package com.example.aabhaa.services;

import com.example.aabhaa.models.Address;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface AddressService {
    @POST("address/add")
    Call<ResponseBody> storeAddress(@Body Address address);

    @GET("user/addresses")
    Call<List<Address>> getUserAddresses();

    @DELETE("address/delete/{id}")
    Call<Void> deleteAddress(@Path("id") int id);

    @PUT("address/update/{id}")
    Call<Void> updateAddress(@Path("id") int id, @Body Address address);


}
