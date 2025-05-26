package com.example.aabhaa.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.aabhaa.Fragments.ChangePasswordFragment;
import com.example.aabhaa.Fragments.ForgotPasswordFragment;
import com.example.aabhaa.Fragments.OtpVerificationFragment;

public class ForgotPasswordPagerAdapter extends FragmentStateAdapter {

    public ForgotPasswordPagerAdapter(@NonNull FragmentActivity fa) {
        super(fa);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new ForgotPasswordFragment();
            case 1:
                return new OtpVerificationFragment();
            case 2:
                return new ChangePasswordFragment();
            default:
                return new ForgotPasswordFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3; // Total steps
    }
}
