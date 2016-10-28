package com.infusionsoft.beerhub;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.infusionsoft.beerhub.model.Achievement;
import com.infusionsoft.beerhub.model.BeerDrinker;
import com.infusionsoft.beerhub.model.BeerDrinkerFields;
import io.realm.Realm;
import java.util.ArrayList;
import java.util.List;

public class PersonalDashboardActivity extends AppCompatActivity {

    public static final String PIN_KEY = "key.pin";
    private static final String NEGATIVE_NET_BEERS = "Beers in the hole!";
    private static final String POSITIVE_NET_BEERS = "Beers still to drink!";

    private BeerDrinker drinker;

    private Handler handler;
    private Runnable stopUpdatingHandler;

    private MediaPlayer mAddOneBeer;
    private MediaPlayer mAddSixBeers;
    private MediaPlayer mTakeOneBeer;
    private MediaPlayer mNetNegativeSixBeers;
    private MediaPlayer mAchievementAchieved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_dashboard);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitleMarginStart(
            getResources().getDimensionPixelSize(R.dimen.title_margin_start));
        setSupportActionBar(toolbar);

        this.handler = new Handler(Looper.getMainLooper());

        this.mAddOneBeer = MediaPlayer.create(this, R.raw.light_applause);
        this.mAddSixBeers = MediaPlayer.create(this, R.raw.moderate_applause);
        this.mTakeOneBeer = MediaPlayer.create(this, R.raw.beer_bottle_opening);
        this.mNetNegativeSixBeers = MediaPlayer.create(this, R.raw.boo);
        this.mAchievementAchieved = MediaPlayer.create(this, R.raw.achievement_achieved);

        String pin = getIntent().getStringExtra(PIN_KEY);
        Log.d("PersonalDashboard", pin);
        Realm realm = Realm.getDefaultInstance();
        drinker = realm.where(BeerDrinker.class)
            .equalTo(BeerDrinkerFields.PIN, pin)
            .findFirst();

        //toolbar.setTitle(drinker.getNickName());
        getSupportActionBar().setTitle(drinker.getNickName());
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_beer);


        updateDrinkerSummary();
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(stopUpdatingHandler);
        stopUpdatingHandler = null;
    }

    @Override
    protected void onDestroy() {
        mAddOneBeer.stop();
        mAddSixBeers.stop();
        mTakeOneBeer.stop();
        mNetNegativeSixBeers.stop();
        mAchievementAchieved.stop();
        super.onDestroy();
    }

    public void clickAddCase(View view) {
        addBeers(12);
    }

    public void clickAddSixPack(View view) {
        addBeers(6);

        mAddSixBeers.start();
    }

    public void clickAddBeer(View view) {
        addBeers(1);

        mAddOneBeer.start();
    }

    public void clickTakeBeer(View view) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        drinker.setBeersRemoved(drinker.getBeersRemoved() + 1);
        realm.commitTransaction();

        if (drinker.getNetBeers() < -6) {
            mNetNegativeSixBeers.start();
        } else {
            mTakeOneBeer.start();
        }

        List<String> achievements = Achievement.getAchievedAchievements(drinker.getBeersAdded(),
            drinker.getBeersRemoved());
        List<String> croppedAchList = new ArrayList<>();
        for (String achievement : achievements) {
            if (!drinker.getAchievements().contains(achievement)) {
                croppedAchList.add(achievement);
                Toast.makeText(getApplicationContext(), Achievement.valueOf(achievement).getName() + " Achievement Earned!\n" + Achievement.valueOf(achievement).getDescription(), Toast.LENGTH_LONG).show();
                mAchievementAchieved.start();
            }
        }

        realm.beginTransaction();
        List<String> oldAchievements = drinker.getAchievements();
        oldAchievements.addAll(croppedAchList);
        drinker.setAchievements(oldAchievements);
        realm.commitTransaction();

        showInteractionSummary();
    }

    public void addBeers(int numAdded) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        drinker.setBeersAdded(drinker.getBeersAdded() + numAdded);
        realm.commitTransaction();

        List<String> achievements = Achievement.getAchievedAchievements(drinker.getBeersAdded(),
            drinker.getBeersRemoved());
        List<String> croppedAchList = new ArrayList<>();
        for (String achievement : achievements) {
            if (!drinker.getAchievements().contains(achievement)) {
                croppedAchList.add(achievement);
                Toast.makeText(getApplicationContext(), Achievement.valueOf(achievement).getName() + " Achievement Earned!\n" + Achievement.valueOf(achievement).getDescription(), Toast.LENGTH_LONG).show();
                mAchievementAchieved.start();
            }
        }

        realm.beginTransaction();
        List<String> oldAchievements = drinker.getAchievements();
        oldAchievements.addAll(croppedAchList);
        drinker.setAchievements(oldAchievements);
        realm.commitTransaction();

        showInteractionSummary();
    }

    public void showInteractionSummary() {
        updateDrinkerSummary();
        //TODO earned badges?

        handler.removeCallbacks(stopUpdatingHandler);
        stopUpdatingHandler = new Runnable() {
            @Override
            public void run() {
                finish();
            }
        };
        handler.postDelayed(stopUpdatingHandler, 5000);
    }

    private void updateDrinkerSummary() {
        TextView beerTotalText = (TextView)findViewById(R.id.textBeerNet);
        beerTotalText.setText(String.valueOf(drinker.getNetBeers()));

        TextView beerTotalLabel = (TextView)findViewById(R.id.textNetBeersLabel);
        int netBeers = drinker.getNetBeers();
        if (netBeers < 0) {
            beerTotalText.setTextColor(ContextCompat.getColor(this, R.color.colorError));
            beerTotalLabel.setTextColor(ContextCompat.getColor(this, R.color.colorError));
            beerTotalLabel.setText(R.string.beers_in_the_hole);
        } else {
            beerTotalText.setTextColor(ContextCompat.getColor(this, R.color.colorGood));
            beerTotalLabel.setTextColor(ContextCompat.getColor(this, R.color.colorGood));
            if (netBeers == 0) {
                beerTotalLabel.setText(R.string.no_beers_to_drink);
            } else {
                beerTotalLabel.setText(
                    getResources().getQuantityString(R.plurals.beers_to_drink, netBeers));
            }
        }

        TextView beersAddedText = (TextView)findViewById(R.id.textBeersAdded);
        beersAddedText.setText(String.valueOf(drinker.getBeersAdded()));

        TextView beersDrankText = (TextView)findViewById(R.id.textBeersDrank);
        beersDrankText.setText(String.valueOf(drinker.getBeersRemoved()));
    }
}
