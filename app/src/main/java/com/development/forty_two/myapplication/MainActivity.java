package com.development.forty_two.myapplication;

import android.os.Handler;
import android.os.Message;
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

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import dictionary.Callback;
import dictionary.Dictionary;

public class MainActivity extends AppCompatActivity {
    private Dictionary dictionary;
    private Handler handler;

    static final public String KEY = "Key";
    static final public String TRANSLATE_IS_READY = "TranslateIsReady";
    static final public String TRANSLATE_RESULT = "result";

    @Subscribe
    public void react(Message msg) {
        handler.sendMessage(msg);
    }

    public MainActivity() {
        dictionary = new Dictionary();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bus bus = ((ApplicationModified) getApplication()).getBus();
        bus.register(this);
        dictionary.setBus(bus);

        final EditText input = (EditText) findViewById(R.id.editTextInput);
        final EditText output = (EditText) findViewById(R.id.editTextOutput);

        final Switch separately = (Switch) findViewById(R.id.switchTranslateWords);

        Button translate = (Button) findViewById(R.id.button_translate);
        translate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!separately.isChecked()) {
                    dictionary.translate(input.getText().toString());
                } else {
                    // TODO многострочный вывод
                }
            }
        });

        final Spinner from = (Spinner) findViewById(R.id.spinnerInputLanguage);
        final Spinner to = (Spinner) findViewById(R.id.spinnerOutputLanguage);

        ArrayAdapter <String> adapter1 = new ArrayAdapter<>(this, R.layout.spinner_item_droppped_down,
                R.id.language, dictionary.getLanguages());
        ArrayAdapter <String> adapter2 = new ArrayAdapter<>(this, R.layout.spinner_item_droppped_down,
                R.id.language, dictionary.getLanguages());
        from.setAdapter(adapter1);
        from.setSelection(0);
        to.setAdapter(adapter2);
        to.setSelection(1);

        // TODO тут может быть утечка памяти, но я не въехал пока откуда она тут (нужно пересмотреть лекцию)
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                if (bundle.containsKey(KEY)) {
                    switch (bundle.getString(KEY)) {
                        case TRANSLATE_IS_READY:
                            output.setText(bundle.getString(TRANSLATE_RESULT));
                            break;
                    }
                }
            }
        };
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
