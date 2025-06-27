package com.example.aabhaa.views;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aabhaa.R;
import com.example.aabhaa.databinding.ActivityProfileBinding;

public class ProfileActivity extends BaseActivity<ActivityProfileBinding> {

    ActivityProfileBinding binding;
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.editProfile.setOnClickListener(v->{
            Intent intent = new Intent(ProfileActivity.this , EditProfileActivity.class);
            startActivity(intent);
        });

        binding.backbtn.btnBack.setOnClickListener(v->{
            Intent intent = new Intent (ProfileActivity.this , HomeActivity.class);
            startActivity(intent);
            finish();
        });

        binding.address.setOnClickListener(v->{
            Intent intent = new Intent(ProfileActivity.this , AddAddressActivity.class);
            startActivity(intent);

        });
  binding.changePassword.setOnClickListener(v->{
      Intent intent = new Intent(this , ForgotpasswordActivity.class);
      startActivity(intent);
  });

    }

}
