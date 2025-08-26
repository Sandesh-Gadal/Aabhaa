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

public class CropDetailsActivity extends AppCompatActivity {

    private ActivityCropDetailsBinding binding;

    private CropController cropController;
    private CropRepository cropRepository;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MyApplication.updateLocale(newBase));
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCropDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // ✅ Get crop from intent
        Crop crop = (Crop) getIntent().getParcelableExtra("crop_item");
        if (crop != null) {
            populateCropDetails(crop);
        }
        RecyclerView recyclerView = binding.rvYouMayAlsoLike;
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        CropRepository cropRepository = new CropRepository(this);

        CropController cropController = new CropController(recyclerView, cropRepository,    crop1 -> {
            // Activity context is valid
            Intent intent = new Intent(this, CropDetailsActivity.class);
            intent.putExtra("crop_item", crop1);
            startActivity(intent);
            finish();
        });
        cropController.fetchCropsBySeason();




        // 🔁 Chip group toggling
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

    private void populateCropDetails(Crop crop) {
        // ✅ Image
        Glide.with(this)
                .load(crop.getImage_url())
                .placeholder(R.drawable.bg_wheat)
                .into(binding.profileImage);

        // ✅ Name
        binding.cropName.setText(crop.getName() + " | " + crop.getName());

        // ✅ Overview
        binding.tvRichDescription.setText(crop.getDescription_en());
        ((TextView) findViewById(R.id.tvDuration)).setText(
                crop.getDuration_days_min() + " - " + crop.getDuration_days_max() + " Days"
        );
        ((TextView) findViewById(R.id.tvPHLevel)).setText(
                crop.getPh_min() + " - " + crop.getPh_max()
        );
        ((TextView) findViewById(R.id.tvTemperature)).setText(
                crop.getTemp_min_c() + "°C - " + crop.getTemp_max_c() + "°C"
        );

        binding.layoutSteps.removeAllViews();

        if (crop.getSteps() != null && !crop.getSteps().isEmpty()) {
            View stepView = LayoutInflater.from(this)
                    .inflate(R.layout.item_step, binding.layoutSteps, false);

            // Set HTML formatted text if needed
            TextView tvSteps = stepView.findViewById(R.id.tvSteps);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                tvSteps.setText(Html.fromHtml(crop.getSteps(), Html.FROM_HTML_MODE_LEGACY));
            } else {
                tvSteps.setText(Html.fromHtml(crop.getSteps()));
            }

            binding.layoutSteps.addView(stepView);
        }





        // ✅ Nutrients (individual fields)
        binding.layoutNutrients.removeAllViews();
        View nutrientView = LayoutInflater.from(this)
                .inflate(R.layout.layout_nutrient_info, binding.layoutNutrients, false);
        ((TextView) nutrientView.findViewById(R.id.tvNitrogen)).setText(crop.getN_min_kg() + " kg/ha");
        ((TextView) nutrientView.findViewById(R.id.tvPhosphorus)).setText(crop.getP_min_kg() + " kg/ha");
        ((TextView) nutrientView.findViewById(R.id.tvPotassium)).setText(crop.getK_min_kg() + " kg/ha");
        binding.layoutNutrients.addView(nutrientView);
    }

}
