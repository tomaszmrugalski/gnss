package com.example.gnss;

import android.location.OnNmeaMessageListener;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;

public class ThNMEAListener  implements OnNmeaMessageListener {

    public TextView debug;

    @Override
    public void onNmeaMessage(String message, long timestamp) {
        // Log.d("nmea", message);

        Date now = Calendar.getInstance().getTime();

        writeToFile("nmea.txt", now.toString() + ": " + message);
        debug.setText(message);
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
        Log.e("OK", "Wrote " + txt.length() + " byte(s)");
    }

}
