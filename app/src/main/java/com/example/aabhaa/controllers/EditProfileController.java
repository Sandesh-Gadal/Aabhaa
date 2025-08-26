package com.example.aabhaa.controllers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aabhaa.models.ApiResponse;
import com.example.aabhaa.models.User;
import com.example.aabhaa.data.repository.UserRepository;
import com.example.aabhaa.views.MainActivity;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileController {

    private final Context context;
    private final UserRepository userRepository;

    public EditProfileController(Context context) {
        this.context = context;
        this.userRepository = new UserRepository(context);
    }

    public void handleProfileUpdate(
            TextInputEditText etFullName,
            TextInputEditText etPhone,
            TextInputEditText etDate,
            TextInputEditText etExperience,
            AutoCompleteTextView acvFarmerType,
            AutoCompleteTextView acvEducation,
            RadioGroup genderGroup,
            RadioGroup langGroup,
            String imageUrl
    ) {
        try {

            boolean isValid = true;

            String fullName = etFullName.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String dateOfBirth = etDate.getText().toString().trim();
            String experienceStr = etExperience.getText().toString().trim();
            String farmerType = acvFarmerType.getText().toString().trim();
            String education = acvEducation.getText().toString().trim();

            String gender = getSelectedRadioTag(genderGroup);
            String language = getSelectedRadioTag(langGroup);


            Log.d("EditProfile",""+fullName+"  "+dateOfBirth);
            // Validate each field
            if (fullName.isEmpty()) {
                etFullName.setError("Full name is required");
                isValid = false;
            }

            if (phone.isEmpty()) {
                etPhone.setError("Phone number is required");
                isValid = false;
            }

            if (dateOfBirth.isEmpty()) {
                etDate.setError("Date of birth is required");
                isValid = false;
            }

            int experience = -1;
            if (experienceStr.isEmpty()) {
                etExperience.setError("Experience is required");
                isValid = false;
            } else {
                try {
                    experience = Integer.parseInt(experienceStr);
                } catch (NumberFormatException e) {
                    etExperience.setError("Enter a valid number");
                    isValid = false;
                }
            }

            if (farmerType.isEmpty()) {
                acvFarmerType.setError("Select farmer type");
                isValid = false;
            }

            if (education.isEmpty()) {
                acvEducation.setError("Select education level");
                isValid = false;
            }

            if (gender.isEmpty()) {
                Toast.makeText(context, "Please select gender", Toast.LENGTH_SHORT).show();
                isValid = false;
            }

            if (language.isEmpty()) {
                Toast.makeText(context, "Please select preferred language", Toast.LENGTH_SHORT).show();
                isValid = false;
            }

            if (!isValid) {
                return; // Don’t continue if there are errors
            }



            User user = new User(fullName, phone, gender, dateOfBirth, farmerType, experience, education, language, imageUrl);

//            Log.d("data", "User data: name=" + fullName +
//                    ", phone=" + phone +
//                    ", gender=" + gender +
//                    ", dob=" + dateOfBirth +
//                    ", type=" + farmerType +
//                    ", experience=" + experience +
//                    ", education=" + education +
//                    ", language=" + language +
//                    ", image=" + imageUrl);

            updateUserProfile(user);

        } catch (Exception e) {
            Toast.makeText(context, "Form error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private String getSelectedRadioText(RadioGroup group) {
        int id = group.getCheckedRadioButtonId();
        if (id != -1) {
            RadioButton selected = group.findViewById(id);
            return selected.getText().toString();
        }
        return "";
    }

    private String getSelectedRadioTag(RadioGroup group) {
        int id = group.getCheckedRadioButtonId();
        if (id != -1) {
            RadioButton selected = group.findViewById(id);
            Object tag = selected.getTag();
            return tag != null ? tag.toString() : "";
        }
        return "";
    }


    public void updateUserProfile(User user) {
        Call<User> call = userRepository.updateUser(user);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    Log.d("EditProfile", "Profile updated successfully");

                    // Use context instead of 'this'
                    if (context instanceof Activity) {
                        Intent intent = new Intent();
                        intent.putExtra("profile_updated", true);
                        ((Activity) context).setResult(Activity.RESULT_OK, intent);
                        ((Activity) context).finish();
                    }

                    Toast.makeText(context, "Profile updated", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("EditProfile", "Update failed: " + response.code());
                    Toast.makeText(context, "Update failed: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("EditProfile", "Network error: " + t.getMessage());
                Toast.makeText(context, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public  void fetchUserProfile(UserProfileCallback callback){
        Call<User> call = userRepository.getUserProfile();

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful() && response.body() != null){
                    User user = response.body();
                    callback.onUserDataFetched(user);
                }else{
                    Log.e("EditProfile" , "Failed to fetch" + response.code());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("EditProfile", "Error: " + t.getMessage());

            }
        });
    };

    public void changePassword(EditText currentPassword, EditText newPassword, EditText newPasswordConfirm, final ControllerCallback callback) {
        // Simple validation example

        String currentpassword = currentPassword.getText().toString().trim();
        String newpassword = newPassword.getText().toString().trim();
        String confirmpassword = newPasswordConfirm.getText().toString().trim();
        if (currentpassword == null || currentpassword.isEmpty() || currentpassword.length() < 8) {
            currentPassword.setError("Password must be at least 8 characters and not empty");
            callback.onError("Current password is required");
            return;
        }
        if (newpassword == null || newpassword.length() < 8) {
            newPassword.setError("New password must be at least 8 characters");
            callback.onError("New password must be at least 8 characters");
            return;
        }
        if (!newpassword.equals(confirmpassword)) {
            newPassword.setError("Does not match with confirm password");
            newPasswordConfirm.setError("Doesnot match with the new password");
            callback.onError("New password and confirmation do not match");
            return;
        }

        userRepository.changePassword(currentpassword, newpassword, confirmpassword, new RepositoryCallback<ApiResponse>() {
            @Override
            public void onSuccess(ApiResponse data) {
                currentPassword.setError(null);
                newPassword.setError(null);
                newPasswordConfirm.setError(null);
                callback.onSuccess(data.getMessage());
            }

            @Override
            public void onError(String errorMessage) {
                callback.onError(errorMessage);
            }
        });
    }

    public void sendOtpToEmail(EditText email, ControllerCallback callback) {
        String emailStr = email.getText().toString().trim();

        // Simple regex for email validation
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (emailStr.isEmpty()) {
            email.setError("Email cannot be empty");
            callback.onError("Email cannot be empty");
            return;
        }

        if (!emailStr.matches(emailPattern)) {
            email.setError("Please enter a valid email address");
            callback.onError("Please enter a valid email address");
            return;
        }

        userRepository.sendOtp(emailStr, new RepositoryCallback<String>() {
            @Override
            public void onSuccess(String data) {
                callback.onSuccess(data);
            }

            @Override
            public void onError(String errorMessage) {
                callback.onError(errorMessage);
            }
        });
    }

    public void verifyOtp(String email, String otp, ControllerCallback callback) {

        if (otp == null || otp.length() != 4) {
            callback.onError("OTP must be 4 digits");
            return;
        }

        userRepository.verifyOtp(email, otp, new RepositoryCallback<String>() {
            @Override
            public void onSuccess(String data) {
                Log.d("view","this is in the controller success"+email+otp);
                callback.onSuccess(data);
            }

            @Override
            public void onError(String errorMessage) {
                Log.d("view","this is in the controller error"+email+otp);
                callback.onError(errorMessage);
            }
        });
    }
    public void resetPasswordByEmail(String email, String newPassword, String confirmPassword, ControllerCallback callback) {
        // No EditTexts, just string parameters
        userRepository.resetPasswordByEmail(email, newPassword, confirmPassword, new RepositoryCallback<ApiResponse>() {
            @Override
            public void onSuccess(ApiResponse data) {
                callback.onSuccess(data.getMessage());
            }

            @Override
            public void onError(String errorMessage) {
                callback.onError(errorMessage);
            }
        });
    }




}
