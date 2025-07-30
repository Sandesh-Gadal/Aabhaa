package com.example.aabhaa.data.repository;

import android.content.Context;

import com.example.aabhaa.models.User;
import com.example.aabhaa.services.RetrofitClient;
import com.example.aabhaa.services.UserService;

import retrofit2.Call;

public class UserRepository {

    private final UserService userService;

    public  UserRepository (Context context){
        userService = RetrofitClient.getClient(context).create(UserService.class);
    }

    public Call<User> updateUser(User user ){
        return userService.updateUser(user);
    }

    public  Call<User> getUserProfile(){
        return userService.getUserData();
    }

}
