package com.example.aabhaa.data.repository;

import android.content.Context;
import android.util.Log;

import com.example.aabhaa.auth.ChangePasswordRequest;
import com.example.aabhaa.controllers.RepositoryCallback;
import com.example.aabhaa.models.ApiResponse;
import com.example.aabhaa.models.User;
import com.example.aabhaa.services.AuthService;
import com.example.aabhaa.services.RetrofitClient;
import com.example.aabhaa.services.UserService;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.util.Log;

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

    public void sendOtp(String email, RepositoryCallback<String> callback) {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("email", email);

        authService.sendOtp(requestBody).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess("OTP sent successfully");
                } else {
                    try {
                        // Read error body from Laravel
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : null;
                        if (errorBody != null) {
                            JSONObject json = new JSONObject(errorBody);
                            String message = json.optString("message", "Failed to send OTP");
                            callback.onError(message);
                        } else {
                            callback.onError("Unknown error occurred");
                        }
                    } catch (Exception e) {
                        callback.onError("Error parsing server response");
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public void verifyOtp(String email, String otp, RepositoryCallback<String> callback) {
        Map<String, String> fields = new HashMap<>();
        fields.put("email", email);
        fields.put("otp", otp);
        Log.d("view","this is the "+email+otp);
        authService.verifyOtp(fields).enqueue(new Callback<ApiResponse>() {


            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse apiResponse = response.body();
                    if ("success".equalsIgnoreCase(apiResponse.getStatus())) {
                        Log.d("view","this is in the repo sucess "+email+otp);
                        callback.onSuccess(apiResponse.getMessage());
                    } else {
                        callback.onError(apiResponse.getMessage());
                    }
                } else {
                    Log.d("view","this is in the repo error "+email+otp);
                    callback.onError("Failed to verify OTP. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public void resetPasswordByEmail(String email, String newPassword, String newPasswordConfirmation, RepositoryCallback<ApiResponse> callback) {
        // No model class for request - use Map for simplicity
        Map<String, String> body = new HashMap<>();
        body.put("email", email);
        body.put("new_password", newPassword);
        body.put("new_password_confirmation", newPasswordConfirmation);

        Call<ApiResponse> call = authService.resetPasswordByEmail(body);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Failed to reset password. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }


}
