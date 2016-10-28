package com.infusionsoft.beerhub;

import android.app.Application;
import android.util.Log;
import com.infusionsoft.beerhub.model.BeerDrinker;
import com.infusionsoft.beerhub.model.Settings;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import java.util.ArrayList;
import java.util.List;

/**
 * File description here...
 */
public class BeerHubApplication extends Application {

    private static final String TAG = BeerHubApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
            .name(Realm.DEFAULT_REALM_NAME)
            .schemaVersion(0)
            .deleteRealmIfMigrationNeeded()
            .build();
        Realm.setDefaultConfiguration(realmConfiguration);

        Realm realm = Realm.getDefaultInstance();
        final Settings settings = getSettings();
        if (!settings.isInitialized()) {
            Log.d(TAG, "initialize with dummy data");

            final List<BeerDrinker> drinkers = new ArrayList<>();

            drinkers.add(BeerDrinker.create("President Skroob", "12345", 0, 0));
            drinkers.add(BeerDrinker.create("Dark Helmet", "13254", 10, 2));
            drinkers.add(BeerDrinker.create("Lone Starr", "90354", 20, 10));
            drinkers.add(BeerDrinker.create("Princess Vespa", "02385", 0, 60));
            drinkers.add(BeerDrinker.create("Barf", "98538", 0, 0));

            settings.setInitialized(true);

            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.copyToRealm(drinkers);
                    realm.copyToRealm(settings);
                }
            });
        }
    }

    private Settings getSettings() {
        Realm realm = Realm.getDefaultInstance();
        Settings settings = realm.where(Settings.class)
            .findFirst();

        if (settings == null) {
            settings = new Settings();
        } else {
            settings = realm.copyFromRealm(settings);
        }
        return settings;
    }
}
