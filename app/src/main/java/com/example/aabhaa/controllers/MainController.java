package com.example.aabhaa.controllers;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.aabhaa.data.repository.CropRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainController {

    private static final String PREFS_NAME = "fab_position_prefs";
    private static final String KEY_X = "fab_x";
    private static final String KEY_Y = "fab_y";

    private final Activity activity;
    private float dX, dY;
    private int lastAction;

    // For drag detection
    private boolean dragStarted = false;
    private float startX, startY;

    private final WeatherController weatherController;


    public MainController(Activity activity ,WeatherController weatherController) {
        this.activity = activity;
        this.weatherController = weatherController;

    }

    @SuppressLint("ClickableViewAccessibility")
    public void initDraggableChatBot(FloatingActionButton fab, View header, View bottomNav) {
        positionFabAtBottomRight(fab);

        fab.setOnTouchListener((view, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    dragStarted = false;
                    startX = event.getRawX();
                    startY = event.getRawY();
                    onTouch(view, event);
                    return true; // consume

                case MotionEvent.ACTION_MOVE:
                    float dx = Math.abs(event.getRawX() - startX);
                    float dy = Math.abs(event.getRawY() - startY);
                    int dragThreshold = dpToPx(10);

                    if (dx > dragThreshold || dy > dragThreshold) {
                        dragStarted = true;
                    }

                    if (dragStarted) {
                        onTouch(view, event);
                        Log.d("SAB", "drag");
                        return true; // consume drag
                    } else {
                        return false; // don't consume, allow click detection
                    }

                case MotionEvent.ACTION_UP:
                    if (!dragStarted) {
                        Log.d("UP", "click");
                        view.performClick(); // forward click event
                        return false; // allow click listener to handle
                    } else {
                        Log.d("UP", "drag ended");
                        onTouch(view, event); // snap and save
                        return true; // consume event
                    }

                default:
                    return false;
            }
        });
    }

    private void snapToEdge(View v) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        float screenWidth = metrics.widthPixels;
        float margin = dpToPx(16);

        float finalX;
        if (v.getX() > screenWidth / 2) {
            finalX = screenWidth - v.getWidth() - margin;
        } else {
            finalX = margin;
        }
        float finalY = v.getY();
        v.animate().x(finalX).setDuration(200).withEndAction(() -> saveFabPosition(finalX, finalY)).start();
    }

    public void positionFabAtBottomRight(FloatingActionButton fab) {
        ViewTreeObserver.OnGlobalLayoutListener layoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                float[] savedPos = getSavedFabPosition();
                float x = savedPos[0];
                float y = savedPos[1];

                if (x >= 0 && y >= 0) {
                    fab.setX(x);
                    fab.setY(y);
                } else {
                    int marginPx = dpToPx(15);
                    int bottomLimitPx = dpToPx(60);

                    DisplayMetrics metrics = new DisplayMetrics();
                    activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
                    int screenWidth = metrics.widthPixels;
                    int screenHeight = metrics.heightPixels;

                    float posX = screenWidth - fab.getWidth() - marginPx;
                    float posY = screenHeight - fab.getHeight() - marginPx - bottomLimitPx;

                    fab.setX(posX);
                    fab.setY(posY);
                }

                fab.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        };

        fab.getViewTreeObserver().addOnGlobalLayoutListener(layoutListener);
    }

    private void saveFabPosition(float x, float y) {
        activity.getSharedPreferences(PREFS_NAME, Activity.MODE_PRIVATE)
                .edit()
                .putFloat(KEY_X, x)
                .putFloat(KEY_Y, y)
                .apply();
    }

    private float[] getSavedFabPosition() {
        var prefs = activity.getSharedPreferences(PREFS_NAME, Activity.MODE_PRIVATE);
        float x = prefs.getFloat(KEY_X, -1);
        float y = prefs.getFloat(KEY_Y, -1);
        return new float[]{x, y};
    }

    private int dpToPx(int dp) {
        float density = activity.getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    public boolean onTouch(View view, MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                dX = view.getX() - event.getRawX();
                dY = view.getY() - event.getRawY();
                lastAction = MotionEvent.ACTION_DOWN;
                return true;

            case MotionEvent.ACTION_MOVE:
                float newX = event.getRawX() + dX;
                float newY = event.getRawY() + dY;

                int topLimit = dpToPx(60);
                int bottomLimit = dpToPx(65);

                DisplayMetrics metrics = new DisplayMetrics();
                activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
                int screenHeight = metrics.heightPixels;

                float clampedY = Math.max(topLimit, Math.min(newY, screenHeight - bottomLimit - view.getHeight()));
                float clampedX = Math.max(0, Math.min(newX, metrics.widthPixels - view.getWidth()));

                view.setX(clampedX);
                view.setY(clampedY);

                lastAction = MotionEvent.ACTION_MOVE;
                return true;

            case MotionEvent.ACTION_UP:
                if (lastAction == MotionEvent.ACTION_DOWN) {
                    view.performClick(); // Accessibility
                }
                snapToEdge(view); // Snap and save

                view.postDelayed(() -> saveFabPosition(view.getX(), view.getY()), 250);

                return true;

            default:
                return false;
        }
    }

    public void checkAndRequestLocationPermission(Activity activity) {
        // Step 1: Check if permission is granted
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Step 2: Request permission
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1001);
        } else {
            // Step 3: Check if location is turned on
            LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
            boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean networkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!gpsEnabled && !networkEnabled) {
                // Step 4: Ask user to enable location settings
                new AlertDialog.Builder(activity)
                        .setTitle("Enable Location")
                        .setMessage("Location is required for weather data. Please enable GPS.")
                        .setPositiveButton("Open Settings", (dialog, which) -> {
                            activity.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            } else {
                // Everything is fine: fetch weather
                weatherController.fetchWeatherFromAPI();
            }
        }
    }



    }

