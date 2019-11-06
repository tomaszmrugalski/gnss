package com.example.gnss;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;
import android.content.SharedPreferences;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Date;

import static android.location.Location.FORMAT_SECONDS;

public class ThGpsListener implements LocationListener {

    public TextView tv;
    public TextView debug;
    public SharedPreferences.Editor pref;

    @Override
    public void onLocationChanged(Location location) {
        przetwarzajLokalizacje(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // debug.append("onStatusChanged ");
    }

    @Override
    public void onProviderEnabled(String provider) {
        // debug.append("onProviderEnabled ");
    }

    @Override
    public void onProviderDisabled(String provider) {
        // debug.append("onProviderDisabled ");
    }

    Location prevLocation = null;

    private void przetwarzajLokalizacje(Location location) {
        // debug.append(". ");
        String info;
        info = "Lat:" + location.convert(location.getLatitude(), FORMAT_SECONDS) +
                " Long:" + location.convert(location.getLongitude(), FORMAT_SECONDS);


        if (prevLocation != null) {
            float bearing = prevLocation.bearingTo(location);
            float distance = prevLocation.distanceTo(location);
            info += "\nDist:" + distance + " Azimuth:" + bearing;
        }

        if (location.hasSpeed()) {
            info += "\nSpeed:" + location.getSpeed() + " [m/s]";
        }

        if (location.hasAltitude()) {
            info += "\nAltitude: " + location.getAltitude() + " [mals]";
        }

        if (location.hasAccuracy()) {
            info += "\nAccuracy: " + location.getAccuracy() + " [m] ";
        }

        Date d;
        d = new Date(location.getTime());

        // Write the contents to a location.txt file
        writeToFile("location.txt", d.toString() + ": " + info);


        info = "" + d.toString() + "\n" + info + "\n" + "Nearest POI: " + getNearestPoi(location);
        tv.setText(info);
        prevLocation = location;

        savePreferences(location);
    }

    String getNearestPoi(Location l) {
        Poi nearest = null;
        Double dist = 40000000.0;
        for (Poi p : PoiList.getInstance().pois_) {
            if (nearest == null) {
                nearest = p;
                dist = dist(l.getLongitude(), l.getLatitude(), p.lon, p.lat);
                continue;
            }
            Double tmp = dist(l.getLongitude(), l.getLatitude(), p.lon, p.lat);
            if (tmp < dist) {
                dist = tmp;
                nearest = p;
                continue;
            }
        }

        return nearest.getText() + " (" + String.format("%4.3f", dist) + "km)";
    }

    Double degreesToRadians(Double degrees) {
        return degrees * Math.PI / 180;
    }

    Double dist(double long1, double lat1, double long2, double lat2) {

        Double earthRadiusKm = 6371.0;

        Double dLat = degreesToRadians(lat2 - lat1);
        Double dLon = degreesToRadians(long2 - long1);

        lat1 = degreesToRadians(lat1);
        lat2 = degreesToRadians(lat2);

        Double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadiusKm * c;
    }

    private void writeToFile(String filename, String txt) {
        File fex = Environment.getExternalStorageDirectory();
        fex = new File(fex, filename);
        FileOutputStream fos = null;
        try {
            boolean append = true;
            fos = new FileOutputStream(fex, append);
        } catch (FileNotFoundException ex) {
            //deal with the problem
            Log.e("ERR","writeToFile exception: " + ex);
            return;
        }
        PrintWriter pw = new PrintWriter(fos);
        pw.println(txt);
        pw.close();
    }

    void savePreferences(Location l) {
        pref.putString("lat", String.format("%2.4f", l.getLatitude()));
        pref.putString("long", String.format("%2.4f", l.getLongitude()));

    }

}

