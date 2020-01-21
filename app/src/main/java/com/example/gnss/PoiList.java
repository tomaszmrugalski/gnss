package com.example.gnss;

import java.lang.reflect.Array;
import java.util.ArrayList;

class PoiList {
    private static final PoiList ourInstance = new PoiList();

    public static ArrayList<Poi> pois_;

    static PoiList getInstance() {
        return ourInstance;
    }

    void add(double lat, double lon, String name, int openmonfri, int closemonfri, int opensat, int closesat, int opensun, int closesun) {
        Poi p = new Poi(lon, lat, name);
        p.setHours(Poi.Day.MONFRI, openmonfri, closemonfri);
        p.setHours(Poi.Day.SAT, opensat, closesat);
        p.setHours(Poi.Day.SUN, opensun, closesun);
        /// @todo: working sunday
        pois_.add(p);
    }

    private PoiList() {
        pois_ = new ArrayList<Poi>();

        // Come up with some better fabricated data!
        add(54.370428, 18.611526, "ABI", 0, 2400, 0, 2400, 0, 2400);
        add(54.377451, 18.616577, "Lidl", 700, 2100, 700, 2100, 0, 0);
        add(54.369990, 18.610157, "Mechaniczna pomarańcza", 1300, 2400, 1300, 2400, 1100, 2400);
        add(54.368924,18.610707,"Autsajder",  1300, 2400, 1300, 2400, 1100, 2400);
        add( 54.375402,18.616569,"Radiostacja", 1100, 2300, 1100, 2400, 1100, 2100);
        add( 54.375479,18.616220,"Lewiatan", 700, 2100, 700, 2100, 0, 0);
    }
}
