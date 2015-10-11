package com.development.forty_two.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {

    public static final String APP_PREFERENCES = "settings";
    public static final String APP_PREFERENCES_LIMITS_SAVE = "limits_save";
    public static final String APP_PREFERENCES_AUTOSAVE_HISTORY = "autosave_history";
    public static final String APP_PREFERENCES_AUTODETERMINE_INLANG = "autodetermine_inlang";
    private SharedPreferences mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayList<Integer> limits = new ArrayList<>();
        limits.add(10);
        limits.add(25);
        limits.add(50);
        limits.add(100);
        final ArrayAdapter<Integer> limitsAdap = new ArrayAdapter<Integer>(this,R.layout.spinner_item,
                R.id.ltem,limits);
        limitsAdap.setDropDownViewResource(R.layout.spinner_item_droppped_down);
        setContentView(R.layout.activity_settings);


        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        final CheckBox autoSave = (CheckBox) findViewById(R.id.checkBox);
        final CheckBox autoDetermine = (CheckBox) findViewById(R.id.checkBox2);
        final Spinner limitsSpin = (Spinner) findViewById(R.id.limits_save);
        limitsSpin.setAdapter(limitsAdap);
        limitsSpin.setSelection(0);

        if (mSettings.contains(APP_PREFERENCES_AUTOSAVE_HISTORY)) {
            autoSave.setChecked(mSettings.getBoolean(APP_PREFERENCES_AUTOSAVE_HISTORY, false));
        }
        if (mSettings.contains(APP_PREFERENCES_AUTODETERMINE_INLANG)){
            autoDetermine.setChecked(mSettings.getBoolean(APP_PREFERENCES_AUTODETERMINE_INLANG,false));
        }
        if (mSettings.contains(APP_PREFERENCES_LIMITS_SAVE)){
            limitsSpin.setSelection(limitsAdap.getPosition(mSettings.getInt(APP_PREFERENCES_LIMITS_SAVE,10)));
        }

        autoSave.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = mSettings.edit();
                editor.putBoolean(APP_PREFERENCES_AUTOSAVE_HISTORY, isChecked);
                editor.commit();
            }
        });

        autoDetermine.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = mSettings.edit();
                editor.putBoolean(APP_PREFERENCES_AUTODETERMINE_INLANG,isChecked);
                editor.commit();
            }
        });
        limitsSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences.Editor editor = mSettings.edit();
                editor.putInt(APP_PREFERENCES_LIMITS_SAVE,limitsAdap.getItem(position));
                editor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                SharedPreferences.Editor editor = mSettings.edit();
                editor.putInt(APP_PREFERENCES_LIMITS_SAVE,10);
                editor.commit();
            }
        });
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
        this.closeOptionsMenu();
        int id = item.getItemId();
        Intent i;
        switch (id){
            case R.id.menu_main:
                i = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(i);
                return true;
            case R.id.menu_dictionary:
                i = new Intent(SettingsActivity.this, DictionaryActivity.class);
                startActivity(i);
                return true;
        }
        //noinspection SimplifiableIfStatement
        return true;
    }
}
