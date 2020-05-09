package com.byfrunze.memshelper;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);

        RealmConfiguration configurationHistory = new RealmConfiguration.Builder()
                .name("history.realm")
                .schemaVersion(1)
                .build();
        RealmConfiguration configurationWidget = new RealmConfiguration.Builder()
                .name("widget.realm")
                .schemaVersion(1)
                .build();


    }
}
