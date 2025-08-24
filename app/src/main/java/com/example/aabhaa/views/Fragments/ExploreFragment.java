package com.example.aabhaa.views.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.aabhaa.R;
import com.example.aabhaa.adapters.ContentAdapter;
import com.example.aabhaa.controllers.ExploreController;
import com.example.aabhaa.data.repository.ContentRepository;
import com.example.aabhaa.databinding.FragmentExploreBinding;
import com.example.aabhaa.models.Content;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class ExploreFragment extends Fragment {

    private FragmentExploreBinding binding;
    ExploreController exploreController;

    private RecyclerView rvExplorePosts;
    private ChipGroup chipGroupCategories;
    private ContentAdapter contentAdapter;

    private ContentRepository contentRepository;
    private List<Content> contentList;
    private List<Content> allContentList; // Keep original list for filtering

    private String selectedCategory = "All";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentExploreBinding.inflate(inflater, container, false);
        return binding.getRoot(); // Inflate the layout for this fragment
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Use static mock content
        initViews(view);
        initRepository();
        setupRecyclerView();
        setupChips();
        observeData();
        loadContents();
    }

    private void initViews(View view) {
        rvExplorePosts = view.findViewById(R.id.rvExplorePosts);
        chipGroupCategories = view.findViewById(R.id.chipGroupCategories);
    }

    private void initRepository() {
        contentRepository = new ContentRepository(getContext());
        exploreController = new ExploreController();
        contentList = new ArrayList<>();
        allContentList = new ArrayList<>();
    }

    private void setupRecyclerView() {
        contentAdapter = new ContentAdapter(getContext(), contentList, exploreController);
        rvExplorePosts.setLayoutManager(new LinearLayoutManager(getContext()));
        rvExplorePosts.setAdapter(contentAdapter);

        // Add scroll listener for pagination if needed
        rvExplorePosts.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                // Implement pagination logic here if needed
            }
        });
    }

    private void setupChips() {
        chipGroupCategories.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (!checkedIds.isEmpty()) {
                Chip selectedChip = group.findViewById(checkedIds.get(0));
                if (selectedChip != null) {
                    selectedCategory = selectedChip.getText().toString();
                    filterContentByCategory(selectedCategory);
                }
            }
        });
    }

    private void observeData() {
        // Observe content data
        contentRepository.getContentsLiveData().observe(getViewLifecycleOwner(), contents -> {
            if (contents != null) {
                allContentList.clear();
                allContentList.addAll(contents);

                // Apply current filter
                filterContentByCategory(selectedCategory);

                // Update chips with available categories
                updateChipsWithCategories(contents);
            }
        });

        // Observe loading state
        contentRepository.getLoadingLiveData().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading != null) {
                // You can show/hide progress bar here if you add one to your layout
                // For now, just update the UI accordingly
            }
        });

        // Observe errors
        contentRepository.getErrorLiveData().observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
            }
        });
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
        contentAdapter.notifyDataSetChanged();
    }

    private void updateChipsWithCategories(List<Content> contents) {
        // Get unique categories from content
        List<String> categories = contents.stream()
                .map(content -> content.category)
                .filter(category -> category != null && !category.isEmpty())
                .distinct()
                .collect(Collectors.toList());

        // Clear existing chips except "All"
        chipGroupCategories.removeAllViews();

        // Add "All" chip
        Chip allChip = new Chip(getContext());
        allChip.setText("All");
        allChip.setCheckable(true);
        allChip.setChecked(true);
        chipGroupCategories.addView(allChip);

        // Add category chips
        for (String category : categories) {
            Chip chip = new Chip(getContext());
            chip.setText(category);
            chip.setCheckable(true);
            chipGroupCategories.addView(chip);
        }
    }

    private void loadContents() {
        // Load all verified contents
        contentRepository.fetchVerifiedContents();

        // Alternatively, load dummy data for testing:
        // loadDummyContents();
    }

    private void loadDummyContents() {
        // For testing purposes, load dummy data
        List<Content> dummyContents = exploreController.getDummyContents();
        allContentList.clear();
        allContentList.addAll(dummyContents);
        filterContentByCategory(selectedCategory);
        updateChipsWithCategories(dummyContents);
    }

    private void refreshContents() {
        loadContents();
    }

    // Public method to refresh content (can be called from parent activity)
    public void refresh() {
        refreshContents();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
