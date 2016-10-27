package com.infusionsoft.beerhub;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import com.infusionsoft.beerhub.model.BeerDrinker;
import com.infusionsoft.beerhub.model.BeerDrinkerFields;
import io.realm.Realm;

public class PersonalDashboardActivity extends AppCompatActivity {

    public static final String PIN_KEY = "key.pin";

    BeerDrinker drinker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_dashboard);
        //TODO show current totals on startup?

        String pin = getIntent().getStringExtra(PIN_KEY);
        Log.d("PersonalDashboard", pin);
        Realm realm = Realm.getDefaultInstance();
        drinker = realm.where(BeerDrinker.class)
            .equalTo(BeerDrinkerFields.PIN, pin)
            .findFirst();

        realm.beginTransaction();
        drinker.setBeersAdded(373);
        realm.commitTransaction();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                drinker.setBeersAdded(373);
            }
        });
    }

    public void clickAddCase(View view) {
        addBeers(12);
    }

    public void clickAddSixPack(View view) {
        addBeers(6);
    }

    public void clickAddBeer(View view) {
        addBeers(1);
    }

    public void clickTakeBeer(View view) {
        addBeers(-1);
    }

    public void addBeers(int numAdded){
        //TODO get the current user and update the beers
        showInteractionSummary();
    }

    public void showInteractionSummary(){
        //TODO new NetBeers
        //TODO earned badges?
        //TODO drop back to mainActitivy
    }

}
