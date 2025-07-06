package com.example.aabhaa.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.aabhaa.views.Fragments.CalendarFragment;
import com.example.aabhaa.views.Fragments.ExploreFragment;
import com.example.aabhaa.views.Fragments.HomeFragment;
import com.example.aabhaa.views.Fragments.ProfileFragment;
import com.example.aabhaa.views.Fragments.WeatherFragment;
import com.example.aabhaa.R;
import com.example.aabhaa.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private int currentSelectedItemId = -1; // Initially no selection

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set selected item in BottomNavigationView, triggers onItemSelectedListener or call directly
        binding.bottomNavigationView.setSelectedItemId(R.id.nav_home);

        // Load Home fragment explicitly since listener may not trigger on initial setSelectedItemId
        switchFragmentById(R.id.nav_home);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switchFragmentById(item.getItemId());
            return true;
        });

        handleIntent(getIntent());
    }

    public void switchFragmentById(int newItemId) {
        if (newItemId == currentSelectedItemId) return;

        Fragment fragment = null;
        boolean hideHeader;

        if (newItemId == R.id.nav_home) {
            fragment = new HomeFragment();
            hideHeader = true;
        } else {
            hideHeader = false;
            if (newItemId == R.id.nav_calendar) {
                fragment = new CalendarFragment();
            } else if (newItemId == R.id.nav_weather) {
                fragment = new WeatherFragment();
            } else if (newItemId == R.id.nav_profile){
                fragment = new ProfileFragment();
            } else if (newItemId == R.id.nav_explore) {
                fragment = new ExploreFragment();

            }
        }

        if (fragment == null) return;

        int enterAnim, exitAnim, popEnter, popExit;
        if (getTabIndex(newItemId) > getTabIndex(currentSelectedItemId)) {
            enterAnim = R.anim.slide_in_right;
            exitAnim = R.anim.slide_out_left;
            popEnter = R.anim.slide_in_left;
            popExit = R.anim.slide_out_right;
        } else {
            enterAnim = R.anim.slide_in_left;
            exitAnim = R.anim.slide_out_right;
            popEnter = R.anim.slide_in_right;
            popExit = R.anim.slide_out_left;
        }

        // If this is the first load, skip animation for immediate display
        boolean firstLoad = currentSelectedItemId == -1;

        if (firstLoad) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, fragment)
                    .commit();

            // Immediately update header visibility on first load
            binding.headerCard.setVisibility(hideHeader ? View.GONE : View.VISIBLE);
        } else {
            // Animate transitions on subsequent loads
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(enterAnim, exitAnim, popEnter, popExit)
                    .replace(R.id.fragmentContainer, fragment)
                    .commit();

            // Delay header visibility slightly to sync with animation
            binding.headerCard.postDelayed(() -> {
                binding.headerCard.setVisibility(hideHeader ? View.GONE : View.VISIBLE);
            }, 200);
        }

        currentSelectedItemId = newItemId;
    }

    private int getTabIndex(int itemId) {
        if (itemId == R.id.nav_home) return 0;
        if (itemId == R.id.nav_calendar) return 1;
        if (itemId == R.id.nav_weather) return 2;
        if (itemId == R.id.nav_explore) return 3;
        if (itemId == R.id.nav_profile) return 4;
        return 0;
    }


    private void handleIntent(Intent intent) {
        if (intent == null) return;

        String target = intent.getStringExtra("navigate_to");
        if (target == null) return;

        switch (target) {
            case "home":
                binding.bottomNavigationView.setSelectedItemId(R.id.nav_home);
                break;
            case "calendar":
                binding.bottomNavigationView.setSelectedItemId(R.id.nav_calendar);
                break;
            case "weather":
                binding.bottomNavigationView.setSelectedItemId(R.id.nav_weather);
                break;
            case "explore":
                binding.bottomNavigationView.setSelectedItemId(R.id.nav_explore);
                break;
            case "profile":
                binding.bottomNavigationView.setSelectedItemId(R.id.nav_profile);
                break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent); // Update the intent
        handleIntent(intent);
    }


}
