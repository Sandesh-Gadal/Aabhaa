package com.example.aabhaa.views;

import android.app.DatePickerDialog;
import android.content.Context;
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
import java.util.TimeZone;

public class EditProfileActivity extends AppCompatActivity {

    private static final int IMAGE_PICK_CODE = 1001;
    private String uploadedImageUrl = null;

    private TextInputEditText etFullName, etPhone, etDate, etExperience;
    private AutoCompleteTextView acvFarmerType, acvEducation;
    private RadioGroup genderGroup, langGroup ;

    private RadioButton rbMale , rbFemale ,rbOther;
    private ImageView ivProfile;
    private EditProfileController controller;

    private boolean isImageUploading = false;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MyApplication.updateLocale(newBase));
    }


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
            if (isImageUploading) {
                Toast.makeText(this, "Image is being uploaded, please wait...", Toast.LENGTH_SHORT).show();
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
        etDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        // Format as dd/MM/yyyy
                        String formattedDate = String.format(Locale.ENGLISH, "%02d/%02d/%04d",
                                selectedDay, selectedMonth + 1, selectedYear);
                        etDate.setText(formattedDate);
                    }, year, month, day);

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
        isImageUploading = true;
        Button btnSave = findViewById(R.id.btnSave);
        btnSave.setEnabled(false); // disable save button

        MediaManager.get().upload(imageUri)
                .callback(new UploadCallback() {
                    @Override public void onStart(String requestId) {}
                    @Override public void onProgress(String requestId, long bytes, long totalBytes) {}
                    @Override public void onReschedule(String requestId, ErrorInfo error) {}
                    @Override
                    public void onSuccess(String requestId, Map resultData) {
                        uploadedImageUrl = resultData.get("secure_url").toString();
                        Toast.makeText(EditProfileActivity.this, "Image uploaded", Toast.LENGTH_SHORT).show();
                        isImageUploading = false;
                        btnSave.setEnabled(true); // re-enable save button
                    }
                    @Override
                    public void onError(String requestId, ErrorInfo error) {
                        Toast.makeText(EditProfileActivity.this, "Upload error: " + error.getDescription(), Toast.LENGTH_LONG).show();
                        isImageUploading = false;
                        btnSave.setEnabled(true); // allow retry or skip
                    }
                }).dispatch();
    }

    private void updateUIWithUserData(User user) {
        if (user == null) {
            // User object is null, set all to default
            etFullName.setText("");
            etPhone.setText("");
            etDate.setText("");
            etExperience.setText("0");

            rbMale.setChecked(false);
            rbFemale.setChecked(false);
            rbOther.setChecked(false);

            acvFarmerType.setText("", false);
            acvEducation.setText("", false);

            ((RadioButton) langGroup.getChildAt(0)).setChecked(true); // Default to English

            uploadedImageUrl = null;
            ivProfile.setImageResource(R.drawable.bg_wheat);
            return;
        }

        // Full Name
        etFullName.setText(user.getFullName() != null && !user.getFullName().isEmpty() ? user.getFullName() : "");

        // Phone
        etPhone.setText(user.getPhone() != null && !user.getPhone().isEmpty() ? user.getPhone() : "");

        // Date of Birth
//        etDate.setText(user.getDateOfBirth() != null && !user.getDateOfBirth().isEmpty() ? user.getDateOfBirth() : "");

        String rawDate = user.getDateOfBirth();

        if (rawDate != null && !rawDate.isEmpty()) {
            try {
                // Parse the ISO date string
                SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
                isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                Date date = isoFormat.parse(rawDate);

                // Format to desired output
                SimpleDateFormat desiredFormat = new SimpleDateFormat("d/M/yyyy", Locale.ENGLISH);
                String formattedDate = desiredFormat.format(date);

                etDate.setText(formattedDate);
            } catch (ParseException e) {
                e.printStackTrace();
                etDate.setText(rawDate);  // fallback
            }
        } else {
            etDate.setText("");
        }

        // Experience (integer or number)
        etExperience.setText(user.getExperienceYears() > 0 ? String.valueOf(user.getExperienceYears()) : "0");

        // Gender - reset all first
        rbMale.setChecked(false);
        rbFemale.setChecked(false);
        rbOther.setChecked(false);
        if (user.getGender() != null) {
            switch (user.getGender()) {
                case "Male": rbMale.setChecked(true); break;
                case "Female": rbFemale.setChecked(true); break;
                case "Other": rbOther.setChecked(true); break;
            }
        }

        // Farmer type
        acvFarmerType.setText(user.getFarmerType() != null && !user.getFarmerType().isEmpty() ? user.getFarmerType() : "", false);

        // Education level
        acvEducation.setText(user.getEducationLevel() != null && !user.getEducationLevel().isEmpty() ? user.getEducationLevel() : "", false);

        // Preferred language - default to English if null or unknown
        if ("English".equals(user.getPreferredLanguage())) {
            ((RadioButton) langGroup.getChildAt(0)).setChecked(true);
        } else if ("OtherLanguageCodeOrName".equals(user.getPreferredLanguage())) {
            // Replace OtherLanguageCodeOrName with your actual second language code/name
            ((RadioButton) langGroup.getChildAt(1)).setChecked(true);
        } else {
            // Default fallback
            ((RadioButton) langGroup.getChildAt(0)).setChecked(true);
        }

        // Profile image URL
        uploadedImageUrl = (user.getProfileImageUrl() != null && !user.getProfileImageUrl().isEmpty()) ? user.getProfileImageUrl() : null;

        Glide.with(this)
                .load(uploadedImageUrl != null ? uploadedImageUrl : "")
                .placeholder(R.drawable.bg_wheat)
                .into(ivProfile);
    }




}
