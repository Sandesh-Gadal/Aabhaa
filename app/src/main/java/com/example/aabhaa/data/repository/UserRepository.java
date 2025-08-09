package com.example.aabhaa.data.repository;

import android.content.Context;

import com.example.aabhaa.auth.ChangePasswordRequest;
import com.example.aabhaa.controllers.RepositoryCallback;
import com.example.aabhaa.models.ApiResponse;
import com.example.aabhaa.models.User;
import com.example.aabhaa.services.AuthService;
import com.example.aabhaa.services.RetrofitClient;
import com.example.aabhaa.services.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {

    private final UserService userService;
    private  final AuthService authService;

    public  UserRepository (Context context){
        userService = RetrofitClient.getClient(context).create(UserService.class);
        authService = RetrofitClient.getClient(context).create(AuthService.class);
    }

    public Call<User> updateUser(User user ){
        return userService.updateUser(user);
    }

    public  Call<User> getUserProfile(){
        return userService.getUserData();
    }

    public void changePassword(String currentPassword, String newPassword, String newPasswordConfirm, final RepositoryCallback<ApiResponse> callback) {
        ChangePasswordRequest request = new ChangePasswordRequest(currentPassword, newPassword, newPasswordConfirm);

        Call<ApiResponse> call = authService.changePassword(request);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    // handle API error or parse error body
                    callback.onError("Failed to change password");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

}
