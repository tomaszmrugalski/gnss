package com.example.gnss;

import java.lang.reflect.Array;
import java.util.ArrayList;

class PoiList {
    private static final PoiList ourInstance = new PoiList();

    public static ArrayList<Poi> pois_;

    static PoiList getInstance() {
        return ourInstance;
    }

    private PoiList() {
        pois_ = new ArrayList<Poi>();

        // Come up with some better fabricated data!
        pois_.add(new Poi(18.1, 54.9, "Pkt 1"));
        pois_.add(new Poi(18.2, 54.8, "Pkt 2"));
        pois_.add(new Poi(18.3, 54.7, "Pkt 3"));
        pois_.add(new Poi(18.4, 54.6, "Pkt 4"));
    }
}
