package com.example.gnss;

public class Poi {
    public Poi(double lon, double lat, String descr) {
        this.lon = lon;
        this.lat = lat;
        this.descr = descr;
    }

    public String getText() {
        String t = descr + " " + String.format("%.5f %s,%.5f%s", lon, (lon>0?"E":"W"), lat, (lat>0?"N":"S"));
        return t;
    }
    public double lon;
    public double lat;
    public String descr;
};
