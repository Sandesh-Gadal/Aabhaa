package com.example.aabhaa.views.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.aabhaa.R;
import com.example.aabhaa.controllers.ControllerCallback;
import com.example.aabhaa.controllers.EditProfileController;
import com.example.aabhaa.data.repository.UserRepository;
import com.example.aabhaa.databinding.FragmentChangePasswordBinding;

public class ChangePasswordFragment extends Fragment {

    private FragmentChangePasswordBinding binding;

    private EditProfileController editProfileController;

    public ChangePasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding= FragmentChangePasswordBinding.inflate(inflater, container, false);
        this.editProfileController = new EditProfileController(requireContext());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnSubmitChangePassword.setOnClickListener(v -> {


            editProfileController.changePassword(binding.etCurrentPassword,binding.etNewPassword ,binding.etConfirmNewPassword, new ControllerCallback() {
                @Override
                public void onSuccess(String message) {
                    if (getActivity() == null) return;
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                        binding.etCurrentPassword.setText("");
                        binding.etNewPassword.setText("");
                        binding.etConfirmNewPassword.setText("");
                    });
                }

                @Override
                public void onError(String error) {
                    if (getActivity() == null) return;
                    getActivity().runOnUiThread(() ->
                            Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show());
                }
            });
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;  // Avoid memory leaks
    }
}
