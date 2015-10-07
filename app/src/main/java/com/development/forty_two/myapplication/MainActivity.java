package com.development.forty_two.myapplication;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
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
import android.widget.TextView;
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

    private static final String LAST_INPUT_WORD = "LAST_IN_WORD";
    private static final String LAST_OUTPUT_WORD = "LAST_OUT_WORD";

    private Boolean isSwapping;

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
//        dictionary.setBus(bus);

        final EditText input = (EditText) findViewById(R.id.editTextInput);
        final EditText output = (EditText) findViewById(R.id.editTextOutput);

        final Switch separately = (Switch) findViewById(R.id.switchTranslateWords);

        final Spinner from = (Spinner) findViewById(R.id.spinnerInputLanguage);
        final Spinner to = (Spinner) findViewById(R.id.spinnerOutputLanguage);

        /* Восстановление данных */
        if (savedInstanceState != null) {
            input.setText(savedInstanceState.getString(LAST_INPUT_WORD));
            output.setText(savedInstanceState.getString(LAST_OUTPUT_WORD));
        }

        /* Установка хендлера сообщения */
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
                            reflectOnError(ErrorTypes.valueOf(bundle.getString(MessageKey.ERROR_TYPE.toString())));
                    }
                }
            }
        };
        dictionary.updateRoutes();

        /* Set ersing output */
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                output.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        /* Setting data for spinners */
        ArrayAdapter <String> adapterFrom = new ArrayAdapter<>(this, R.layout.spinner_item,
                R.id.language, dictionary.getLanguages());
        adapterFrom.setDropDownViewResource(R.layout.spinner_item_droppped_down);
        from.setAdapter(adapterFrom);
        if (savedInstanceState != null && savedInstanceState.containsKey(FROM)) {
            from.getSelectedItemPosition();
            from.setSelection(savedInstanceState.getInt(FROM));
        } else {
            from.setSelection(adapterFrom.getPosition(DEFAULT_LANG_FROM));
        }

        ArrayAdapter <String> adapterTo = new ArrayAdapter<>(this, R.layout.spinner_item,
                R.id.language, dictionary.getToLangPairedWithGivenLang((String)from.getSelectedItem()));
        adapterTo.setDropDownViewResource(R.layout.spinner_item_droppped_down);
        to.setAdapter(adapterTo);

        if (savedInstanceState != null && savedInstanceState.containsKey(TO)) {
            to.getSelectedItemPosition();
            to.setSelection(savedInstanceState.getInt(TO));
        } else {
            to.setSelection(adapterTo.getPosition(DEFAULT_LANG_TO));
        }


        /* Change from language */
        from.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String toLang = (String) to.getSelectedItem();
                String fromLang = (String) from.getSelectedItem();
                updateRoute(fromLang, toLang, to);

                // TODO спросить про то как тут очищать

//                input.setText("");
//                output.setText("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

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
                updateRoute(fromLang, toLang, to);

                Editable temp = input.getText();
                input.setText(output.getText());
                output.setText(temp);
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Spinner from = (Spinner) findViewById(R.id.spinnerInputLanguage);
        Spinner to = (Spinner) findViewById(R.id.spinnerOutputLanguage);
        outState.putInt(FROM, from.getSelectedItemPosition());
        outState.putInt(TO, to.getSelectedItemPosition());


        EditText in = (EditText) findViewById(R.id.editTextInput);
        EditText out = (EditText) findViewById(R.id.editTextOutput);
        outState.putString(LAST_INPUT_WORD, in.getText().toString());
        outState.putString(LAST_OUTPUT_WORD, out.getText().toString());
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
