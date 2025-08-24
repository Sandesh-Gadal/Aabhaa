package com.example.aabhaa.views.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.example.aabhaa.R;
import com.example.aabhaa.adapters.ContentAdapter;
import com.example.aabhaa.adapters.FarmingTipsAdapter;
import com.example.aabhaa.adapters.PesticideAlertAdapter;
import com.example.aabhaa.controllers.CropController;
import com.example.aabhaa.controllers.EditProfileController;
import com.example.aabhaa.controllers.ExploreController;
import com.example.aabhaa.controllers.MainController;
import com.example.aabhaa.controllers.UserProfileCallback;
import com.example.aabhaa.controllers.WeatherController;
import com.example.aabhaa.data.repository.ContentRepository;
import com.example.aabhaa.data.repository.CropRepository;
import com.example.aabhaa.databinding.FragmentHomeBinding;
import com.example.aabhaa.models.Content;
import com.example.aabhaa.models.User;
import com.example.aabhaa.views.MainActivity;
import com.example.aabhaa.views.NotificationActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private CropController cropController;
    private CropRepository cropRepository;

    private RecyclerView rvPesticides;

    ExploreController exploreController;


    private EditProfileController editProfileController;
    private ContentRepository contentRepository;
    private List<Content> pesticideAlertList;
    private PesticideAlertAdapter pesticideAlertAdapter;

    private List<Content> contentList;
    private List<Content> allContentList;

    private RecyclerView rvFarmingTips;
    private List<Content> farmingTipsList;
    private FarmingTipsAdapter farmingTipsAdapter;

    private RecyclerView rvSectionArticles;
    private ContentAdapter sectionArticlesAdapter;
    private List<Content> sectionArticlesList;




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
        initViews(view);
        initRepository();
        setupRecyclerView();
        setupFarmingTipsRecyclerView(); // farming tips
        loadContents();

        rvSectionArticles = view.findViewById(R.id.rvSectionArticles);
        sectionArticlesList = new ArrayList<>();
        sectionArticlesAdapter = new ContentAdapter(getContext(), sectionArticlesList, exploreController);

        rvSectionArticles.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvSectionArticles.setAdapter(sectionArticlesAdapter);

        loadSectionArticles();



        cropRepository = new CropRepository(getContext());
        cropController = new CropController(getContext(), recyclerView, cropRepository);
        cropController.fetchCropsBySeason();
        binding.includeHeader.notificationContainer.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), NotificationActivity.class);
            startActivity(intent);
        });

        binding.includePesticides.tvPesticideMore.setOnClickListener(v -> {
            ((MainActivity) requireActivity())
                    .switchFragmentById(R.id.nav_explore);
            ((MainActivity) requireActivity())
                    .binding.bottomNavigationView.setSelectedItemId(R.id.nav_explore);
        });

        binding.includeTips.tvTipsMore.setOnClickListener(v -> {
            ((MainActivity) requireActivity())
                    .switchFragmentById(R.id.nav_explore);
            ((MainActivity) requireActivity())
                    .binding.bottomNavigationView.setSelectedItemId(R.id.nav_explore);
        });

        binding.includeSlider.tvSliderMore.setOnClickListener(v -> {
            ((MainActivity) requireActivity())
                    .switchFragmentById(R.id.nav_explore);
            ((MainActivity) requireActivity())
                    .binding.bottomNavigationView.setSelectedItemId(R.id.nav_explore);
        });




        // No BottomNavigationView here
    }

    private void loadSectionArticles() {
        contentRepository.getContentsLiveData().observe(getViewLifecycleOwner(), contents -> {
            if (contents != null && !contents.isEmpty()) {
                // Example: show latest 5 articles
                sectionArticlesList.clear();
//                int limit = Math.min(5, contents.size());
                sectionArticlesList.addAll(contents);
                sectionArticlesAdapter.notifyDataSetChanged();
            }
        });

        contentRepository.fetchVerifiedContents();
    }


    private void initViews(View view) {
        rvPesticides = view.findViewById(R.id.rvExplorePosts);
        rvFarmingTips = view.findViewById(R.id.rvFarmingTips);

    }

    private void filterContentByCategory(String category) {
        List<Content> filteredList;

        if ("All".equals(category)) {
            filteredList = new ArrayList<>(allContentList);
        } else {
            filteredList = allContentList.stream()
                    .filter(content -> category.equalsIgnoreCase(content.category))
                    .collect(Collectors.toList());
        }

        contentList.clear();
        contentList.addAll(filteredList);
        pesticideAlertAdapter.notifyDataSetChanged();
    }

    private void initRepository() {
        contentRepository = new ContentRepository(getContext());
        exploreController = new ExploreController();
        contentList = new ArrayList<>();
        allContentList = new ArrayList<>();
    }

    private void setupRecyclerView() {
        pesticideAlertList = new ArrayList<>();
        pesticideAlertAdapter = new PesticideAlertAdapter(getContext(), pesticideAlertList);

        rvPesticides.setLayoutManager(new LinearLayoutManager(getContext()));
        rvPesticides.setAdapter(pesticideAlertAdapter);

        // Optional: scroll listener for pagination
        rvPesticides.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                // Implement pagination logic here if needed
            }
        });
    }

    private void setupFarmingTipsRecyclerView() {
        farmingTipsList = new ArrayList<>();
        farmingTipsAdapter = new FarmingTipsAdapter(getContext(), farmingTipsList);

        rvFarmingTips.setLayoutManager(new LinearLayoutManager(getContext()));
        rvFarmingTips.setAdapter(farmingTipsAdapter);
    }


    private void loadContents() {
        contentRepository.getContentsLiveData().observe(getViewLifecycleOwner(), contents -> {
            if (contents == null || contents.isEmpty()) return;

            // Pesticide alerts
            List<Content> alerts = new ArrayList<>();
            List<Content> tips = new ArrayList<>();

            for (Content content : contents) {
                if ("pesticides".equalsIgnoreCase(content.category)) {
                    alerts.add(content);
                } else if ("farming tips".equalsIgnoreCase(content.category)) {  // make sure this matches backend
                    tips.add(content);
                }
            }

            // Update pesticide alerts (limit 2)
            pesticideAlertList.clear();
            if (!alerts.isEmpty()) {
                int alertLimit = Math.min(2, alerts.size());
                pesticideAlertList.addAll(alerts.subList(0, alertLimit));
            }
            pesticideAlertAdapter.notifyDataSetChanged();

            // Update farming tips (limit 2)
            farmingTipsList.clear();
            if (!tips.isEmpty()) {
                int tipLimit = Math.min(2, tips.size());
                farmingTipsList.addAll(tips.subList(0, tipLimit));
            }
            farmingTipsAdapter.notifyDataSetChanged();
        });


        // Trigger repository fetch **once**
        contentRepository.fetchVerifiedContents();
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
