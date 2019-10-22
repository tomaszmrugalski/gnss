package com.example.gnss;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private LocationManager lm;
    private ThGpsListener gpsListener;
    private ThNMEAListener nmeaListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lm = (LocationManager) getSystemService(LOCATION_SERVICE);

        gpsListener = new ThGpsListener();
        gpsListener.tv = findViewById(R.id.tv);
        gpsListener.debug = findViewById(R.id.debug);

        nmeaListener = new ThNMEAListener();
        nmeaListener.debug = findViewById(R.id.debug);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},0);
            return;
        }

        registerListener();

        Button bt1 = (Button) findViewById(R.id.button1);
        bt1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                gpsListener.tv.setText("Click!");
            }
        });
    }

    protected void registerListener() {
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, gpsListener);

        lm.addNmeaListener(nmeaListener);
    }
};
