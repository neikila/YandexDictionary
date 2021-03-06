package com.development.forty_two.myapplication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import dictionary.Dictionary;
import utils.ErrorReflection;
import utils.ErrorTypes;
import utils.MessageKey;

public class MainActivity extends AppCompatActivity {
    private Dictionary dictionary;
    private Handler handler;

    private static final String DEFAULT_LANG_FROM = "Английский";
    private static final String DEFAULT_LANG_TO = "Русский";

    private static final String FROM = "FROM";
    private static final String TO = "TO";

    private static final String LAST_INPUT_WORD = "LAST_IN_WORD";
    private static final String LAST_OUTPUT_WORD = "LAST_OUT_WORD";

    private boolean isRotating = false;

    @Subscribe
    public void react(Message msg) {
        if (handler != null) {
            if (MessageKey.valueOf(msg.getData().getString(MessageKey.KEY.toString())).equals(MessageKey.TRANSLATE_IS_READY)) {
                handler.sendMessageDelayed(msg, 2000);
            } else {
                handler.sendMessage(msg);
            }
        }
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

        final SharedPreferences mSettings = getSharedPreferences(SettingsActivity.APP_PREFERENCES, Context.MODE_PRIVATE);

        final EditText input = (EditText) findViewById(R.id.editTextInput);
        final EditText output = (EditText) findViewById(R.id.editTextOutput);

        final Spinner from = (Spinner) findViewById(R.id.spinnerInputLanguage);
        final Spinner to = (Spinner) findViewById(R.id.spinnerOutputLanguage);
        final ImageButton rotateButton = (ImageButton) findViewById(R.id.rotateButton);
        findViewById(R.id.loading_spinner).setVisibility(View.GONE);

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
                            hideView(findViewById(R.id.loading_spinner), 100);
                            output.setText(bundle.getString(MessageKey.TRANSLATE_RESULT.toString()));
                            appearView(findViewById(R.id.editTextOutput), 1000);
                            break;
                        case UPDATE_ROUTES:
                            String fromLang = (String) from.getSelectedItem();
                            ArrayAdapter <String> adapterFrom = (ArrayAdapter)from.getAdapter();
                            adapterFrom.clear();
                            adapterFrom.addAll(dictionary.getFromLanguages());
                            from.setAdapter(adapterFrom);
                            from.setSelection(adapterFrom.getPosition(fromLang));
                            Toast.makeText(getApplicationContext(), "Languages updated", Toast.LENGTH_LONG).show();
                            break;
                        case ERROR:
                            ErrorReflection.reflectOnError(getApplicationContext(), ErrorTypes.valueOf(bundle.getString(MessageKey.ERROR_TYPE.toString())));
                    }
                }
            }
        };

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
        String toLang;
        if (savedInstanceState != null && savedInstanceState.containsKey(TO)) {
            toLang = savedInstanceState.getString(TO);
        } else {
            toLang = DEFAULT_LANG_TO;
        }
        ArrayList <String> temp = new ArrayList<>();
        temp.add(toLang);

        ArrayAdapter <String> adapterTo = new ArrayAdapter<>(this, R.layout.spinner_item,
                R.id.ltem, temp);
        adapterTo.setDropDownViewResource(R.layout.spinner_item_droppped_down);
        to.setAdapter(adapterTo);

        /* Change from language */
        from.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String toLang = (String) to.getSelectedItem();
                String fromLang = (String) from.getSelectedItem();
                new UpdateRouteAsyncTask().execute(fromLang, toLang);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ArrayAdapter <String> adapterFrom = new ArrayAdapter<>(this, R.layout.spinner_item,
                R.id.ltem, dictionary.getFromLanguages());
        adapterFrom.setDropDownViewResource(R.layout.spinner_item_droppped_down);
        from.setAdapter(adapterFrom);
        if (savedInstanceState != null && savedInstanceState.containsKey(FROM)) {
            from.setSelection(adapterFrom.getPosition(savedInstanceState.getString(FROM)));
        } else {
            from.setSelection(adapterFrom.getPosition(DEFAULT_LANG_FROM));
        }


        Button translate = (Button) findViewById(R.id.button_translate);
        translate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputLang;
                if (mSettings.getBoolean(SettingsActivity.APP_PREFERENCES_AUTODETERMINE_INLANG,false)) {
                    inputLang = "";
                } else {
                    inputLang = (String) from.getSelectedItem();
                }
                dictionary.translate(input.getText().toString(),
                        inputLang,
                        (String) to.getSelectedItem()
                );
                hideView(findViewById(R.id.editTextOutput), 0);
                appearView(findViewById(R.id.loading_spinner), 1000);
            }
        });


        rotateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRotating) {
                    isRotating = true;
                    String toLang = (String) from.getSelectedItem();
                    String fromLang = (String) to.getSelectedItem();
                    ((ArrayAdapter) to.getAdapter()).insert(toLang, 0);
                    to.setSelection(0);
                    from.setSelection(((ArrayAdapter) from.getAdapter()).getPosition(fromLang));

                    String outputWord = input.getText().toString();
                    String inputWord = output.getText().toString();
                    if (inputWord.equals("")) {
                        outputWord = "";
                    }
                    input.setText(inputWord);
                    output.setText(outputWord);
                }
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Bus bus = ((ApplicationModified) getApplication()).getBus();
        bus.register(this);
    }

    @Override
    protected void onResume(){
        super.onResume();
        final Spinner from = (Spinner) findViewById(R.id.spinnerInputLanguage);
        final ImageButton rotateButton = (ImageButton) findViewById(R.id.rotateButton);
        SharedPreferences mSettings = getSharedPreferences(SettingsActivity.APP_PREFERENCES, Context.MODE_PRIVATE);
        if (mSettings.getBoolean(SettingsActivity.APP_PREFERENCES_AUTODETERMINE_INLANG,false)){
            from.setClickable(false);
            rotateButton.setClickable(false);
        } else {
            from.setClickable(true);
            rotateButton.setClickable(true);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Spinner from = (Spinner) findViewById(R.id.spinnerInputLanguage);
        Spinner to = (Spinner) findViewById(R.id.spinnerOutputLanguage);
        outState.putString(FROM, (String) from.getSelectedItem());
        outState.putString(TO, (String) to.getSelectedItem());


        EditText in = (EditText) findViewById(R.id.editTextInput);
        EditText out = (EditText) findViewById(R.id.editTextOutput);
        outState.putString(LAST_INPUT_WORD, in.getText().toString());
        outState.putString(LAST_OUTPUT_WORD, out.getText().toString());
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

    private class UpdateRouteAsyncTask extends AsyncTask <String, Void, Void> {
        private ArrayList<String> toLangs;
        private String toLang;

        @Override
        protected Void doInBackground(String... params) {
            String fromLang = params[0];
            toLang = params[1];
            toLangs = dictionary.getToLangPairedWithGivenLang(fromLang);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Spinner to = (Spinner) findViewById(R.id.spinnerOutputLanguage);
            ArrayAdapter temp = ((ArrayAdapter) to.getAdapter());
            temp.clear();
            temp.addAll(toLangs);
            if (toLangs.contains(toLang)) {
                to.setSelection(temp.getPosition(toLang));
            } else {
                to.setSelection(0);
            }

            if (isRotating) {
                isRotating = false;
            }
        }
    }

    public void appearView(final View showView, long milisec) {
        showView.setAlpha(0f);
        showView.setVisibility(View.VISIBLE);

        showView.animate()
                .alpha(1f)
                .setDuration(milisec)
                .setListener(null);
    }

    public void hideView(final View view, long milisec) {
        view.animate()
                .alpha(0f)
                .setDuration(milisec)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setVisibility(View.INVISIBLE);
                    }
                });
    }
}
