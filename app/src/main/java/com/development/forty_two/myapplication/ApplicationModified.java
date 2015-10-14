package com.development.forty_two.myapplication;

import android.app.Application;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

import dbservice.DBHelperFactory;
import dictionary.Dictionary;

/**
 * Created by neikila on 29.09.15.
 */
public class ApplicationModified extends Application {
    private Bus bus;

    @Override
    public void onCreate() {
        DBHelperFactory.setHelper(getApplicationContext());
        bus = new Bus(ThreadEnforcer.ANY);
        Dictionary dictionary = Dictionary.getInstance();
        dictionary.setBus(bus);
        dictionary.updateRoutes();
        super.onCreate();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        DBHelperFactory.releaseHelper();
    }

    public Bus getBus() {
        return bus;
    }
}
