package com.infusionsoft.beerhub;

import android.databinding.BaseObservable;

import com.infusionsoft.beerhub.model.Achievement;
import com.infusionsoft.beerhub.model.BeerDrinker;

import java.util.ArrayList;
import java.util.List;

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

    public String getAchievements() {
        List<String> achievementList = drinker != null ? drinker.getAchievements(): new ArrayList<String>();
        String achievementsEarned;

        StringBuilder sb = new StringBuilder();
        for (String s : achievementList) {
            sb.append(", ").append(Achievement.valueOf(s).getName());
        }

        if (sb.length() > 0) {
            return sb.substring(2); // remove leading separator
        }
        return "";
    }

}
