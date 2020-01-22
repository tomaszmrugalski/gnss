package com.example.gnss;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;

import org.json.JSONObject;

// AppCompatActivity - provides ActionBar at the top.
// FragmentActivity - doesn't :)
public class MapsActivity extends AppCompatActivity
        implements OnMapReadyCallback {

    public static final int PERMISSIONS_ACCESS_FINE_LOCATION = 1;
    public static final int PERMISSIONS_ACCESS_COARSE_LOCATION = 2;
    public static final int PERMISSIONS_WRITE_EXTERNAL_STORAGE = 3;

    private LocationManager lm_;
    private ThGpsListener gpsListener_;

    ProgressDialog progressDialog_;

    private MarkerOptions source_;
    private MarkerOptions dest_;

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

        lm_ = (LocationManager) getSystemService(LOCATION_SERVICE);
        gpsListener_ = new ThGpsListener();
        gpsListener_.tv = findViewById(R.id.tv);
        registerListener();
    }

    private void navigate() {

        if (dest_ == null) {
            Toast.makeText(this, "Cel nie wybrany", Toast.LENGTH_SHORT).show();

            return;
        }

        Location l = gpsListener_.prevLocation;
        if (l == null) {
            Toast.makeText(this, "GPS nie odbiera.", Toast.LENGTH_SHORT).show();
            return;
        }
            mMap.clear();
        addMarkers();

        String t = String.format("Navigate from %4.2f:%4.2f", l.getLongitude(), l.getLatitude());
        Toast.makeText(this, t, Toast.LENGTH_LONG).show();

        LatLng pos = new LatLng(l.getLatitude(), l.getLongitude());
        source_ = new MarkerOptions().position(pos).title("Start!");
        source_.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

        // Add our starting position.
        mMap.addMarker(source_);

        if (source_ == null){
            source_ = dest_;
        }
        mMap.addMarker(dest_);


        //source_ = new MarkerOptions()

        progressDialog_ = new ProgressDialog(MapsActivity.this);
        progressDialog_.setMessage("Please Wait, Polyline between two locations is building.");
        progressDialog_.setCancelable(false);
        progressDialog_.show();

        // Checks, whether start and end locations are captured
        // Getting URL to the Google Directions API
        String url = getDirectionsUrl(source_.getPosition(), dest_.getPosition());
        Log.d("url", url + "");
        DownloadTask downloadTask = new DownloadTask();
        // Start downloading json data from Google Directions API
        downloadTask.execute(url);
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
            case R.id.navigate:
                navigate();
                return true;

            case R.id.menu_map:
                Toast.makeText(this, "Mapa", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.menu_stores:
                launchPoiActivity();
                return true;

            case R.id.menu_gps:
                launchGpsActivity();
                return true;

            case R.id.menu_about:
                launchAboutActivity();
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

    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            String data = "";

            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();


            parserTask.execute(result);

        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {

            progressDialog_.dismiss();
            Log.i("result", result.toString());
            ArrayList points = null;
            PolylineOptions lineOptions = null;

            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList();
                lineOptions = new PolylineOptions();

                List<HashMap<String, String>> path = result.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                lineOptions.addAll(points);
                lineOptions.width(12);
                lineOptions.color(Color.RED);
                lineOptions.geodesic(true);

            }

// Drawing polyline in the Google Map for the i-th route
            if (lineOptions != null) {
                mMap.addPolyline(lineOptions);
            }
        }
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

        // Install our own display window adapter
        mMap.setInfoWindowAdapter(new MyInfoWindowAdapter());
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=driving";
        String key = "key=";
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode + "&" + key;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();
            Log.d("data", data);

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }


    int getCurrentHours() {

        // for debugging purposes
        return 910; // 9:10 am;

        // Calendar c = Calendar.getInstance();
        // return c.get(Calendar.HOUR_OF_DAY)*100 + c.get(Calendar.MINUTE);
    }

    Poi.Day getCurrentDayOfWeek() {
        Calendar c = Calendar.getInstance();
        int d = c.get(Calendar.DAY_OF_WEEK);
        if ( (Calendar.MONDAY <= d) && (d <= Calendar.FRIDAY) ) {
            return Poi.Day.MONFRI;
        } else if (d == Calendar.SATURDAY) {
            return Poi.Day.SAT;
        } else {
            return Poi.Day.SUN;
        }
    }

    private void addMarkers() {
        final ArrayList<Poi> pois = PoiList.getInstance().pois_;

        getCurrentDayOfWeek();

        for (Poi poi : pois) {
            LatLng pos = new LatLng(poi.lat, poi.lon);
            MarkerOptions m = new MarkerOptions().position(pos).title(poi.descr);
            m.position(pos);
            m.title(poi.descr);
            m.snippet(poi.getTextHours());

            if (poi.open(getCurrentDayOfWeek(), getCurrentHours())) {
                m.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            } else {
                m.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            }
            mMap.addMarker(m);
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

    private void launchAboutActivity() {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    /** Demonstrates customizing the info window and/or its contents. */
    class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private final View myContentsView;

        MyInfoWindowAdapter(){
            myContentsView = getLayoutInflater().inflate(R.layout.marker_info, null);
        }

        @Override
        public View getInfoContents(Marker marker) {

            TextView tvTitle = ((TextView)myContentsView.findViewById(R.id.title));
            SpannableString titleText = new SpannableString(marker.getTitle());
            titleText.setSpan(new ForegroundColorSpan(Color.RED), 0, titleText.length(), 0);
            tvTitle.setText(titleText);
            TextView tvSnippet = ((TextView)myContentsView.findViewById(R.id.snippet));
            tvSnippet.setText(marker.getSnippet());

            return myContentsView;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            // TODO Auto-generated method stub
            dest_ = new MarkerOptions().position(marker.getPosition());
            String t = "Dest:" + String.format("%6.4f, %6.4f", marker.getPosition().longitude, marker.getPosition().latitude);
            //.makeText(this, t, Toast.LENGTH_LONG).show();
            return null;
        }

    }

    protected void registerListener() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            lm_.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, gpsListener_);
        }
    }

}
