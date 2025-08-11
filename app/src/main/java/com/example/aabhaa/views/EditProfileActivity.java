package com.example.aabhaa.views;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.example.aabhaa.R;
import com.example.aabhaa.controllers.EditProfileController;
import com.example.aabhaa.controllers.UserProfileCallback;
import com.example.aabhaa.models.User;
import com.google.android.material.textfield.TextInputEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    private static final int IMAGE_PICK_CODE = 1001;
    private String uploadedImageUrl = null;

    private TextInputEditText etFullName, etPhone, etDate, etExperience;
    private AutoCompleteTextView acvFarmerType, acvEducation;
    private RadioGroup genderGroup, langGroup ;

    private RadioButton rbMale , rbFemale ,rbOther;
    private ImageView ivProfile;
    private EditProfileController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Initialize UI
        etFullName = findViewById(R.id.etFullName);
        etPhone = findViewById(R.id.etPhoneNumber);
        etDate = findViewById(R.id.etdatePicker);
        etExperience = findViewById(R.id.etExperience);
        acvFarmerType = findViewById(R.id.autoCompleteFarmerType);
        acvEducation = findViewById(R.id.acvEducation);
        genderGroup = findViewById(R.id.acvGender);
        langGroup = findViewById(R.id.acvLanguage);
        ivProfile = findViewById(R.id.profile_image);
        rbMale = findViewById(R.id.rbMale);
        rbFemale = findViewById(R.id.rbFemale);
        rbOther = findViewById(R.id.rbOther);



        controller = new EditProfileController(this);

        setupDropdowns();
        setupDatePicker();

        controller.fetchUserProfile(new UserProfileCallback() {
            @Override
            public void onUserDataFetched(User user) {
                updateUIWithUserData(user);
            }
        });


        ivProfile.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, IMAGE_PICK_CODE);
        });

        Button btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(v -> {
            if (uploadedImageUrl == null) {
                Toast.makeText(this, "Please wait until image is uploaded", Toast.LENGTH_SHORT).show();
                return;
            }
            controller.handleProfileUpdate(etFullName, etPhone, etDate, etExperience,
                    acvFarmerType, acvEducation, genderGroup, langGroup, uploadedImageUrl);


        });
    }

    private void setupDropdowns() {
        String[] farmerTypes = {"Small Scale", "Commercial", "Organic", "Subsistence"};
        String[] educationLevels = {"No Schooling", "Primary", "High School Graduate", "Undergraduate", "Graduate", "Master's", "PhD"};

        acvFarmerType.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, farmerTypes));
        acvEducation.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, educationLevels));
    }

    private void setupDatePicker() {
        etDate.setFocusable(false);
        etDate.setOnClickListener(view -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    EditProfileActivity.this,
                    (DatePicker view1, int selectedYear, int selectedMonth, int selectedDay) -> {
                        String date = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        etDate.setText(date);
                    },
                    year, month, day
            );

            datePickerDialog.show();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_PICK_CODE && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            ivProfile.setImageURI(imageUri);
            uploadImageToCloudinary(imageUri);
        }
    }

    private void uploadImageToCloudinary(Uri imageUri) {
        long currentEpochSeconds = System.currentTimeMillis() / 1000L;
        Log.d("TIME_CHECK", "Current Epoch Seconds: " + currentEpochSeconds);
        MediaManager.get().upload(imageUri)
                .callback(new UploadCallback() {
                    @Override public void onStart(String requestId) {}
                    @Override public void onProgress(String requestId, long bytes, long totalBytes) {}
                    @Override public void onReschedule(String requestId, ErrorInfo error) {}
                    @Override
                    public void onSuccess(String requestId, Map resultData) {
                        uploadedImageUrl = resultData.get("secure_url").toString();
                        Toast.makeText(EditProfileActivity.this, "Image uploaded", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(String requestId, ErrorInfo error) {
                        Toast.makeText(EditProfileActivity.this, "Upload error: " + error.getDescription(), Toast.LENGTH_LONG).show();
                    }
                }).dispatch();
    }
    private void updateUIWithUserData(User user) {
        etFullName.setText(user.getFullName());
        etPhone.setText(user.getPhone());
        etDate.setText(user.getDateOfBirth());
        etExperience.setText(String.valueOf(user.getExperienceYears()));

        // Gender
        switch (user.getGender()) {
            case "Male": rbMale.setChecked(true); break;
            case "Female": rbFemale.setChecked(true); break;
            case "Other": rbOther.setChecked(true); break;
        }

        // Farmer type (AutoCompleteTextView)
        acvFarmerType.setText(user.getFarmerType(), false);

        // Education
        acvEducation.setText(user.getEducationLevel(), false);

        // Language
        if ("English".equals(user.getPreferredLanguage())) {
            ((RadioButton) langGroup.getChildAt(0)).setChecked(true);
        } else {
            ((RadioButton) langGroup.getChildAt(1)).setChecked(true);
        }

        uploadedImageUrl=user.getProfileImageUrl();
        // Load profile image
        Glide.with(this)
                .load(user.getProfileImageUrl())
                .placeholder(R.drawable.bg_wheat)
                .into(ivProfile);
    }



}
