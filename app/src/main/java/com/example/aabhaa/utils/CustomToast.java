package com.example.aabhaa.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.aabhaa.R;

public class CustomToast {

    private static View currentToastView;

    public static void showToast(Context context, @Nullable Integer iconResId, String message) {
        // Get root view of the activity to add toast-like view
        ViewGroup rootView = (ViewGroup) ((Activity) context).getWindow().getDecorView();


        // If another toast is showing, remove it immediately
        if (currentToastView != null) {
            rootView.removeView(currentToastView);
            currentToastView = null;
        }

        // Inflate custom toast layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.custom_toast_notification, rootView, false);

        // Set layout params with margins
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        int margin = (int) (16 * context.getResources().getDisplayMetrics().density); // 16dp margin
        params.setMargins(margin, 0, margin, 0);
        layout.setLayoutParams(params);

        ImageView toastIcon = layout.findViewById(R.id.toast_icon);
        TextView toastMessage = layout.findViewById(R.id.toast_message);

        toastMessage.setText(message);

        if (iconResId != null) {
            toastIcon.setImageResource(iconResId);
            toastIcon.setVisibility(View.VISIBLE);
        } else {
            // Keep default icon if iconResId == null or optionally hide it
            toastIcon.setVisibility(View.VISIBLE); // or View.GONE if you want no icon
        }

        // Start with the toast view above the screen (negative translationY)
        layout.setTranslationY(-500f); // start off-screen above

        // Add the toast view to root layout
        rootView.addView(layout);
        currentToastView = layout;

        // Animate slide down
        layout.animate()
                .translationY(110)
                .setDuration(300)
                .setListener(null)
                .start();

        // Schedule slide up and removal after 3 seconds
        layout.postDelayed(() -> {
            layout.animate()
                    .translationY(-layout.getHeight())
                    .setDuration(300)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            rootView.removeView(layout);
                            currentToastView = null;
                        }
                    })
                    .start();
        }, 2000);
    }
}
