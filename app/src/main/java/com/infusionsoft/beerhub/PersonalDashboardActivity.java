package com.infusionsoft.beerhub;

import android.app.Dialog;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

    private static final long IDLE_TIMER = 30 * 1000; // 30 seconds
    private static final long KICKBACK_TIMER = 5 * 1000; // 5 seconds

    private BeerDrinker drinker;

    private Handler handler;
    private Runnable finishHandler;

    private MediaPlayer mAddOneBeer;
    private MediaPlayer mAddSixBeers;
    private MediaPlayer mTakeOneBeer;
    private MediaPlayer mNetNegativeSixBeers;
    private MediaPlayer mAchievementAchieved;
    private MediaPlayer mNoAchievementsHorn;

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
        this.mNoAchievementsHorn = MediaPlayer.create(this, R.raw.price_is_right_you_lose);

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
        resetFinishHandler(IDLE_TIMER);
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(finishHandler);
        finishHandler = null;
    }

    @Override
    protected void onDestroy() {
        mAddOneBeer.stop();
        mAddSixBeers.stop();
        mTakeOneBeer.stop();
        mNetNegativeSixBeers.stop();
        mAchievementAchieved.stop();
        mNoAchievementsHorn.stop();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_view_achievements) {

            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.achievement_list);

            List<String> achievementsText = new ArrayList<>();
            for (String achievement : drinker.getAchievements()) {
                achievementsText.add(Achievement.valueOf(achievement).getName() + " - " + Achievement.valueOf(achievement).getDescription());
            }

            if (achievementsText.size() == 0) {
                achievementsText.add("No Achievements :(");
                mNoAchievementsHorn.start();
            }
            
            ArrayAdapter<String> achievementAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, achievementsText);

            ListView achievementList = (ListView) dialog.findViewById(R.id.achievementListView);
            achievementList.setAdapter(achievementAdapter);

            dialog.setCancelable(true);
            dialog.setTitle("Your Achievements");
            dialog.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
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

        long delay = KICKBACK_TIMER;

        List<String> achievements = Achievement.getAchievedAchievements(drinker.getBeersAdded(),
            drinker.getBeersRemoved(), true);
        List<String> croppedAchList = new ArrayList<>();
        for (String achievement : achievements) {
            if (!drinker.getAchievements().contains(achievement)) {
                croppedAchList.add(achievement);
                Toast.makeText(getApplicationContext(), Achievement.valueOf(achievement).getName() + " Achievement Earned!\n" + Achievement.valueOf(achievement).getDescription(), Toast.LENGTH_LONG).show();
                mAchievementAchieved.start();
                delay = IDLE_TIMER;
            }
        }

        realm.beginTransaction();
        List<String> oldAchievements = drinker.getAchievements();
        oldAchievements.addAll(croppedAchList);
        drinker.setAchievements(oldAchievements);
        realm.commitTransaction();

        showInteractionSummary(delay);
    }

    public void addBeers(int numAdded) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        drinker.setBeersAdded(drinker.getBeersAdded() + numAdded);
        realm.commitTransaction();

        long delay = KICKBACK_TIMER;

        List<String> achievements = Achievement.getAchievedAchievements(drinker.getBeersAdded(),
            drinker.getBeersRemoved(), false);
        List<String> croppedAchList = new ArrayList<>();
        for (String achievement : achievements) {
            if (!drinker.getAchievements().contains(achievement)) {
                croppedAchList.add(achievement);
                Toast.makeText(getApplicationContext(), Achievement.valueOf(achievement).getName() + " Achievement Earned!\n" + Achievement.valueOf(achievement).getDescription(), Toast.LENGTH_LONG).show();
                mAchievementAchieved.start();
                delay = IDLE_TIMER;
            }
        }

        realm.beginTransaction();
        List<String> oldAchievements = drinker.getAchievements();
        oldAchievements.addAll(croppedAchList);
        drinker.setAchievements(oldAchievements);
        realm.commitTransaction();

        showInteractionSummary(delay);
    }

    public void showInteractionSummary(long delay) {
        updateDrinkerSummary();
        resetFinishHandler(delay);
    }

    private void resetFinishHandler(long delay) {
        handler.removeCallbacks(finishHandler);
        finishHandler = new Runnable() {
            @Override
            public void run() {
                finish();
            }
        };
        handler.postDelayed(finishHandler, delay);
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
