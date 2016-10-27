package com.infusionsoft.beerhub;

import android.app.Application;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * File description here...
 */
public class BeerHubApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();



        /*RealmConfiguration config = new RealmConfiguration.Builder(this)
            .name("beer-hub.realm")
            .schemaVersion(0)
            .deleteRealmIfMigrationNeeded()
            .build();
        Realm.setDefaultConfiguration(config);*/
    }

}
