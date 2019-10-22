package com.example.gnss;

import android.location.OnNmeaMessageListener;
import android.util.Log;
import android.widget.TextView;

public class ThNMEAListener  implements OnNmeaMessageListener {

    public TextView debug;

    @Override
    public void onNmeaMessage(String message, long timestamp) {
        Log.d("nmea", message);

        debug.append(message);
    }
}
