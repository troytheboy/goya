package com.example.chowi.goya;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng uw = new LatLng(47.655548, -122.303200);
        LatLng uw2 = new LatLng(47.655248, -122.303200);
        LatLng uw3 = new LatLng(47.655548, -122.303000);
        mMap.addMarker(new MarkerOptions().position(uw).title("Big bird!").snippet("Hey I saw a bald eagle over here come check it out!"));
        mMap.addMarker(new MarkerOptions().position(uw2).title("Sunset!").snippet("Awesome view of the sunset from over here"));
        mMap.addMarker(new MarkerOptions().position(uw3).title("Free Donuts!").snippet("I ordered too many donuts, who wants some?"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(uw));
    }
}
