package com.example.aabhaa.views;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.example.aabhaa.R;
import com.example.aabhaa.models.Issue;
import com.example.aabhaa.services.IssueApiService;
import com.example.aabhaa.services.RetrofitClient;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendIssueActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 101;

    private TextInputEditText etTitle, etContent;
    private AutoCompleteTextView acvCategory, acvPriority;
    private MaterialButton btnPublish;

    private View btnUpload;

    private Uri selectedImageUri;
    private String uploadedImageUrl = "";

    private IssueApiService apiService;

    ImageView ivUploadedImage ;

    @Override
    protected void attachBaseContext(android.content.Context newBase) {
        super.attachBaseContext(MyApplication.updateLocale(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendissue);

        // Initialize Cloudinary

        // UI
        etTitle = findViewById(R.id.etTitle);
        etContent = findViewById(R.id.etContent);
        acvCategory = findViewById(R.id.acvCategoryProblem);
        btnUpload = findViewById(R.id.uploadContent); // CardView click
        btnPublish = findViewById(R.id.btnPublish);
        ivUploadedImage = findViewById(R.id.ivUploadedImage);

        apiService = RetrofitClient.getClient(this).create(IssueApiService.class);

        setupCategoryDropdown();

        btnUpload.setOnClickListener(v -> selectImage());
        btnPublish.setOnClickListener(v -> publishIssue());
    }

    private void setupCategoryDropdown() {
        String[] categories = {"UI", "Features", "Performance", "Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, categories);
        acvCategory.setAdapter(adapter);
    }

//    private void setupPriorityDropdown() {
//        String[] priorities = {"Low", "Medium", "High"};
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, priorities);
//        acvPriority.setAdapter(adapter);
//    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            uploadImageToCloudinary(selectedImageUri);
        }
    }



    private void uploadImageToCloudinary(Uri imageUri) {
        if (imageUri == null) return;

        btnPublish.setEnabled(false); // disable until upload finishes

        MediaManager.get().upload(imageUri).callback(new UploadCallback() {
            @Override public void onStart(String requestId) {}
            @Override public void onProgress(String requestId, long bytes, long totalBytes) {}
            @Override public void onReschedule(String requestId, ErrorInfo error) {}
            @Override
            public void onSuccess(String requestId, Map resultData) {
                uploadedImageUrl = (String) resultData.get("secure_url");
                Glide.with(SendIssueActivity.this)
                        .load(uploadedImageUrl)
                        .centerCrop()
                        .into(ivUploadedImage);
                Toast.makeText(SendIssueActivity.this, "Image uploaded", Toast.LENGTH_SHORT).show();
                btnPublish.setEnabled(true);
            }
            @Override
            public void onError(String requestId, ErrorInfo error) {
                Toast.makeText(SendIssueActivity.this, "Upload failed: " + error.getDescription(), Toast.LENGTH_SHORT).show();
                btnPublish.setEnabled(true);
            }
        }).dispatch();
    }

    private void publishIssue() {
        String title = etTitle.getText().toString().trim();
        String category = acvCategory.getText().toString().trim();
        String message = etContent.getText().toString().trim();

        Issue issue = new Issue(title, category, message, uploadedImageUrl, "open", "low");

        apiService.sendIssue(issue).enqueue(new Callback<Issue>() {
            @Override
            public void onResponse(Call<Issue> call, Response<Issue> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(SendIssueActivity.this, "Issue sent successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(SendIssueActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Issue> call, Throwable t) {
                Toast.makeText(SendIssueActivity.this, "Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
