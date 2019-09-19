package com.example.songs_listing;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.songs_listing.songs.SongAsynctask;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner titleSpinner;
    private Spinner rowSpinner;

    private static final String API_ENDPOINT = "http://api.myjson.com/bins/rov51";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        titleSpinner = findViewById(R.id.title);
        rowSpinner = findViewById(R.id.row);
        ArrayAdapter<String> titleAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.titles));
        titleAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        titleSpinner.setAdapter(titleAdapter);
        titleSpinner.setOnItemSelectedListener(this);

        ArrayAdapter<String> rowAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.rows));
        titleAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        rowSpinner.setAdapter(rowAdapter);
        rowSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        new SongAsynctask(this, titleSpinner.getSelectedItem().toString(), Integer.valueOf(rowSpinner.getSelectedItem().toString())).execute(API_ENDPOINT);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}
