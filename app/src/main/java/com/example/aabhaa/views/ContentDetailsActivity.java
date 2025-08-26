package com.example.aabhaa.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.aabhaa.R;
import com.example.aabhaa.adapters.SuggestionAdapter;
import com.example.aabhaa.controllers.ContentDetailController;
import com.example.aabhaa.databinding.ActivityContentDetailsBinding;
import com.example.aabhaa.models.Content;

import java.util.ArrayList;
import java.util.List;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ContentDetailsActivity extends AppCompatActivity {

    private ActivityContentDetailsBinding binding;
    private ExoPlayer player;
    private View currentInnerView;
    private List<Content> fullContentList = new ArrayList<>(); // Full list passed from adapter

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MyApplication.updateLocale(newBase));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityContentDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get clicked content from Intent
        Content content = getIntent().getParcelableExtra("content_item");

        // Get the full list of contents from Intent
        ArrayList<Content> contentList = getIntent().getParcelableArrayListExtra("content_list");
        if (contentList != null) {
            fullContentList = contentList;
        }

        if (content != null) {
            inflateContent(content);
        } else {
            Log.w("ContentDetails", "No content received from Intent");
        }

        // Back button
        binding.backButton.btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("navigate_to", "explore");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }

    private void inflateContent(Content content) {
        // Remove old inner view
        binding.contentContainer.removeAllViews();

        // Inflate actual content layout
        View innerView = LayoutInflater.from(this)
                .inflate(R.layout.layout_content_detail_inner, binding.contentContainer, false);
        currentInnerView = innerView;

        // Apply fade + scale animation
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.scale_fade_in);
        innerView.startAnimation(animation);

        // Bind actual content data
        updateContentUI(innerView, content);

        // Add view to container
        binding.contentContainer.addView(innerView);
        Log.d("ContentDetails", "Content loaded dynamically from Intent");
    }

    private void updateContentUI(View view, Content content) {
        // Title, Description, Date, Category
        view.<android.widget.TextView>findViewById(R.id.tvTitle).setSelected(true);
        view.<android.widget.TextView>findViewById(R.id.tvTitle).setText(content.title_en);
        view.<android.widget.TextView>findViewById(R.id.tvDescription).setText(content.description_en);
        view.<android.widget.TextView>findViewById(R.id.tvDate).setText(formatDateTime(content.published_at));
        view.<android.widget.TextView>findViewById(R.id.tvCategory).setText(content.category);

        // Media views
        View playerView = view.findViewById(R.id.playerView);
        View imageView = view.findViewById(R.id.imageView);
        View audioContainer = view.findViewById(R.id.audioContainer);

        playerView.setVisibility(View.GONE);
        imageView.setVisibility(View.GONE);
        audioContainer.setVisibility(View.GONE);

        if (player != null) {
            player.release();
            player = null;
        }

        // Video
        if (content.video_url != null && !content.video_url.trim().isEmpty()) {
            playerView.setVisibility(View.VISIBLE);
            player = new ExoPlayer.Builder(this).build();
            ((androidx.media3.ui.PlayerView) playerView).setPlayer(player);
            player.setMediaItem(MediaItem.fromUri(content.video_url));
            player.prepare();
            player.play();

            // Image
        } else if (content.image_url != null && !content.image_url.trim().isEmpty()) {
            imageView.setVisibility(View.VISIBLE);
            try {
                int resId = Integer.parseInt(content.image_url);
                ((android.widget.ImageView) imageView).setImageResource(resId);
            } catch (NumberFormatException e) {
                Glide.with(this)
                        .load(content.image_url)
                        .placeholder(R.drawable.bg_otp_box_active)
                        .into((android.widget.ImageView) imageView);
            }

            // Audio
        } else if (content.audio_url != null && !content.audio_url.trim().isEmpty()) {
            audioContainer.setVisibility(View.VISIBLE);
            view.findViewById(R.id.btnPlayAudio).setOnClickListener(v -> {
                player = new ExoPlayer.Builder(this).build();
                player.setMediaItem(MediaItem.fromUri(content.audio_url));
                player.prepare();
                player.play();
            });
        }

// Suggestions based on current content category
        List<Content> filteredSuggestions = new ArrayList<>();
        for (Content c : fullContentList) {
            // Only show same category and not the current content
            if (c.category != null && c.category.equals(content.category) && c.id != content.id) {
                filteredSuggestions.add(c);
            }
        }

// Setup SuggestionAdapter
        SuggestionAdapter suggestionAdapter = new SuggestionAdapter(this, filteredSuggestions, suggestionView -> {
            Content clicked = (Content) suggestionView.getTag();
            inflateContent(clicked); // Dynamically load the clicked content
        });

// RecyclerView
        androidx.recyclerview.widget.RecyclerView rv = binding.rvYouMayAlsoLike;
        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rv.setAdapter(suggestionAdapter);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (player != null) {
            player.release();
            player = null;
        }
    }

    public String formatDateTime(String isoDate) {
        if (isoDate == null || isoDate.isEmpty()) return "";

        // Remove extra microseconds if present
        String cleaned = isoDate.replaceAll("\\.\\d{6}", ""); // removes .000000

        try {
            SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
            isoFormat.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
            Date date = isoFormat.parse(cleaned);

            // Desired display format: e.g., "Aug 24, 2025 5:50 AM"
            SimpleDateFormat displayFormat = new SimpleDateFormat("MMM dd, yyyy h:mm a", Locale.getDefault());
            return displayFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
            return isoDate; // fallback
        }
    }
}
