package com.infusionsoft.beerhub;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;
import com.infusionsoft.beerhub.model.BeerDrinker;
import com.infusionsoft.beerhub.model.BeerDrinkerFields;
import io.realm.Realm;

/**
 * File description here...
 */
public class SigninFragment extends Fragment {

    private PinLockView pinLockView;
    private IndicatorDots indicatorDots;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pin_input_layout, container, false);

        pinLockView = (PinLockView) view.findViewById(R.id.pin_lock_view);
        indicatorDots = (IndicatorDots) view.findViewById(R.id.indicator_dots);

        pinLockView.attachIndicatorDots(indicatorDots);
        pinLockView.setPinLockListener(new PinLockListener() {
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

                pinLockView.resetPinLockView();
                if (drinker != null) {
                    clickSignIn(drinker.getPin());
                } else {
                    Toast.makeText(getActivity(), "Unknown user!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onEmpty() {

            }

            @Override
            public void onPinChange(int pinLength, String intermediatePin) {
            }
        });

        pinLockView.setPinLength(6);

        return view;
    }

    private void clickSignIn(String pin) {
        Intent intent = new Intent(getActivity(), PersonalDashboardActivity.class);
        intent.putExtra(PersonalDashboardActivity.PIN_KEY, pin);
        startActivity(intent);
    }

}
