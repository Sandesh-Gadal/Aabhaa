package com.example.aabhaa.views;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aabhaa.R;
import com.example.aabhaa.databinding.ActivityCropDetailsBinding;
import com.google.android.material.chip.Chip;

public class CropDetailsActivity extends AppCompatActivity {

    private ActivityCropDetailsBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCropDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        View stepView = LayoutInflater.from(this).inflate(R.layout.item_step, binding.layoutSteps, false);
// set data: stepView.findViewById(R.id.tvStepTitle).setText(...);
        binding.layoutSteps.addView(stepView);

        View nutrientView = LayoutInflater.from(this).inflate(R.layout.layout_nutrient_info, binding.layoutNutrients, false);
        binding.layoutNutrients.addView(nutrientView);

        binding.chipGroupCategories.setOnCheckedChangeListener((group, checkedId) -> {
            binding.layoutOverview.setVisibility(View.GONE);
            binding.layoutSteps.setVisibility(View.GONE);
            binding.layoutNutrients.setVisibility(View.GONE);

            Chip selectedChip = group.findViewById(checkedId);
            if (selectedChip != null) {
                String tag = (String) selectedChip.getTag();
                if ("overview".equals(tag)) {
                    binding.layoutOverview.setVisibility(View.VISIBLE);
                } else if ("steps".equals(tag)) {
                    binding.layoutSteps.setVisibility(View.VISIBLE);
                } else if ("nutrients".equals(tag)) {
                    binding.layoutNutrients.setVisibility(View.VISIBLE);
                }
            }
        });

    }
}
