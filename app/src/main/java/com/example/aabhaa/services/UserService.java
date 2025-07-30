package com.example.aabhaa.services;

import com.example.aabhaa.models.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PUT;

public interface UserService {

    @PUT("user/profile/update")
    @Headers("Accept: application/json")
    Call<User> updateUser(@Body User user);

    @GET("user")
    Call<User> getUserData();

}
