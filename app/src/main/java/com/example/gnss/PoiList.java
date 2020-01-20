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

        pois_.add(new Poi(18.611526, 54.370428, "ABI" ));
        pois_.add(new Poi(18.616577, 54.377451, "Lidl"));
        pois_.add(new Poi(18.610157, 54.369990, "Mechaniczna pomarańcza"));
        pois_.add(new Poi(18.610707, 54.368924, "Autsajder"));
        pois_.add(new Poi(18.616569, 54.375402, "Radiostacja"));
        pois_.add(new Poi(18.616220, 54.375479, "Lewiatan"));

        // pois_.add(new Poi(18.6163, 54.3717, "PG"));
        // pois_.add(new Poi(18.5305, 54.5189, "Gdynia"));
        // pois_.add(new Poi(18.7773, 54.0919, "Tczew"));
        // pois_.add(new Poi(18.3650, 54.3420, "Zukowo"));
    }
}
