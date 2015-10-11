package com.development.forty_two.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import dictionary.Dictionary;
import utils.MessageKey;

/**
 * Created by ivan on 11.10.15
 */
public class PreloadActivity extends AppCompatActivity {
    private Dictionary dictionary;
    private Handler handler;

    public PreloadActivity() {
        dictionary = Dictionary.getInstance();
    }

    @Subscribe
    public void react(Message msg) {
        handler.sendMessage(msg);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bus bus = ((ApplicationModified) getApplication()).getBus();
        bus.register(this);

        setContentView(R.layout.activity_preload);
        dictionary.updateRoutes();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                String key = bundle.getString(MessageKey.KEY.toString());
                if (key != null) {
                    switch (MessageKey.valueOf(key)) {
                        case UPDATE_ROUTES:
                            Intent i = new Intent(PreloadActivity.this, MainActivity.class);
                            startActivity(i);
                            finish();
                    }
                }
            }
        };
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Intent i = new Intent(PreloadActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }

}
