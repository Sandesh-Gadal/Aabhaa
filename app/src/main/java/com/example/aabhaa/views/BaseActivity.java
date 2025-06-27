package com.example.aabhaa.views;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;

import com.example.aabhaa.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public abstract class BaseActivity<B extends ViewBinding> extends AppCompatActivity {

    protected B binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Do not set contentView here; child activities will handle it using their own binding.
    }

    protected void setupBottomNav(BottomNavigationView navView, int selectedItemId) {
        navView.setSelectedItemId(selectedItemId);

        navView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == selectedItemId) return true;

            if (itemId == R.id.nav_home) {
                startActivity(new Intent(this, HomeActivity.class));
            } else if (itemId == R.id.nav_calendar) {
                startActivity(new Intent(this, CalendarActivity.class));
            } else if (itemId == R.id.nav_explore) {
                startActivity(new Intent(this, ExploreActivity.class));
            } else if (itemId == R.id.nav_weather) {
                startActivity(new Intent(this, WeatherActivity.class));
            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
            }

            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            return true;
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

}
