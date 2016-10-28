package com.infusionsoft.beerhub;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;
import com.infusionsoft.beerhub.model.BeerDrinker;
import com.infusionsoft.beerhub.model.BeerDrinkerFields;

import io.realm.Realm;

public class RegistrationActivity extends AppCompatActivity {

    private BeerDrinker newDrinker = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        final PinLockView mRegisterPinView = (PinLockView) findViewById(R.id.pin_register_view);
        IndicatorDots mIndicatorDots = (IndicatorDots) findViewById(R.id.indicator_dots_register);

        mRegisterPinView.attachIndicatorDots(mIndicatorDots);
        mRegisterPinView.setPinLockListener(new PinLockListener() {

            private String firstPin = null;
            private String confirmPin = null;

            @Override
            public void onComplete(String pin) {
                if (firstPin == null) {
                    firstPin = pin;
                    mRegisterPinView.resetPinLockView();
                    Toast.makeText(getApplicationContext(), "Please re-enter your PIN to confirm.", Toast.LENGTH_LONG).show();
                } else if (confirmPin == null) {
                    confirmPin = pin;

                    if (!confirmPin.equals(firstPin)) {
                        Toast.makeText(getApplicationContext(), "Pins did not match, try again!", Toast.LENGTH_LONG).show();
                        firstPin = null;
                        confirmPin = null;
                        mRegisterPinView.resetPinLockView();

                    } else {
                        Realm realm = Realm.getDefaultInstance();
                        BeerDrinker found = realm.where(BeerDrinker.class).equalTo(BeerDrinkerFields.PIN, pin).findFirst();

                        if (found != null) {
                            Toast.makeText(getApplicationContext(), "Pin is already in use!", Toast.LENGTH_LONG).show();
                            firstPin = null;
                            confirmPin = null;
                            mRegisterPinView.resetPinLockView();
                        } else {
                            TextView editNickname = (TextView) findViewById(R.id.editNickname);
                            String nickname = editNickname.getText().toString();

                            newDrinker = new BeerDrinker();
                            newDrinker.setPin(pin);
                            newDrinker.setNickName(nickname);

                            realm.beginTransaction();
                            realm.copyToRealm(newDrinker);
                            realm.commitTransaction();

                            Intent intent = new Intent(getApplicationContext(), PersonalDashboardActivity.class);
                            intent.putExtra(PersonalDashboardActivity.PIN_KEY, pin);
                            startActivity(intent);

                            finish();
                        }
                    }
                }

            }

            @Override
            public void onEmpty() {

            }

            @Override
            public void onPinChange(int pinLength, String intermediatePin) {

            }
        });

        mRegisterPinView.setPinLength(5);
        mRegisterPinView.setTextColor(getResources().getColor(R.color.white));
    }
}
