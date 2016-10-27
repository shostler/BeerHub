package com.infusionsoft.beerhub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;
import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;
import com.infusionsoft.beerhub.model.BeerDrinker;
import com.infusionsoft.beerhub.model.BeerDrinkerFields;
import io.realm.Realm;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pin_input_layout);

        PinLockView mPinLockView = (PinLockView) findViewById(R.id.pin_lock_view);
        IndicatorDots mIndicatorDots = (IndicatorDots) findViewById(R.id.indicator_dots);

        mPinLockView.attachIndicatorDots(mIndicatorDots);
        mPinLockView.setPinLockListener(new PinLockListener() {
            private String last = null;

            @Override
            public void onComplete(String pin) {
                if (pin.equals(last)) {
                    return;
                }
                last = pin;
                Log.d("PinLockListener", "onComplete() called with: pin = [" + pin + "]");
                Realm realm = Realm.getDefaultInstance();
                BeerDrinker drinker = realm.where(BeerDrinker.class)
                    .equalTo(BeerDrinkerFields.PIN, pin)
                    .findFirst();

                if (drinker != null) {
                    clickSignIn(drinker.getPin());
                } else {
                    Toast.makeText(MainActivity.this, "Unknown user!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onEmpty() {

            }

            @Override
            public void onPinChange(int pinLength, String intermediatePin) {
            }
        });

        mPinLockView.setPinLength(6);
        mPinLockView.setTextColor(getResources().getColor(R.color.white));

    }

    private void clickSignIn(String pin) {

        //TODO verify the pin
        //TODO pullup the user of that pin
        //TODO pass user info to next page
        //TODO global for logged in user that gets wipped out when signing out
        Intent intent = new Intent(this, PersonalDashboardActivity.class);
        intent.putExtra(PersonalDashboardActivity.PIN_KEY, pin);
        startActivity(intent);
    }
}
