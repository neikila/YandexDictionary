package com.development.forty_two.myapplication;

import android.app.Application;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * Created by neikila on 29.09.15.
 */
public class ApplicationModified extends Application {
    private Bus bus;

    @Override
    public void onCreate() {
        super.onCreate();
        bus = new Bus(ThreadEnforcer.ANY);
    }

    public Bus getBus() {
        return bus;
    }
}
