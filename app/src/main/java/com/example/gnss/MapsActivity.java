package com.example.gnss;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import android.widget.Toast;
import android.content.Context;

// AppCompatActivity - provides ActionBar at the top.
// FragmentActivity - doesn't :)
public class MapsActivity extends AppCompatActivity
        implements OnMapReadyCallback {

    public static final int PERMISSIONS_ACCESS_FINE_LOCATION = 1;
    public static final int PERMISSIONS_ACCESS_COARSE_LOCATION = 2;
    public static final int PERMISSIONS_WRITE_EXTERNAL_STORAGE = 3;

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Toolbar myToolbar = findViewById(R.id.maps_toolbar);
        //setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle("Gdzie browar?");
            // getSupportActionBar().setHomeButtonEnabled(true);
            // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // OK, let's deal with the stupid permissions first.
        checkPerm(Manifest.permission.ACCESS_FINE_LOCATION, PERMISSIONS_ACCESS_FINE_LOCATION);
        checkPerm(Manifest.permission.ACCESS_COARSE_LOCATION, PERMISSIONS_ACCESS_COARSE_LOCATION);
        checkPerm(Manifest.permission.WRITE_EXTERNAL_STORAGE, PERMISSIONS_WRITE_EXTERNAL_STORAGE);

        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch (item.getItemId())
        {
            case R.id.menu_map:
                Toast.makeText(this, "Mapa", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.menu_stores:
                Toast.makeText(this, "Sklepy", Toast.LENGTH_SHORT).show();
                launchPoiActivity();
                return true;

            case R.id.menu_gps:
                Toast.makeText(this, "GPS", Toast.LENGTH_SHORT).show();
                launchGpsActivity();
                return true;

            case R.id.menu_about:
                Toast.makeText(this, "About", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void checkPerm(java.lang.String perm, int requestCode) {
        if (ActivityCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {perm}, requestCode);
            return;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        String txt = "Nothing...";
        switch (requestCode) {
            case PERMISSIONS_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    txt = "Fine location granted!";
                } else {
                    txt = "Fine location rejected. Doh :(";
                }
                break;
            }
            case PERMISSIONS_ACCESS_COARSE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    txt = "Coarse location granted!";
                } else {
                    txt = "Coarse location rejected. Doh :(";
                }

                break;
            }
            case PERMISSIONS_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    txt = "Write external storage granted!";
                } else {
                    txt = "Write external storage rejected. Doh :(";
                }
                break;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }

        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, txt, duration);
        toast.show();

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

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }

        addMarkers();

        // Zoom in on Wrzeszcz
        LatLng last_pos = new LatLng(54.3725, 18.6138);
        mMap.moveCamera((CameraUpdateFactory.newLatLng(last_pos)));
    }

    private void addMarkers() {
        final ArrayList<Poi> pois = PoiList.getInstance().pois_;

        for (Poi poi : pois) {
            LatLng pos = new LatLng(poi.lat, poi.lon);
            mMap.addMarker(new MarkerOptions().position(pos).title(poi.descr));
        }
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
