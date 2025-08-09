package com.example.aabhaa.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
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
import com.example.aabhaa.data.StaticContentProvider;
import com.example.aabhaa.databinding.ActivityContentDetailsBinding;
import com.example.aabhaa.models.Content;
import com.example.aabhaa.utils.CustomToast;

import java.util.List;

public class ContentDetailsActivity extends AppCompatActivity {

    private ActivityContentDetailsBinding binding;
    private ExoPlayer player;
    private View currentInnerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityContentDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Content content = (Content) getIntent().getSerializableExtra("content_item");

        if (content != null) {
            inflateContent(content);
        }
        CustomToast.showToast(this,null,"Welcome Back");

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

        // Inflate actual content layout (without shimmer)
        View innerView = LayoutInflater.from(this).inflate(R.layout.layout_content_detail_inner, binding.contentContainer, false);
        currentInnerView = innerView;

        // Apply fade + slide animation
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.scale_fade_in);
        innerView.startAnimation(animation);

        // Bind actual content data
        updateContentUI(innerView, content);

        // Add real content view to container
        binding.contentContainer.addView(innerView);
        Log.d("ContentDetails", "Content loaded without shimmer");
    }


    private void updateContentUI(View view, Content content) {
        // Title, Description, Date
        view.findViewById(R.id.tvTitle).setSelected(true);
        view.<android.widget.TextView>findViewById(R.id.tvTitle).setText(content.title_en);
        view.<android.widget.TextView>findViewById(R.id.tvDescription).setText(content.description_en);
        view.<android.widget.TextView>findViewById(R.id.tvDate).setText(content.published_at);
        view.<android.widget.TextView>findViewById(R.id.tvCategory).setText(content.category);

        // Media logic
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

        if (content.video_url != null && !content.video_url.trim().isEmpty()) {
            // Show video
            playerView.setVisibility(View.VISIBLE);
            player = new ExoPlayer.Builder(this).build();
            ((androidx.media3.ui.PlayerView) playerView).setPlayer(player);
            player.setMediaItem(MediaItem.fromUri(content.video_url));
            player.prepare();
            player.play();
        } else if (content.image_url != null && !content.image_url.trim().isEmpty()) {
            // Show image
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
        } else if (content.audio_url != null && !content.audio_url.trim().isEmpty()) {
            // Show audio
            audioContainer.setVisibility(View.VISIBLE);
            view.findViewById(R.id.btnPlayAudio).setOnClickListener(v -> {
                player = new ExoPlayer.Builder(this).build();
                player.setMediaItem(MediaItem.fromUri(content.audio_url));
                player.prepare();
                player.play();
            });
        }

        // Suggestions
        ContentDetailController controller = new ContentDetailController();
        List<Content> allContents = StaticContentProvider.getAllExploreContents();
        List<Content> filteredSuggestions = controller.getSuggestionsByType(allContents, content.content_type, content.id);

        SuggestionAdapter suggestionAdapter = new SuggestionAdapter(this, filteredSuggestions, suggestionView -> {
            Content clicked = (Content) suggestionView.getTag();
            inflateContent(clicked); // Re-inflate with new content (no full reload)
        });

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
}
