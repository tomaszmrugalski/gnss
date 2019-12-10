package com.example.gnss;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

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

        Button bt1 = (Button) findViewById(R.id.button_stores);
        bt1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                launchPoiActivity();
            }
        });

        Button bt2 = (Button) findViewById(R.id.button_gps);
        bt2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                launchGpsActivity();
            }
        });

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

        addMarkers();
    }

    private void addMarkers() {
        final ArrayList<Poi> pois = PoiList.getInstance().pois_;

        LatLng last_pos = new LatLng(54, 18);
        for (Poi poi : pois) {
            LatLng pos = new LatLng(poi.lat, poi.lon);
            mMap.addMarker(new MarkerOptions().position(pos).title(poi.descr));
            last_pos = pos;
        }
        mMap.moveCamera((CameraUpdateFactory.newLatLng(last_pos)));
    }

    private void launchPoiActivity() {
        Intent intent = new Intent(this, PoiActivity.class);
        startActivity(intent);
    }

    private void launchGpsActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
