package com.infusionsoft.beerhub;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.infusionsoft.beerhub.model.BeerDrinker;
import com.infusionsoft.beerhub.model.BeerDrinkerFields;
import io.realm.Realm;

public class PersonalDashboardActivity extends AppCompatActivity {

    public static final String PIN_KEY = "key.pin";
    private static final String NEGATIVE_NET_BEERS  = "Beers in the hole!";
    private static final String POSITIVE_NET_BEERS  = "Beers still to drink!";

    private BeerDrinker drinker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_dashboard);

        String pin = getIntent().getStringExtra(PIN_KEY);
        Log.d("PersonalDashboard", pin);
        Realm realm = Realm.getDefaultInstance();
        drinker = realm.where(BeerDrinker.class)
            .equalTo(BeerDrinkerFields.PIN, pin)
            .findFirst();

        updateDrinkerSummary();

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
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        drinker.setBeersRemoved(drinker.getBeersRemoved() + 1);
        realm.commitTransaction();

        showInteractionSummary();
    }

    public void addBeers(int numAdded){
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        drinker.setBeersAdded(drinker.getBeersAdded() + numAdded);
        realm.commitTransaction();

        showInteractionSummary();
    }

    public void showInteractionSummary(){
        updateDrinkerSummary();
        //TODO earned badges?

        //Exit back to main menu after 5 seconds
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 5000);
    }

    private void updateDrinkerSummary(){
        TextView beerTotalText = (TextView)findViewById(R.id.textBeerNet);
        beerTotalText.setText(String.valueOf(drinker.getNetBeers()));

        TextView beerTotalLabel = (TextView)findViewById(R.id.textNetBeersLabel);
        beerTotalLabel.setText( (drinker.getNetBeers() < 0) ? NEGATIVE_NET_BEERS : POSITIVE_NET_BEERS );

        TextView beersAddedText = (TextView)findViewById(R.id.textBeersAdded);
        beersAddedText.setText(String.valueOf(drinker.getBeersAdded()));

        TextView beersDrankText = (TextView)findViewById(R.id.textBeersDrank);
        beersDrankText.setText(String.valueOf(drinker.getBeersRemoved()));
    }

}
