package com.example.aabhaa.adapters;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.aabhaa.views.Fragments.ChangePasswordFragment;
import com.example.aabhaa.views.Fragments.ForgotPasswordFragment;
import com.example.aabhaa.views.Fragments.OtpVerificationFragment;

public class ForgotPasswordPagerAdapter extends FragmentStateAdapter {
    private String email;

    public ForgotPasswordPagerAdapter(@NonNull FragmentActivity fa , String email) {
        super(fa);
        this.email = email;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new ForgotPasswordFragment();
            case 1:
                OtpVerificationFragment otpFragment = new OtpVerificationFragment();
                Bundle bundle = new Bundle();
                bundle.putString("email", email);
                otpFragment.setArguments(bundle);
                return otpFragment;
            case 2:
               if(email != null){
                   ChangePasswordFragment changePasswordFragment = new ChangePasswordFragment();
                   Bundle bundle1 = new Bundle();
                   bundle1.putString("email", email);
                   changePasswordFragment.setArguments(bundle1);
                   return changePasswordFragment;
               }else {
                   return new ChangePasswordFragment();
               }
            default:
                return new ForgotPasswordFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3; // Total steps
    }
}
