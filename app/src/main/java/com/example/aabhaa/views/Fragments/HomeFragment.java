package com.example.aabhaa.views.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.example.aabhaa.R;
import com.example.aabhaa.controllers.CropController;
import com.example.aabhaa.controllers.EditProfileController;
import com.example.aabhaa.controllers.MainController;
import com.example.aabhaa.controllers.UserProfileCallback;
import com.example.aabhaa.controllers.WeatherController;
import com.example.aabhaa.data.repository.CropRepository;
import com.example.aabhaa.databinding.FragmentHomeBinding;
import com.example.aabhaa.models.User;
import com.example.aabhaa.views.NotificationActivity;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private CropController cropController;
    private CropRepository cropRepository;

    private EditProfileController editProfileController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        WeatherController weatherController = new WeatherController(requireContext());


        weatherController.getLatestWeatherForUser().observe(getViewLifecycleOwner(), weather -> {
            weatherController.bindLatestWeather(
                    weather,
                    binding.weatherCard.tvLocation,
                    binding.weatherCard.tvTemperature,
                    binding.weatherCard.tvCondition,
                    binding.weatherCard.tvHumidity,
                    binding.weatherCard.tvPrecipitation,
                    binding.weatherCard.tvWindSpeed,
                    binding.weatherCard.weatherIcon,
                    requireContext()
            );
        });
        updateProfileUI();

        return binding.getRoot(); // Inflates activity_home.xml
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = binding.includeCrops.rvCrops; // use 'view', not getActivity()
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        cropRepository = new CropRepository(getContext());
        cropController = new CropController(getContext(), recyclerView, cropRepository);
        cropController.fetchCropsBySeason();
        binding.includeHeader.notificationContainer.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), NotificationActivity.class);
            startActivity(intent);
        });

        // No BottomNavigationView here
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Prevent memory leaks
    }

    private void updateUIWithUserData(User user) {
        binding.includeHeader.userName.setText(user.getFullName());
        // Load profile image
        Glide.with(this)
                .load(user.getProfileImageUrl())
                .placeholder(R.drawable.bg_wheat)
                .into(binding.includeHeader.profileImage);
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
}
