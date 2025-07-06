package com.example.aabhaa.views;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.aabhaa.R;
import com.example.aabhaa.adapters.ForgotPasswordPagerAdapter;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

public class
ForgotpasswordActivity extends AppCompatActivity {
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
        dotsIndicator.setViewPager2(viewPager);
    }
}

//for next section call
//((FtpFragment) getParentFragment()).loadStepFragment(2); // For OTP step
//((ForgotpasswordActivity) getActivity()).viewPager.setCurrentItem(1, true); // For OTP
