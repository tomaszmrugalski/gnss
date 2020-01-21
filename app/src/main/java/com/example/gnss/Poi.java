package com.example.gnss;


public class Poi {
    public enum Day { MONFRI, SAT, SUN, SUNWORK };

    public class Hours {

        Hours(int openh, int close) {
            open_ = openh;
            close_ = close;
        }

        boolean open(int h) {
            return ( (open_ <= h) && (h <= close_));
        }

        int open_;  // opening hour (e.g. 900 = 9:00am)
        int close_; // opening hour (e.g. 2300 = 23:00 or 11;00pm)
    };

    void setHours(Day typeofday, int openh, int closeh) {
        Hours h = new Hours(openh, closeh);
        switch (typeofday) {
            case MONFRI:
            default:
                monfri_ = h;
                return;
            case SAT:
                sat_ = h;
                return;
            case SUN:
                sun_ = h;
                return;
            case SUNWORK:
                sunwork_ = h;
                return;
        }
    }

    boolean open(Day typeofday, int hour) {
        switch (typeofday) {
            case MONFRI:
            default:
                return monfri_.open(hour);
            case SAT:
                return sat_.open(hour);
            case SUN:
                return sun_.open(hour);
            case SUNWORK:
                return sunwork_.open(hour);
        }
    }

    public Poi(double lon, double lat, String descr) {
        this.lon = lon;
        this.lat = lat;
        this.descr = descr;
        monfri_ = new Hours(0, 2400);
        sat_ = new Hours(0, 2400);
        sun_ = new Hours(0, 2400);
        sunwork_ = new Hours(0, 0);
    }

    public String getText() {
        String t = descr + " " + String.format("%.5f %s,%.5f%s", lon, (lon>0?"E":"W"), lat, (lat>0?"N":"S"));
        return t;
    }

    public String getTextHours() {
        String t = "Pon-pt: " + String.format("%02d:%02d - %02d:%02d", monfri_.open_/100, monfri_.open_%100, monfri_.close_/100, monfri_.close_%100) + "\n";
        t += "Sob: " + String.format("%02d:%02d - %02d:%02d", sat_.open_/100, sat_.open_%100, sat_.close_/100, sat_.close_%100) + "\n";
        t += "Niedz: " + String.format("%02d:%02d - %02d:%02d", sun_.open_/100, sun_.open_%100, sun_.close_/100, sun_.close_%100) + "\n";
        return t;
    }

    public double lon;
    public double lat;
    public String descr;

    public Hours monfri_;
    public Hours sat_;
    public Hours sun_;
    public Hours sunwork_;
};
