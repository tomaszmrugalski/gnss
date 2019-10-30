package com.example.gnss;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class PoiActivity extends AppCompatActivity {

    ListView poiList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi);

        ArrayAdapter<String> adapter;
        poiList = findViewById(R.id.poiList);

        String poiListTxt[] = { "54, 18", "0,45", "1,1" };
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
        bt1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            }
        });
    }

}
