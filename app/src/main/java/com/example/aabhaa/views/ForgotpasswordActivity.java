package com.example.aabhaa.views;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.aabhaa.Fragments.ChangePasswordFragment;
import com.example.aabhaa.Fragments.ForgotPasswordFragment;
import com.example.aabhaa.Fragments.OtpVerificationFragment;
import com.example.aabhaa.R;
import com.example.aabhaa.adapter.ForgotPasswordPagerAdapter;
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
