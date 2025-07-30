package com.example.aabhaa.views;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aabhaa.R;
import com.example.aabhaa.auth.SharedPrefManager;

public class SplashActivity extends AppCompatActivity {
    ImageView leafImage;
    View[] dots;
    Handler handler;
    int currentIndex = 0;
    Runnable dotAnimator;

    SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPrefManager = new SharedPrefManager(this);
        if (sharedPrefManager.isLoggedIn()) {
            // User is already logged in, go to home
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            // Not logged in, go to login screen
            new Handler().postDelayed(() -> {
                handler.removeCallbacks(dotAnimator); // Stop dot loop
                startActivity(new Intent(SplashActivity.this, OnboardingActivity.class));
                finish();
            }, 200); // Adjusted to match ~1 full dot loop cycle
        }

        setContentView(R.layout.activity_splash);
        leafImage = findViewById(R.id.leafImage);

        Animation bounce = AnimationUtils.loadAnimation(this, R.anim.bounce);
        leafImage.startAnimation(bounce);

        // Dots logic (if using them)
        dots = new View[]{
                findViewById(R.id.dot1),
                findViewById(R.id.dot2),
                findViewById(R.id.dot3)
        };

        handler = new Handler();
        dotAnimator = new Runnable() {
            @Override
            public void run() {
                updateDots(currentIndex);
                currentIndex = (currentIndex + 1) % dots.length;
                handler.postDelayed(this, 500); // Switch dot every 700ms
            }
        };
        handler.post(dotAnimator); // Start dot animation loop

//         Go to MainActivity after 3.2 seconds

    }

    private void updateDots(int currentIndex) {
        for (int i = 0; i < dots.length; i++) {
            if (i == currentIndex) {
                dots[i].setBackgroundResource(R.drawable.dot_active);
                dots[i].animate().scaleX(1.5f).scaleY(1.5f).setDuration(200).start();
            } else {
                dots[i].setBackgroundResource(R.drawable.dot_inactive);
                dots[i].animate().scaleX(1f).scaleY(1f).setDuration(200).start();
            }
        }
    }
}
