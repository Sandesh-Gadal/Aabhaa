package com.example.aabhaa.views.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.aabhaa.databinding.FragmentProfileBinding;
import com.example.aabhaa.views.AddressActivity;
import com.example.aabhaa.views.EditProfileActivity;
import com.example.aabhaa.views.ForgotpasswordActivity;


public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot(); // Inflate the layout for this fragment
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.editProfile.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), EditProfileActivity.class);
            startActivity(intent);
        });



        binding.address.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), AddressActivity.class);
            startActivity(intent);
        });

        binding.changePassword.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), ForgotpasswordActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
