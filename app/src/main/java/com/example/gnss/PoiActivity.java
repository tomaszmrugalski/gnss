package com.example.gnss;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class PoiActivity extends AppCompatActivity {

    ListView poiList;
    ArrayList<String> poiListTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi);

        final ArrayAdapter<String> adapter;
        poiList = findViewById(R.id.poiList);


        final ArrayList<Poi> pois = PoiList.getInstance().pois_;

        poiListTxt = new ArrayList<String>();
        for (Poi poi : pois) {
            poiListTxt.add(poi.getText());
        }

        adapter = new ArrayAdapter<String>(this, R.layout.activity_list_view, R.id.textView5, poiListTxt);
        poiList.setAdapter(adapter);

        Button bt1 = (Button) findViewById(R.id.button1);
        bt1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button bt2 = (Button) findViewById(R.id.button2);
        bt2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            EditText lon = findViewById(R.id.longitudeBox);
            EditText lat = findViewById(R.id.lattitudeBox);
            EditText name = findViewById(R.id.poiName);

            Poi p = new Poi(Double.parseDouble(lon.getText().toString()), Double.parseDouble(lat.getText().toString()), name.getText().toString());

            addPoi(p);
            pois.add(p);

            }
        });
    }

    private void addPoi(Poi p) {
        poiListTxt.add(p.getText());
        Log.d("onClick", Integer.toString(poiListTxt.size()) + " element(s)");
        poiList.invalidateViews();
    }

}
