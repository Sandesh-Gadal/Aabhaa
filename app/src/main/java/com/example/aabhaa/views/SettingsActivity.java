package com.example.aabhaa.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.example.aabhaa.R;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {

    private SwitchCompat langToggle;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(MyApplication.updateLocale(base));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        findViewById(R.id.btnBack).setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("navigate_to", "profile");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        langToggle = findViewById(R.id.langToggle);

        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        String lang = prefs.getString("lang", "en");

// Temporarily remove listener
        langToggle.setOnCheckedChangeListener(null);

// Set initial state
        langToggle.setChecked(lang.equals("ne"));

// Reattach listener
        langToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            String newLang = isChecked ? "ne" : "en";

            // Switch language globally
            MyApplication.switchLanguage(SettingsActivity.this, newLang);

            // tell caller to reopen profile
//            Intent result = new Intent();
//            result.putExtra("navigate_to", "profile");
            setResult(RESULT_OK);
            updateSettingsTexts();

        });



    }

    private void updateSettingsTexts() {
        // Example: update header
        TextView headerTitle = findViewById(R.id.headerTitle);
        headerTitle.setText(getString(R.string.settings));

        // Update all other views in SettingsActivity
        TextView someOption = findViewById(R.id.langTitle);
        someOption.setText(getString(R.string.language));

        // Repeat for every text in SettingsActivity
    }


}


