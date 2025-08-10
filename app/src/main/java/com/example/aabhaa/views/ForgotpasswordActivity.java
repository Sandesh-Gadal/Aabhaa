package com.example.aabhaa.views;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.example.aabhaa.R;
import com.example.aabhaa.adapters.ForgotPasswordPagerAdapter;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

public class ForgotpasswordActivity extends AppCompatActivity {
    private ViewPager2 viewPager;
    private DotsIndicator dotsIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ftp);

        viewPager = findViewById(R.id.viewPager);
        dotsIndicator = findViewById(R.id.dots_indicator);

        // Set up ViewPager with FragmentStateAdapter
        ForgotPasswordPagerAdapter adapter = new ForgotPasswordPagerAdapter(this);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0, false);
        viewPager.setUserInputEnabled(false);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("start_position")) {
            int startPosition = intent.getIntExtra("start_position", 0);
            Log.d("view", "" + startPosition);

            // Hide dots indicator and don't set it up
            dotsIndicator.setVisibility(View.GONE);
            viewPager.setCurrentItem(startPosition, false);
        } else {
            // Only set up dots indicator when we actually want to show it
            setupDotsIndicator();
        }
    }

    private void setupDotsIndicator() {
        dotsIndicator.setViewPager2(viewPager);
        dotsIndicator.setOnTouchListener((v, event) -> true);

        // Disable clicks for the dots indicator and all its children
        dotsIndicator.setClickable(false);
        dotsIndicator.setFocusable(false);

        for (int i = 0; i < dotsIndicator.getChildCount(); i++) {
            View dot = dotsIndicator.getChildAt(i);
            dot.setClickable(false);
            dot.setFocusable(false);
            dot.setOnTouchListener((v, event) -> true);
        }

        dotsIndicator.setVisibility(View.VISIBLE);
    }
}