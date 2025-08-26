package com.example.aabhaa.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.aabhaa.R;
import com.example.aabhaa.controllers.CropController;
import com.example.aabhaa.data.repository.CropRepository;
import com.example.aabhaa.models.Crop;
import com.example.aabhaa.databinding.ActivityCropDetailsBinding;
import com.google.android.material.chip.Chip;
import java.util.Locale;

public class CropDetailsActivity extends AppCompatActivity {

    private ActivityCropDetailsBinding binding;
    private CropController cropController;
    private CropRepository cropRepository;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MyApplication.updateLocale(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCropDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get crop from intent
        Crop crop = (Crop) getIntent().getParcelableExtra("crop_item");
        if (crop != null) {
            populateCropDetails(crop);
        }

        RecyclerView recyclerView = binding.rvYouMayAlsoLike;
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        cropRepository = new CropRepository(this);
        cropController = new CropController(recyclerView, cropRepository, crop1 -> {
            Intent intent = new Intent(this, CropDetailsActivity.class);
            intent.putExtra("crop_item", crop1);
            startActivity(intent);
            finish();
        });
        cropController.fetchCropsBySeason();

        setupChips();
    }

    private void populateCropDetails(Crop crop) {
        // Image
        Glide.with(this)
                .load(crop.getImage_url())
                .placeholder(R.drawable.bg_wheat)
                .into(binding.profileImage);

        // Name
        binding.cropName.setText(crop.getName() + " | " + crop.getName());

        // Description based on locale
        Locale current = getResources().getConfiguration().locale;
        String description = current.getLanguage().equals(new Locale("ne").getLanguage())
                ? crop.getDescription_ne()
                : crop.getDescription_en();

        binding.tvRichDescription.setText(description);

        ((TextView) findViewById(R.id.tvDuration))
                .setText(crop.getDuration_days_min() + " - " + crop.getDuration_days_max() + " Days");
        ((TextView) findViewById(R.id.tvPHLevel))
                .setText(crop.getPh_min() + " - " + crop.getPh_max());
        ((TextView) findViewById(R.id.tvTemperature))
                .setText(crop.getTemp_min_c() + "°C - " + crop.getTemp_max_c() + "°C");

        // Steps
        binding.layoutSteps.removeAllViews();
        if (crop.getSteps() != null && !crop.getSteps().isEmpty()) {
            TextView tvSteps = new TextView(this);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                tvSteps.setText(Html.fromHtml(crop.getSteps(), Html.FROM_HTML_MODE_LEGACY));
            } else {
                tvSteps.setText(Html.fromHtml(crop.getSteps()));
            }
            binding.layoutSteps.addView(tvSteps);
        }

        // Nutrients
        // ✅ Nutrients (individual fields)
        binding.layoutNutrients.removeAllViews();

// Inflate the nutrient layout
        View nutrientView = LayoutInflater.from(this)
                .inflate(R.layout.layout_nutrient_info, binding.layoutNutrients, false);

// Set values
        ((TextView) nutrientView.findViewById(R.id.tvNitrogen)).setText(
                crop.getN_min_kg() + " " + getString(R.string.kg_per_ha));
        ((TextView) nutrientView.findViewById(R.id.tvPhosphorus)).setText(
                crop.getP_min_kg() + " " + getString(R.string.kg_per_ha));
        ((TextView) nutrientView.findViewById(R.id.tvPotassium)).setText(
                crop.getK_min_kg() + " " + getString(R.string.kg_per_ha));

// Add to parent
        binding.layoutNutrients.addView(nutrientView);

    }

    private void setupChips() {
        binding.chipGroupCategories.removeAllViews();

        String[] categoriesBackend = {"overview", "steps", "nutrients"};
        String[] categoriesEN = {"Overview", "Steps", "Nutrients"};
        String[] categoriesNE = {"अवलोकन", "चरणहरू", "पोषक तत्व"};

        Locale current = getResources().getConfiguration().locale;
        String[] categoriesDisplay = current.getLanguage().equals(new Locale("ne").getLanguage())
                ? categoriesNE
                : categoriesEN;

        for (int i = 0; i < categoriesBackend.length; i++) {
            Chip chip = new Chip(this);
            chip.setText(categoriesDisplay[i]);
            chip.setTag(categoriesBackend[i]);
            chip.setCheckable(true);
            binding.chipGroupCategories.addView(chip);
        }

        // Chip click listener
        binding.chipGroupCategories.setOnCheckedChangeListener((group, checkedId) -> {
            binding.layoutOverview.setVisibility(View.GONE);
            binding.layoutSteps.setVisibility(View.GONE);
            binding.layoutNutrients.setVisibility(View.GONE);

            Chip selectedChip = group.findViewById(checkedId);
            if (selectedChip != null) {
                String tag = (String) selectedChip.getTag();
                switch (tag) {
                    case "overview":
                        binding.layoutOverview.setVisibility(View.VISIBLE);
                        break;
                    case "steps":
                        binding.layoutSteps.setVisibility(View.VISIBLE);
                        break;
                    case "nutrients":
                        binding.layoutNutrients.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
    }
}
