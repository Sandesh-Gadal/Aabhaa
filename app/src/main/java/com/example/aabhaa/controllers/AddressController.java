package com.example.aabhaa.controllers;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;

import android.location.Geocoder;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.aabhaa.adapters.AddressAdapter;
import com.example.aabhaa.data.repository.AddressRepository;
import com.example.aabhaa.databinding.ActivityAddAddressBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AddressController {

    private final FragmentActivity activity;
    private ActivityAddAddressBinding binding;

    private GoogleMap smallMap;
    private GoogleMap fullMap;
    private FusedLocationProviderClient fusedLocationClient;

    private TileOverlay smallMapTileOverlay;
    private TileOverlay fullMapTileOverlay;

    private Marker smallMapMarker;
    private Marker fullMapMarker;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    // Nepal bounding box constants
    private static final double NEPAL_WEST = 80.0;
    private static final double NEPAL_EAST = 88.2;
    private static final double NEPAL_SOUTH = 26.3;
    private static final double NEPAL_NORTH = 30.5;

    private final Context context;

    private final AddressRepository addressRepository;

    public AddressController(FragmentActivity activity , Context context) {
        this.context = context;
        this.activity = activity;
        this.fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
        this.addressRepository = new AddressRepository(context);
    }

    public void handleSaveAddress(
            TextInputEditText etAddresstitle,
            AutoCompleteTextView acvProvince,
            AutoCompleteTextView acvDistrict,
            TextInputEditText etLatitude,
            TextInputEditText etLongitude,
            TextInputEditText etDescription
    ) {
        boolean isValid = true;

        String title = etAddresstitle.getText().toString().trim();
        String province = acvProvince.getText().toString().trim();
        String district = acvDistrict.getText().toString().trim();
        String latStr = etLatitude.getText().toString().trim();
        String lonStr = etLongitude.getText().toString().trim();
        String description = etDescription.getText().toString().trim();

        if (title.isEmpty()) {
            etAddresstitle.setError("Required");
            isValid = false;
        }
        if (province.isEmpty()) {
            acvProvince.setError("Required");
            isValid = false;
        }
        if (district.isEmpty()) {
            acvDistrict.setError("Required");
            isValid = false;
        }
        if (latStr.isEmpty()) {
            etLatitude.setError("Required");
            isValid = false;
        }
        if (lonStr.isEmpty()) {
            etLongitude.setError("Required");
            isValid = false;
        }

        double latitude = 0, longitude = 0;
        try {
            latitude = Double.parseDouble(latStr);
            longitude = Double.parseDouble(lonStr);
        } catch (NumberFormatException e) {
            Toast.makeText(context, "Invalid coordinates", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        if (!isValid) return;

        com.example.aabhaa.models.Address address = new com.example.aabhaa.models.Address(
                -1 , title, province, district, latitude, longitude, description
        );

        addressRepository.sendAddress(address , context);
    }


    public void setBinding(ActivityAddAddressBinding binding) {
        this.binding = binding;
    }

    public void initSmallMap() {
        SupportMapFragment mapFragment = (SupportMapFragment)
                activity.getSupportFragmentManager().findFragmentById(binding.map.getId());

        if (mapFragment != null) {
            mapFragment.getMapAsync(googleMap -> {
                smallMap = googleMap;
                smallMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                smallMap.getUiSettings().setZoomControlsEnabled(true);
                LatLng kathmandu = new LatLng(27.7172, 85.3240);
                smallMapMarker = smallMap.addMarker(new MarkerOptions().position(kathmandu).title("Small Map"));
                smallMap.moveCamera(CameraUpdateFactory.newLatLngZoom(kathmandu, 14));
                addSoilMapWMSForSmallMap();
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
                    .commitNow();
        }

        fullMapFragment.getMapAsync(this::setupFullMap);
    }

    private void setupFullMap(GoogleMap googleMap) {
        fullMap = googleMap;
        fullMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        setupMapUI(fullMap);
        addSoilMapWMSForFullMap();
        centerInitialCamera();

        fullMap.setOnCameraIdleListener(() -> {
            if (fullMapTileOverlay != null) {
                fullMapTileOverlay.clearTileCache();
            }
        });

        fullMap.setOnMapClickListener(latLng -> {
            if (fullMapMarker != null) fullMapMarker.remove();
            fullMapMarker = fullMap.addMarker(new MarkerOptions().position(latLng).title("Selected Location"));

            if (smallMap != null) {
                if (smallMapMarker != null) smallMapMarker.remove();
                smallMapMarker = smallMap.addMarker(new MarkerOptions().position(latLng).title("Selected Location"));
                smallMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
            }

            binding.etLatitude.setText(String.valueOf(latLng.latitude));
            binding.etLongitude.setText(String.valueOf(latLng.longitude));
            fetchAddress(latLng);
        });

        enableUserLocation();
    }

    private void centerInitialCamera() {
        String latStr = binding.etLatitude.getText().toString();
        String lonStr = binding.etLongitude.getText().toString();

        LatLng center = (!latStr.isEmpty() && !lonStr.isEmpty())
                ? new LatLng(Double.parseDouble(latStr), Double.parseDouble(lonStr))
                : new LatLng(27.7172, 85.3240);

        fullMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center, 14));
        fullMapMarker = fullMap.addMarker(new MarkerOptions().position(center).title("Selected Location"));
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

        if (fullMap != null) fullMap.setMyLocationEnabled(true);

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());

                        if (fullMapMarker != null) fullMapMarker.remove();
                        fullMapMarker = fullMap.addMarker(new MarkerOptions().position(currentLatLng).title("You are here"));
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
                binding.acvCategoryProvince.setText(address.getAdminArea());
                binding.acvCategoryDistrict.setText(address.getSubAdminArea());

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addSoilMapWMSForSmallMap() {
        if (smallMap == null) return;

        TileProvider tileProvider = createSoilTileProvider();
        if (smallMapTileOverlay != null) smallMapTileOverlay.remove();
        smallMapTileOverlay = smallMap.addTileOverlay(new TileOverlayOptions().tileProvider(tileProvider));
    }

    private void addSoilMapWMSForFullMap() {
        if (fullMap == null) return;

        TileProvider tileProvider = createSoilTileProvider();
        if (fullMapTileOverlay != null) fullMapTileOverlay.remove();
        fullMapTileOverlay = fullMap.addTileOverlay(new TileOverlayOptions().tileProvider(tileProvider));
    }

    private TileProvider createSoilTileProvider() {
        return new UrlTileProvider(256, 256) {
            @Override
            public URL getTileUrl(int x, int y, int zoom) {
                double[] bbox = getBoundingBox(x, y, zoom);
                if (!intersects(bbox, new double[]{NEPAL_WEST, NEPAL_SOUTH, NEPAL_EAST, NEPAL_NORTH})) {
                    return null;
                }

                String url = String.format(Locale.US,
                        "https://soil.narc.gov.np/geoserver/dsm/wms?" +
                                "SERVICE=WMS&VERSION=1.1.1&REQUEST=GetMap&FORMAT=image/png&TRANSPARENT=true" +
                                "&LAYERS=dsm:soilsample&STYLES=&SRS=EPSG:4326&WIDTH=256&HEIGHT=256&BBOX=%f,%f,%f,%f",
                        bbox[0], bbox[1], bbox[2], bbox[3]);

                Log.d("WMS_TileURL", url);

                try {
                    return new URL(url);
                } catch (MalformedURLException e) {
                    return null;
                }
            }

            private boolean intersects(double[] bbox1, double[] bbox2) {
                return !(bbox1[2] < bbox2[0] || bbox1[0] > bbox2[2] || bbox1[3] < bbox2[1] || bbox1[1] > bbox2[3]);
            }

            private double[] getBoundingBox(int x, int y, int zoom) {
                double numTiles = Math.pow(2, zoom);
                double lonPerTile = 360.0 / numTiles;
                double lonLeft = -180 + x * lonPerTile;
                double lonRight = -180 + (x + 1) * lonPerTile;
                double latTop = tile2lat(y, zoom);
                double latBottom = tile2lat(y + 1, zoom);
                return new double[]{lonLeft, latBottom, lonRight, latTop};
            }

            private double tile2lat(int y, int zoom) {
                double n = Math.PI - (2.0 * Math.PI * y) / Math.pow(2, zoom);
                return Math.toDegrees(Math.atan(Math.sinh(n)));
            }
        };
    }
    public void removefullMapMarker(){
        if (fullMapMarker != null) {
            fullMapMarker.remove();
            fullMapMarker = null;
        }
    }


    public void fetchAddressIntoSpinner(Context context, Spinner spinner, AddressFetchCallback addressFetchCallback) {
        addressRepository.fetchAddresses(context, new AddressFetchCallback() {

            @Override
            public void onAddressesFetched(List<com.example.aabhaa.models.Address> addressList) {
                if (addressList == null || addressList.isEmpty()) {
                    return;
                }

                List<String> spinnerItems = new ArrayList<>();
                for (com.example.aabhaa.models.Address userAddress : addressList) {
                    spinnerItems.add(userAddress.getSpinnerAddress());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, spinnerItems);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);

                // Let activity know about the full list
                addressFetchCallback.onAddressesFetched(addressList);

            }
        });
    }

    public void populateAddressRecyclerView(Context context, RecyclerView recyclerView) {
        addressRepository.fetchAddresses(context, new AddressFetchCallback() {
            @Override
            public void onAddressesFetched(List<com.example.aabhaa.models.Address> addressList) {
                if (addressList == null || addressList.isEmpty()) {

                    Toast.makeText(context, "No address to show", Toast.LENGTH_SHORT).show();
                    return;
                }

                AddressAdapter adapter = new AddressAdapter(context, addressList);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                recyclerView.setAdapter(adapter);
            }
        });
    }




}
