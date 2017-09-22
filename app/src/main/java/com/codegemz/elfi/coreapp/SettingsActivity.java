package com.codegemz.elfi.coreapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.codegemz.elfi.model.BTNameConstants;

import java.util.ArrayList;
import java.util.List;


public class SettingsActivity extends AppCompatActivity {
    private AutoCompleteTextView btNamesACTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        btNamesACTV = (AutoCompleteTextView) findViewById(R.id.bt_name);
        List<String> list = new ArrayList<>();
        for(BTNameConstants btn : BTNameConstants.values()){
            list.add(btn.getDeviceName());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        btNamesACTV.setAdapter(dataAdapter);
    }

    public void onClick(View v){
        SharedPreferences prefs = getSharedPreferences(
                "com.codegemz.elfi.coreapp", Context.MODE_PRIVATE);
        String btNameKey = "com.codegemz.elfi.coreapp.bt_name";
        prefs.edit().putString(btNameKey, btNamesACTV.getText().toString()).apply();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
