package com.example.aabhaa.views.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.aabhaa.adapters.ContentAdapter;
import com.example.aabhaa.controllers.ExploreController;
import com.example.aabhaa.data.StaticContentProvider;
import com.example.aabhaa.databinding.FragmentExploreBinding;
import com.example.aabhaa.models.Content;

import java.util.List;


public class ExploreFragment extends Fragment {

    private FragmentExploreBinding binding;
    ExploreController exploreController;

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
        List<Content> contentList = StaticContentProvider.getAllExploreContents();

        // IMPORTANT: Initialize your ExploreController here
        exploreController = new ExploreController();

        // Set adapter
        ContentAdapter adapter = new ContentAdapter(requireContext(), contentList,exploreController);
        binding.rvExplorePosts.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvExplorePosts.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
