package com.example.aabhaa.views.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.aabhaa.R;
import com.example.aabhaa.databinding.FragmentCalendarBinding;
import com.example.aabhaa.views.MainActivity;

public class CalendarFragment extends Fragment {

    private FragmentCalendarBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentCalendarBinding.inflate(inflater, container, false);
        return binding.getRoot(); // Inflates fragment_calendar.xml
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // You can now work with binding.calendar, etc.
        binding.calendar.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            // Handle date selection
        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Avoid memory leaks
    }
}
