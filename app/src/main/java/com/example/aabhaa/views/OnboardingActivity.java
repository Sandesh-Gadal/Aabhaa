package com.example.aabhaa.views;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;


import com.example.aabhaa.R;
import com.example.aabhaa.adapters.OnboardingAdapter;
import com.example.aabhaa.databinding.ActivityOnboardingBinding;
import com.example.aabhaa.models.OnboardingItem;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback;

import java.util.ArrayList;
import java.util.List;

public class OnboardingActivity extends AppCompatActivity {

  private ActivityOnboardingBinding binding;
  private OnboardingAdapter adapter;
    private ViewPager2 viewPager;
    private DotsIndicator dotsIndicator;
    private final Handler slideHandler = new Handler(Looper.getMainLooper());
    private final int SLIDE_DELAY = 3000; // 3 seconds per slide
    private final int PAUSE_AFTER_TOUCH = 10000; // 10 seconds pause after user interaction


    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);

       binding = ActivityOnboardingBinding.inflate(getLayoutInflater());
       setContentView(binding.getRoot());

       viewPager = binding.viewPager;
       dotsIndicator = binding.dotsIndicator;

        adapter = new OnboardingAdapter(getSlides());
       viewPager.setAdapter(adapter);
        dotsIndicator.setViewPager2(viewPager);

        // Start the auto-slide runnable
        slideHandler.postDelayed(slideRunnable, SLIDE_DELAY);

        // Detect user interaction to pause sliding
        viewPager.getChildAt(0).setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // Pause sliding when user touches
                    slideHandler.removeCallbacks(slideRunnable);
                    break;
                case MotionEvent.ACTION_UP:
                    // Resume sliding after 10 seconds
                    slideHandler.postDelayed(slideRunnable, PAUSE_AFTER_TOUCH);
                    // Call performClick() for accessibility
                    v.performClick();
                    break;
            }
            return false; // Allow other touch events (scroll, swipe)
        });


        //sign in button click goes to login
        binding.btnSignIn.setOnClickListener(view ->{
            Intent intent = new Intent (OnboardingActivity.this , LoginActivity.class);
            startActivity(intent);
        });

        binding.createAccount.setOnClickListener(view -> {
            Intent intent = new Intent (OnboardingActivity.this , RegisterActivity.class);
            startActivity(intent);
        });

        binding.btnNext.setOnClickListener(v -> {
            int current = viewPager.getCurrentItem();
            if (current < adapter.getItemCount() - 1) {
                viewPager.setCurrentItem(current + 1);
            }
        });

        binding.btnPrev.setOnClickListener(v -> {
            int current = viewPager.getCurrentItem();
            if (current > 0) {
                viewPager.setCurrentItem(current - 1);
            }
        });

    }

    private List<OnboardingItem> getSlides() {
        List<OnboardingItem> list = new ArrayList<>();

        list.add(new OnboardingItem(R.drawable.obimg1, "Smart Crop Suggestions", "Get crop advice based on weather.","बुद्धिमान वाली सुझावहरू","मौसमको आधारमा सिफारिसहरू प्राप्त गर्नुहोस्।"));
        list.add(new OnboardingItem(R.drawable.obimg2, "Safe Chemical Usage", "Learn safe pesticide usage.","सुरक्षित रसायन प्रयोग","कीटनाशक प्रयोग विधिहरू सिक्नुहोस्।"));
        list.add(new OnboardingItem(R.drawable.obimg3, "Timely Updates", "Get farming alerts & education.","समयमै अपडेटहरू","सूचनाहरू र कृषि शिक्षा प्राप्त गर्नुहोस्।"));

        return list;
    }

    private Runnable slideRunnable = new Runnable() {
        @Override
        public void run() {
            int current = viewPager.getCurrentItem();
            int total = adapter.getItemCount();
            int next = (current + 1) % total;
            viewPager.setCurrentItem(next);
            slideHandler.postDelayed(this, SLIDE_DELAY);
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        // Stop sliding when activity is not visible
        slideHandler.removeCallbacks(slideRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Resume sliding
        slideHandler.postDelayed(slideRunnable, SLIDE_DELAY);
    }

}
