package com.development.forty_two.myapplication;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
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
import android.widget.Toast;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import dictionary.Dictionary;
import utils.ErrorTypes;
import utils.MessageKey;

public class MainActivity extends AppCompatActivity {
    private Dictionary dictionary;
    private Handler handler;

    private static final String DEFAULT_LANG_FROM = "Английский";
    private static final String DEFAULT_LANG_TO = "Русский";

    private static final String FROM = "from";
    private static final String TO = "to";

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
                String toLang = (String) to.getSelectedItem();
                String fromLang = (String) from.getSelectedItem();
                updateRoute(fromLang, toLang, to);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ArrayAdapter <String> adapter1 = new ArrayAdapter<>(this, R.layout.spinner_item,
                R.id.language, dictionary.getLanguages());
        adapter1.setDropDownViewResource(R.layout.spinner_item_droppped_down);
        from.setAdapter(adapter1);
        if (savedInstanceState != null && savedInstanceState.containsKey(FROM)) {
            from.getSelectedItemPosition();
            from.setSelection(savedInstanceState.getInt(FROM));
        } else {
            from.setSelection(adapter1.getPosition(DEFAULT_LANG_FROM));
        }

        ArrayAdapter <String> adapter2 = new ArrayAdapter<>(this, R.layout.spinner_item,
                R.id.language, dictionary.getToLangPairedWithGivenLang((String)from.getSelectedItem()));
        adapter2.setDropDownViewResource(R.layout.spinner_item_droppped_down);
        to.setAdapter(adapter2);

        if (savedInstanceState != null && savedInstanceState.containsKey(TO)) {
            to.getSelectedItemPosition();
            to.setSelection(savedInstanceState.getInt(TO));
        } else {
            from.setSelection(adapter2.getPosition(DEFAULT_LANG_TO));
        }

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
                String toLang = (String) from.getSelectedItem();
                String fromLang = (String) to.getSelectedItem();
                from.setSelection(((ArrayAdapter) from.getAdapter()).getPosition(fromLang));
                updateRoute(fromLang, toLang,to);

                Editable temp = input.getText();
                input.setText(output.getText());
                output.setText(temp);
            }
        });

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
                        case UPDATE_ROUTES:
                            String toLang = (String) to.getSelectedItem();
                            String fromLang = (String) from.getSelectedItem();
                            updateRoute(fromLang, toLang, to);
                            Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
                            break;
                        case ERROR:
                            String errorMessage;
                            switch (ErrorTypes.valueOf(bundle.getString(MessageKey.ERROR_TYPE.toString()))) {
                                case SqlError:
                                    errorMessage = "Sorry. There is a error in application please reload application." +
                                            "If it hasn't solved your problem, please, reinstall application.";
                                    break;
                                case TranslationError:
                                    errorMessage = "Sorry. There is a error in application please reload application." +
                                            "If it hasn't solved your problem, please, reinstall application.";
                                    break;
                                default:
                                    errorMessage = "Nice day, don't you think so?";
                            }
                            Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    }
                }

            }
        };
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Spinner from = (Spinner) findViewById(R.id.spinnerInputLanguage);
        Spinner to = (Spinner) findViewById(R.id.spinnerOutputLanguage);

        outState.putInt(FROM, from.getSelectedItemPosition());
        outState.putInt(TO, to.getSelectedItemPosition());
    }

    private void updateRoute(String fromLang , String toLang, Spinner to) {
        ArrayList<String> toLangs = dictionary.getToLangPairedWithGivenLang(fromLang);
        ArrayAdapter temp = ((ArrayAdapter) to.getAdapter());
        temp.clear();
        temp.addAll(toLangs);
        if (toLangs.contains(toLang)) {
            to.setSelection(temp.getPosition(toLang));
        } else {
            to.setSelection(0);
        }
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
            case R.id.menu_settings:
                i = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(i);
                return true;
            case R.id.menu_dictionary:
                i = new Intent(MainActivity.this, DictionaryActivity.class);
                startActivity(i);
                return true;
        }

        return true;
        //noinspection SimplifiableIfStatement
    }

}
