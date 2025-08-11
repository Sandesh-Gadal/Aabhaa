package com.example.aabhaa.views;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import com.example.aabhaa.R;
import com.example.aabhaa.adapters.ForgotPasswordPagerAdapter;
import com.example.aabhaa.views.Fragments.OtpVerificationFragment;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

public class ForgotpasswordActivity extends AppCompatActivity {
    private ViewPager2 viewPager;
    private DotsIndicator dotsIndicator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ftp);

        String email = getIntent().getStringExtra("email");
        int startPosition = getIntent().getIntExtra("start_position", 0);

        viewPager = findViewById(R.id.viewPager);
        dotsIndicator = findViewById(R.id.dots_indicator);

        ForgotPasswordPagerAdapter adapter = new ForgotPasswordPagerAdapter(this, email);
        viewPager.setAdapter(adapter);
        viewPager.setUserInputEnabled(false);
        dotsIndicator.setVisibility(View.GONE);
        dotsIndicator.setViewPager2(viewPager);

        if (startPosition > 0) {
            viewPager.setCurrentItem(startPosition, false);
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