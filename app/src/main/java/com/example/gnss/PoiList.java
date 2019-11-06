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
        pois_.add(new Poi(18.6163, 54.3717, "PG"));
        pois_.add(new Poi(18.5305, 54.5189, "Gdynia"));
        pois_.add(new Poi(18.7773, 54.0919, "Tczew"));
        pois_.add(new Poi(18.3650, 54.3420, "Zukowo"));
    }
}
