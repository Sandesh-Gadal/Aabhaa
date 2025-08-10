package com.example.aabhaa.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.aabhaa.controllers.MainController;
import com.example.aabhaa.controllers.WeatherController;
import com.example.aabhaa.utils.CustomToast;
import com.example.aabhaa.views.Fragments.CalendarFragment;
import com.example.aabhaa.views.Fragments.ExploreFragment;
import com.example.aabhaa.views.Fragments.HomeFragment;
import com.example.aabhaa.views.Fragments.ProfileFragment;
import com.example.aabhaa.views.Fragments.WeatherFragment;
import com.example.aabhaa.R;
import com.example.aabhaa.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FloatingActionButton fabChatBot;
    private MainController mainController;
    private WeatherController weatherController;
    private int currentSelectedItemId = -1; // Initially no selection

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        // 💡 Initialize FAB and controller first
        fabChatBot = findViewById(R.id.fabChatBot);
        WeatherController weatherController = new WeatherController(getApplicationContext());
        mainController = new MainController(this , weatherController);


        // Init draggable behavior
        mainController.initDraggableChatBot(fabChatBot,
                findViewById(R.id.headerCard),
                findViewById(R.id.bottomNavigationView));

        // Position FAB at bottom-right after layout has rendered
        fabChatBot.post(() -> mainController.positionFabAtBottomRight(fabChatBot));

        mainController.checkAndRequestLocationPermission(this);



        binding.bottomNavigationView.setSelectedItemId(R.id.nav_home);
        switchFragmentById(R.id.nav_home);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switchFragmentById(item.getItemId());
            return true;
        });

        handleIntent(getIntent());

        binding.fabChatBot.setOnClickListener(v -> {
            Log.d("FAB", "FAB clicked!");
            Toast.makeText(this, "FAB clicked!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, ChatActivity.class);
            startActivity(intent);
        });

        binding.backButton.btnBack.setOnClickListener(v -> {
            binding.bottomNavigationView.setSelectedItemId(R.id.nav_home);

        });




    }




    public void switchFragmentById(int newItemId) {
        if (newItemId == currentSelectedItemId) return;

        Fragment fragment = null;
        boolean hideHeader;
        boolean showFab = false; // track fab visibility

        if (newItemId == R.id.nav_home) {
            fragment = new HomeFragment();
            hideHeader = true;
            showFab = true ;
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

        // === Fab visibility and position ===
        if (showFab) {
            fabChatBot.setVisibility(View.VISIBLE);
            mainController.positionFabAtBottomRight(fabChatBot);
        } else {
            fabChatBot.setVisibility(View.GONE);
        }
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
