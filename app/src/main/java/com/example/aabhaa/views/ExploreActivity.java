package com.example.aabhaa.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aabhaa.R;
import com.example.aabhaa.databinding.ActivityExploreBinding;

public class ExploreActivity extends BaseActivity<ActivityExploreBinding> {

  private ActivityExploreBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);

       binding = ActivityExploreBinding.inflate(getLayoutInflater());
       setContentView(binding.getRoot());


       //sign in button click goes to login
        binding.btnSignIn.setOnClickListener(view ->{
            Intent intent = new Intent (ExploreActivity.this , LoginActivity.class);
            startActivity(intent);
        });

        binding.createAccount.setOnClickListener(view -> {
            Intent intent = new Intent (ExploreActivity.this , RegisterActivity.class);
            startActivity(intent);
        });



    }
}
