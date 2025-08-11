package com.example.aabhaa.views.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.aabhaa.R;
import com.example.aabhaa.auth.SharedPrefManager;
import com.example.aabhaa.controllers.ControllerCallback;
import com.example.aabhaa.controllers.EditProfileController;
import com.example.aabhaa.data.repository.UserRepository;
import com.example.aabhaa.databinding.FragmentChangePasswordBinding;
import com.example.aabhaa.views.LoginActivity;

public class ChangePasswordFragment extends Fragment {

    private FragmentChangePasswordBinding binding;

    private EditProfileController editProfileController;
    String emailToSend;

    private SharedPrefManager sharedPrefManager ;

    public ChangePasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding= FragmentChangePasswordBinding.inflate(inflater, container, false);
        this.editProfileController = new EditProfileController(requireContext());
        sharedPrefManager= new SharedPrefManager(requireContext());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

            editProfileController = new EditProfileController(requireContext());

            if (getArguments() != null && getArguments().containsKey("email")) {
                emailToSend = getArguments().getString("email");
                binding.etCurrentPassword.setVisibility(View.GONE);
                binding.currentPasswordLayout.setVisibility(View.GONE);
//                binding.etCurrentPassword.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);

                binding.btnSubmitChangePassword.setOnClickListener(v -> {
                    String newPassword = binding.etNewPassword.getText().toString().trim();
                    String confirmPassword = binding.etConfirmNewPassword.getText().toString().trim();

                    if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                        Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (!newPassword.equals(confirmPassword)) {
                        Toast.makeText(getContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                        return;
                    }


                    editProfileController.resetPasswordByEmail(
                           emailToSend,
                            newPassword,
                            confirmPassword,
                            new ControllerCallback() {
                                @Override
                                public void onSuccess(String message) {
                                    if (getActivity() == null) return;
                                    getActivity().runOnUiThread(() -> {
                                        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                                        binding.etNewPassword.setText("");
                                        binding.etConfirmNewPassword.setText("");
                                        binding.etCurrentPassword.setVisibility(View.VISIBLE);
                                        binding.currentPasswordLayout.setVisibility(View.VISIBLE);
                                        Intent intent = new Intent(requireContext(), LoginActivity.class );
                                        startActivity(intent);
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


            } else {
                binding.etCurrentPassword.setVisibility(View.VISIBLE);
                binding.currentPasswordLayout.setVisibility(View.VISIBLE);

                binding.btnSubmitChangePassword.setOnClickListener(v -> {
                    editProfileController.changePassword(
                            binding.etCurrentPassword,
                            binding.etNewPassword,
                            binding.etConfirmNewPassword,
                            new ControllerCallback() {
                                @Override
                                public void onSuccess(String message) {
                                    if (getActivity() == null) return;
                                    getActivity().runOnUiThread(() -> {
                                        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                                        binding.etCurrentPassword.setText("");
                                        binding.etNewPassword.setText("");
                                        binding.etConfirmNewPassword.setText("");

                                        sharedPrefManager.clearTokens();
                                        // Redirect to login activity
                                        Intent intent = new Intent(requireContext(), LoginActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);

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
        }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;  // Avoid memory leaks
    }
}
