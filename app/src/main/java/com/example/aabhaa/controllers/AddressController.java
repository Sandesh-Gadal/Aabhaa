package com.example.aabhaa.controllers;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.aabhaa.databinding.ActivityAddressBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
public class AddressController {

    private final FragmentActivity activity;
    private ActivityAddressBinding binding;

    private GoogleMap smallMap;
    private GoogleMap fullMap;
    private FusedLocationProviderClient fusedLocationClient;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    public AddressController(FragmentActivity activity) {
        this.activity = activity;
        this.fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
    }

    public void setBinding(ActivityAddressBinding binding) {
        this.binding = binding;
    }

    public void initSmallMap() {
        SupportMapFragment mapFragment = (SupportMapFragment)
                activity.getSupportFragmentManager().findFragmentById(binding.map.getId());

        if (mapFragment != null) {
            mapFragment.getMapAsync(googleMap -> {
                smallMap = googleMap;
                smallMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                LatLng kathmandu = new LatLng(27.7172, 85.3240);
                smallMap.addMarker(new MarkerOptions().position(kathmandu).title("Small Map"));
                smallMap.moveCamera(CameraUpdateFactory.newLatLngZoom(kathmandu, 14));
            });
        }

        requestLocationPermission();
    }

    public void loadFullMap() {
        SupportMapFragment fullMapFragment = (SupportMapFragment)
                activity.getSupportFragmentManager().findFragmentById(binding.fullscreenMap.getId());

        if (fullMapFragment == null) {
            fullMapFragment = SupportMapFragment.newInstance();
            activity.getSupportFragmentManager().beginTransaction()
                    .replace(binding.fullscreenMap.getId(), fullMapFragment)
                    .commit();
        }

        fullMapFragment.getMapAsync(this::setupFullMap);
    }

    private void setupFullMap(GoogleMap googleMap) {
        fullMap = googleMap;
        
        setupMapUI(fullMap);
        centerInitialCamera();

        fullMap.setOnMapClickListener(latLng -> {
            fullMap.clear();
            fullMap.addMarker(new MarkerOptions().position(latLng).title("Selected Location"));

            // Update small map
            if (smallMap != null) {
                smallMap.clear();
                smallMap.addMarker(new MarkerOptions().position(latLng).title("Selected Location"));
                smallMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
            }

            // Update fields
            binding.etLatitude.setText(String.valueOf(latLng.latitude));
            binding.etLongitude.setText(String.valueOf(latLng.longitude));
            fetchAddress(latLng);
        });

        enableUserLocation(); // set blue dot + default location
    }

    private void centerInitialCamera() {
        String latStr = binding.etLatitude.getText().toString();
        String lonStr = binding.etLongitude.getText().toString();

        LatLng center = (!latStr.isEmpty() && !lonStr.isEmpty())
                ? new LatLng(Double.parseDouble(latStr), Double.parseDouble(lonStr))
                : new LatLng(27.7172, 85.3240); // Kathmandu

        fullMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center, 14));
        fullMap.addMarker(new MarkerOptions().position(center).title("Selected Location"));
    }

    private void setupMapUI(GoogleMap map) {


        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.getUiSettings().setCompassEnabled(true);
        map.getUiSettings().setMapToolbarEnabled(true);
    }

    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    public void handlePermissionResult(int requestCode, int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE &&
                grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            enableUserLocation();
        }
    }

    private void enableUserLocation() {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) return;

        if (fullMap != null) {
            fullMap.setMyLocationEnabled(true);
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());

                        fullMap.clear();
                        fullMap.addMarker(new MarkerOptions().position(currentLatLng).title("You are here"));
                        fullMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 16));

                        binding.etLatitude.setText(String.valueOf(currentLatLng.latitude));
                        binding.etLongitude.setText(String.valueOf(currentLatLng.longitude));
                        fetchAddress(currentLatLng);
                    }
                });
    }

    private void fetchAddress(LatLng latLng) {
        Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
        try {
            List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (!addressList.isEmpty()) {
                Address address = addressList.get(0);
                binding.acvCategoryProvince.setText(address.getAdminArea());     // Province
                binding.acvCategoryDistrict.setText(address.getSubAdminArea());  // District
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
