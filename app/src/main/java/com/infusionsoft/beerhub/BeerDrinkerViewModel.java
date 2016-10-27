package com.infusionsoft.beerhub;

import android.databinding.BaseObservable;
import com.infusionsoft.beerhub.model.BeerDrinker;

/**
 * File description here...
 */
public class BeerDrinkerViewModel extends BaseObservable {

    private BeerDrinker drinker;

    public void setDrinker(BeerDrinker drinker) {
        this.drinker = drinker;
        notifyChange();
    }

    public String getNickName() {
        String nickName = drinker != null ? drinker.getNickName() : "";
        return nickName;
    }
}
