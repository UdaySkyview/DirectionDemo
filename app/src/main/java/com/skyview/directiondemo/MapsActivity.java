package com.skyview.directiondemo;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.skyview.directiondemo.databinding.ActivityMapsBinding;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private final static int LOCATION_REQUEST_CODE = 23;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private MarkerOptions markerOptions;
    private Marker marker;
    private LatLng currentLatLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initViews();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        Objects.requireNonNull(mapFragment).getMapAsync(this);

        requestPermission();
        locationOn();

    }

    private void initViews() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        markerOptions = new MarkerOptions();
        initLocationCallBack();
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        if (checkLocationEnabled()) {

            getCurrentLocation();

            getLiveLocation();

        }
    }

    private void requestPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_REQUEST_CODE);
        }
    }

    public void locationOn() {
        if (!checkLocationEnabled()) {
            new AlertDialog.Builder(this)
                    .setTitle("Location")
                    .setMessage("Turn on Location")
                    .setPositiveButton(android.R.string.yes, (dialogInterface, i) -> startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
                    .setNegativeButton(android.R.string.no, null)
                    .show();
        }
    }

    private boolean checkLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return true;
        }
        return false;
    }


    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        if (checkLocationEnabled()) {

            Task<Location> location = fusedLocationProviderClient.getLastLocation();
            location.addOnSuccessListener((Location location1) -> {
                marker = mMap.addMarker(markerOptions
                        .position(new LatLng(location1.getLatitude(), location1.getLongitude())));
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                        new LatLng(location1.getLatitude(), location1.getLongitude()), 16f);
                mMap.animateCamera(cameraUpdate);
            });
        }
    }

    private void initLocationCallBack() {
        //System.out.println("called===============================318");
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull @NotNull LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                //System.out.println("called===============================325");
                Location location = locationResult.getLastLocation();
                if (location != null) {
                    if (marker != null) {
                        marker.remove();
                    }
                    currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                    marker = mMap.addMarker(markerOptions
                            .position(currentLatLng));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng));
                }
            }

            @Override
            public void onLocationAvailability(@NonNull @NotNull LocationAvailability locationAvailability) {
                super.onLocationAvailability(locationAvailability);
            }
        };
    }

    private void getLiveLocation() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationRequest.setInterval(3000);
        locationRequest.setFastestInterval(1000);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, getMainLooper());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case LOCATION_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (checkLocationEnabled()) {
                        getCurrentLocation();
                    }
                }
                return;
            }
        }
    }
}