package com.example.gnss;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Date;

public class ThGpsListener implements LocationListener {

    public TextView tv;
    public TextView debug;

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

    Location prevLocation= null;

    private void przetwarzajLokalizacje(Location location) {
        // debug.append(". ");
        String info;
        info = location.getLatitude()+ " " + location.getLongitude();
        if(prevLocation!=null){
            float bearing= prevLocation.bearingTo(location);
            float distance= prevLocation.distanceTo(location);
            info+= " "+distance+" "+bearing;
        }

        Date d;
        d = new Date(location.getTime());
        info = "" + d.toString() + "\n" + info;
        tv.setText(info);
        prevLocation= location;
    }
}

