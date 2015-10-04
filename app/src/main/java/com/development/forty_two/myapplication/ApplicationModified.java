package com.development.forty_two.myapplication;

import android.app.Application;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

import dbservice.DBHelperFactory;

/**
 * Created by neikila on 29.09.15.
 */
public class ApplicationModified extends Application {
    private Bus bus;

    @Override
    public void onCreate() {
        super.onCreate();
        bus = new Bus(ThreadEnforcer.ANY);

        // TODO отвратительная зависимость базы от контекста, можно ли от неё избавиться?
        DBHelperFactory.setHelper(getApplicationContext());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        // TODO отвратительная зависимость базы от контекста, можно ли от неё избавиться?
        DBHelperFactory.releaseHelper();
    }

    public Bus getBus() {
        return bus;
    }
}
