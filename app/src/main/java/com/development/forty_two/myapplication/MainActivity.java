package com.development.forty_two.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import java.util.Vector;

import dbservice.DbService;
import dbservice.DbServiceStubImpl;
import yandex.YandexCommunicator;

public class MainActivity extends AppCompatActivity {
    YandexCommunicator communicator;
    DbService dbService = new DbServiceStubImpl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText input = (EditText) findViewById(R.id.editTextInput);
        final EditText output = (EditText) findViewById(R.id.editTextOutput);

        final Switch separately = (Switch) findViewById(R.id.switchTranslateWords);

        Button translate = (Button) findViewById(R.id.button_translate);
        translate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!separately.isChecked()) {
                    output.setText(communicator.translate(input.getText().toString()));
                } else {
                    Vector<String> result = communicator.translateSeparately(input.getText().toString());
                    // TODO многострочный вывод
                }
            }
        });

        final Spinner from = (Spinner) findViewById(R.id.spinnerInputLanguage);
        final Spinner to = (Spinner) findViewById(R.id.spinnerOutputLanguage);

        ArrayAdapter <String> adapter1 = new ArrayAdapter<>(this, R.layout.spinner_item_droppped_down, R.id.language, dbService.getLanguages());
        ArrayAdapter <String> adapter2 = new ArrayAdapter<>(this, R.layout.spinner_item_droppped_down, R.id.language, dbService.getLanguages());
        from.setAdapter(adapter1);
        from.setSelection(0);
        to.setAdapter(adapter2);
        to.setSelection(1);
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
