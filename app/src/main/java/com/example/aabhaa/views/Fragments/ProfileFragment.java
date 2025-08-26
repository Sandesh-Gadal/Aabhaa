package com.example.aabhaa.views.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.aabhaa.R;
import com.example.aabhaa.auth.SharedPrefManager;
import com.example.aabhaa.controllers.EditProfileController;
import com.example.aabhaa.controllers.UserProfileCallback;
import com.example.aabhaa.databinding.FragmentProfileBinding;
import com.example.aabhaa.models.User;
import com.example.aabhaa.views.AddAddressActivity;
import com.example.aabhaa.views.AddressActivity;
import com.example.aabhaa.views.CropDetailsActivity;
import com.example.aabhaa.views.EditProfileActivity;
import com.example.aabhaa.views.FAQActivity;
import com.example.aabhaa.views.ForgotpasswordActivity;
import com.example.aabhaa.views.LoginActivity;
import com.example.aabhaa.views.MyApplication;
import com.example.aabhaa.views.SendIssueActivity;
import com.example.aabhaa.views.SettingsActivity;
import com.example.aabhaa.views.SoilActivity;
import com.example.aabhaa.views.SoilListActivity;


public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private SharedPrefManager sharedPrefManager;

    private EditProfileController editProfileController;

    private static final int EDIT_PROFILE_REQUEST = 1001;



    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(MyApplication.updateLocale(context)); // Do NOT call updateLocale here
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        sharedPrefManager = new SharedPrefManager(requireContext());


        return binding.getRoot(); // Inflate the layout for this fragment
    }



    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // ✅ Initialize controller with context
        editProfileController = new EditProfileController(requireContext());

        editProfileController.fetchUserProfile(new UserProfileCallback() {
            @Override
            public void onUserDataFetched(User user) {
                updateUIWithUserData(user);
            }
        });

        binding.editProfile.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), EditProfileActivity.class);
            startActivityForResult(intent, EDIT_PROFILE_REQUEST);
        });

        binding.myCrops.setOnClickListener(v->{
            Intent intent = new Intent(requireContext() , CropDetailsActivity.class);
            startActivity(intent);
        });

        binding.address.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), AddressActivity.class);
            startActivity(intent);
        });

        binding.changePassword.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), ForgotpasswordActivity.class);
            intent.putExtra("start_position" , 2);
            startActivity(intent);
        });

        binding.reportIssue.setOnClickListener(v->{
            Intent intent = new Intent(requireContext() , SendIssueActivity.class);
            startActivity(intent);
        });

        binding.soil.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), SoilListActivity.class);
            startActivity(intent);
        });

        binding.faq.setOnClickListener((v->{
            Intent intent = new Intent(requireContext() , FAQActivity.class);
            startActivity(intent);
        }));

        binding.settings.setOnClickListener((v->{
          openSettings();
        }));

        binding.logout.setOnClickListener(v->{
            // Clear tokens from shared prefs
            sharedPrefManager.clearTokens();

            // Redirect to login activity
            Intent intent = new Intent(requireContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    private final ActivityResultLauncher<Intent> settingsLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    // pass along the fragment to reload
//                    String target = result.getData().getStringExtra("navigate_to");

                    Intent intent = requireActivity().getIntent();
                    intent.putExtra("navigate_to", "profile");
                    requireActivity().recreate(); // refresh MainActivity + fragment
                }
            });

    private void openSettings() {
        Intent intent = new Intent(requireContext(), SettingsActivity.class);
        settingsLauncher.launch(intent);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    private void updateUIWithUserData(User user) {
        if (binding == null || getActivity() == null || !isAdded()) return;

        binding.name.setText(user.getFullName());
        binding.phone.setText(user.getPhone());

        Glide.with(this)
                .load(user.getProfileImageUrl())
                .placeholder(R.drawable.bg_wheat)
                .into(binding.profileImage);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EDIT_PROFILE_REQUEST && resultCode == Activity.RESULT_OK) {
            boolean profileUpdated = data != null && data.getBooleanExtra("profile_updated", false);
            if (profileUpdated) {
                // Refresh profile data
                updateProfileUI();
            }
        }
    }


    public void updateProfileUI(){
        editProfileController = new EditProfileController(requireContext());

        editProfileController.fetchUserProfile(new UserProfileCallback() {
            @Override
            public void onUserDataFetched(User user) {
                updateUIWithUserData(user);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

   Log.d("LANG","the proflie frament is resumed");

        // Update UI initially
        updateProfileTexts();

        // Register language listener
        MyApplication.setLanguageChangeListener(newLang -> {
            Log.d("LANG", "ProfileFragment detected language change: " + newLang);
            updateProfileTexts();
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        // Remove listener to prevent leaks
        MyApplication.setLanguageChangeListener(null);
    }

    private void updateProfileTexts() {
        if (getView() == null) return;

        // Header / Section titles
        Button editCover = getView().findViewById(R.id.editcoverimg);
        editCover.setText(getString(R.string.edit_cover));

        TextView editProfile = getView().findViewById(R.id.editProfile);
        editProfile.setText(getString(R.string.edit_profile));
        Log.d("LANG", "Edit Profile text set: " + editProfile.getText());
        TextView myCrops = getView().findViewById(R.id.my_crop);
        myCrops.setText(getString(R.string.my_crops));

        TextView address = getView().findViewById(R.id.tvaddress);
        address.setText(getString(R.string.address));

        TextView soil = getView().findViewById(R.id.soiltext);
        soil.setText(getString(R.string.soil_data));

        TextView changePassword = getView().findViewById(R.id.tvchangepwd);
        changePassword.setText(getString(R.string.change_password));

        TextView reportIssue = getView().findViewById(R.id.tvreportissue);
        reportIssue.setText(getString(R.string.report_issue));

        TextView faq = getView().findViewById(R.id.tvlogout);
        faq.setText(getString(R.string.logout));

        TextView logout = getView().findViewById(R.id.tvfaq);
        logout.setText(getString(R.string.faq));



        TextView settingsLabel = getView().findViewById(R.id.tvsettings);
        if (settingsLabel != null) {
            settingsLabel.setText(getString(R.string.settings));
        }
    }

}
