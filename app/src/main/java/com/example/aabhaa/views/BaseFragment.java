package com.example.aabhaa.views;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.aabhaa.R;
import com.example.aabhaa.views.MainActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public abstract class BaseFragment extends Fragment {

    protected void setupBottomNav(BottomNavigationView navView, @IdRes int selectedItemId) {
        navView.setSelectedItemId(selectedItemId);

        navView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == selectedItemId) return true;

            // Replace the fragment inside MainActivity
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).switchFragmentById(itemId);
            }

            return true;
        });
    }

    public void overrideBackTransition() {
        if (getActivity() != null) {
            getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
    }
}
