package com.example.aabhaa.views;

import android.content.Intent;
import android.os.Bundle;
import com.example.aabhaa.R;

import com.example.aabhaa.databinding.ActivityHomeBinding;

public class HomeActivity extends BaseActivity<ActivityHomeBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize the BottomNavigationView with current selected item

        setupBottomNav(binding.bottomNavigationView, R.id.nav_home);

        binding.includeHeader.notificationContainer.setOnClickListener(v->{
            Intent intent = new Intent(this , NotificationActivity.class);
            startActivity(intent);
        });



    }
}