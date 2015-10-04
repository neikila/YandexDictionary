package com.development.forty_two.myapplication;

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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import dictionary.Dictionary;
import utils.MessageKey;

public class MainActivity extends AppCompatActivity {
    private Dictionary dictionary;
    private Handler handler;

    @Subscribe
    public void react(Message msg) {
        handler.sendMessage(msg);
    }

    public MainActivity() {
        dictionary = Dictionary.getInstance();
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

        final Spinner from = (Spinner) findViewById(R.id.spinnerInputLanguage);
        final Spinner to = (Spinner) findViewById(R.id.spinnerOutputLanguage);

        from.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<String> toLangs = dictionary.getToLangPairedWithGivenLang((String) from.getSelectedItem());
                ArrayAdapter temp = ((ArrayAdapter) to.getAdapter());
                String currentLang = (String) to.getSelectedItem();
                temp.clear();
                temp.addAll(toLangs);
                if (toLangs.contains(currentLang)) {
                    to.setSelection(temp.getPosition(currentLang));
                } else {
                    to.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ArrayAdapter <String> adapter1 = new ArrayAdapter<String>(this, R.layout.spinner_item_droppped_down,
                R.id.language, dictionary.getLanguages());
        from.setAdapter(adapter1);
        from.setSelection(0);

        ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(this, R.layout.spinner_item_droppped_down,
                R.id.language, dictionary.getToLangPairedWithGivenLang((String)from.getSelectedItem()));
        to.setAdapter(adapter2);
        to.setSelection(0);

        Button translate = (Button) findViewById(R.id.button_translate);
        translate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!separately.isChecked()) {
                    dictionary.translate(input.getText().toString(),
                            (String) from.getSelectedItem(),
                            (String) to.getSelectedItem()
                    );
                } else {
                    // TODO многострочный вывод
                }
            }
        });

        ImageButton rotateButton = (ImageButton) findViewById(R.id.rotateButton);
        rotateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sourceLang = (String) from.getSelectedItem();
                String currentLang = (String) to.getSelectedItem();
                from.setSelection(((ArrayAdapter) from.getAdapter()).getPosition(currentLang));

                ArrayList<String> toLangs = dictionary.getToLangPairedWithGivenLang(currentLang);
                ArrayAdapter temp = ((ArrayAdapter) to.getAdapter());
                temp.clear();
                temp.addAll(toLangs);
                if (toLangs.contains(sourceLang)) {
                    to.setSelection(temp.getPosition(sourceLang));
                } else {
                    to.setSelection(0);
                }
            }
        });

        // TODO тут может быть утечка памяти, но я не въехал пока откуда она тут (нужно пересмотреть лекцию)
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                String key = bundle.getString(MessageKey.KEY.toString());
                if (key != null) {
                    switch (MessageKey.valueOf(key)) {
                        case TRANSLATE_IS_READY:
                            output.setText(bundle.getString(MessageKey.TRANSLATE_RESULT.toString()));
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
