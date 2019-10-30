package com.example.gnss;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

        poiListTxt = new ArrayList<String>();
        poiListTxt.add("54, 18");
        poiListTxt.add("0,45");
        poiListTxt.add("1,1");
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
                EditText lon = findViewById(R.id.)

            poiListTxt.add("new data");
            Log.d("onClick", Integer.toString(poiListTxt.size()) + " element(s)");
            adapter.notifyDataSetChanged();
            poiList.invalidateViews();
            }
        });
    }

}
