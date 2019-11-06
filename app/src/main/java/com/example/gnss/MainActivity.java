package com.example.gnss;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private LocationManager lm;
    private ThGpsListener gpsListener;

    private ThNMEAListener nmeaListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the location manager
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);

        String MY_PREFS_NAME = "prefs.txt";
        SharedPreferences.Editor pref = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();

        gpsListener = new ThGpsListener();
        gpsListener.tv = findViewById(R.id.tv);
        gpsListener.debug = findViewById(R.id.nmea);
        gpsListener.pref = pref;


        TextView nmea = findViewById(R.id.nmea);
        nmea.setMovementMethod(new ScrollingMovementMethod());

        nmeaListener = new ThNMEAListener();
        nmeaListener.debug = nmea;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION},0);
            return;
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
            requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
            return;
        }

        registerListener();

        Button bt1 = (Button) findViewById(R.id.button1);
        bt1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                launchPoi();
            }
        });
    }

    private void launchPoi() {
        Intent intent = new Intent(this, PoiActivity.class);
        startActivity(intent);
    }

    protected void registerListener() {
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, gpsListener);

        lm.addNmeaListener(nmeaListener);
    }


};
