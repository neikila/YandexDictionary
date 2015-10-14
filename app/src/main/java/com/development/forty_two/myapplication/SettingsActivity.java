package com.development.forty_two.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.sql.SQLException;
import java.util.ArrayList;

import dictionary.Dictionary;
import utils.ErrorTypes;
import utils.MessageKey;

public class SettingsActivity extends AppCompatActivity {

    public static final String APP_PREFERENCES = "settings";
    public static final String APP_PREFERENCES_AUTOSAVE_HISTORY = "autosave_history";
    public static final String APP_PREFERENCES_AUTODETERMINE_INLANG = "autodetermine_inlang";
    private SharedPreferences mSettings;
    private Dictionary dictionary;
    private Handler handler;

    @Subscribe
    public void react(Message msg) {
        if (handler != null) {
            handler.sendMessage(msg);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                String key = bundle.getString(MessageKey.KEY.toString());
                if (key != null) {
                    switch (MessageKey.valueOf(key)) {
                        case UPDATE_ROUTES:
                            Toast.makeText(getApplicationContext(), "Languages updated", Toast.LENGTH_LONG).show();
                            break;
                        case ERROR:
                            reflectOnError(ErrorTypes.valueOf(bundle.getString(MessageKey.ERROR_TYPE.toString())));
                    }
                }
            }
        };


        Bus bus = ((ApplicationModified) getApplication()).getBus();
        bus.register(this);

        dictionary = Dictionary.getInstance();

        setContentView(R.layout.activity_settings);

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        final CheckBox autoSave = (CheckBox) findViewById(R.id.checkBox);
        final CheckBox autoDetermine = (CheckBox) findViewById(R.id.checkBox2);


        if (mSettings.contains(APP_PREFERENCES_AUTOSAVE_HISTORY)) {
            autoSave.setChecked(mSettings.getBoolean(APP_PREFERENCES_AUTOSAVE_HISTORY, false));
        }
        if (mSettings.contains(APP_PREFERENCES_AUTODETERMINE_INLANG)){
            autoDetermine.setChecked(mSettings.getBoolean(APP_PREFERENCES_AUTODETERMINE_INLANG, false));
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
                editor.putBoolean(APP_PREFERENCES_AUTODETERMINE_INLANG, isChecked);
                editor.commit();
            }
        });

        final Button updateRoutes = (Button) findViewById(R.id.updateRoutes);
        updateRoutes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dictionary.updateRoutes();
            }
        });

        final Button cleanButton = (Button) findViewById(R.id.cleanButton);
        cleanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ClearAsyncTask().execute();
            }
        });
    }

    private void reflectOnError(ErrorTypes type) {
        String errorMessage;
        switch (type) {
            case SqlError:
                errorMessage = "Sorry. There is a error in application please reload application." +
                        "If it hasn't solved your problem, please, reinstall application.";
                break;
            case TranslationError:
                errorMessage = "Sorry. There is a error in application please reload application." +
                        "If it hasn't solved your problem, please, reinstall application.";
                break;
            case NoInternetCantUpdateRoutes:
                errorMessage = "No Internet connection. Can't update routes.";
                break;
            case NoInternet:
                errorMessage = "Word is not in database and there is no Internet connection";
                break;
            default:
                errorMessage = "Nice day, don't you think so?";
        }
        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Bus bus = ((ApplicationModified) getApplication()).getBus();
        bus.register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Bus bus = ((ApplicationModified) getApplication()).getBus();
        bus.unregister(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    private class ClearAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                dictionary.clearDict();
            } catch (SQLException e) {
                // Error, There is nothing we can do with it
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getApplicationContext(), "Dictionary cleaned", Toast.LENGTH_LONG).show();
        }
    }

}
