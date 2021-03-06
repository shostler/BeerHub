package com.infusionsoft.beerhub.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by stuart-hostler on 10/27/16.
 */

public class BeerDrinker extends RealmObject {

    //The net number of beers (added minus removed)
    private int netBeers;

    private int beersAdded;

    private int beersRemoved;

    @PrimaryKey
    private String pin;

    private String nickName;

    private String achievements;

    public BeerDrinker() {
    }

    public BeerDrinker(int beersAdded, int beersRemoved) {
        this.beersAdded = beersAdded;
        this.beersRemoved = beersRemoved;
        this.updateNetBeers();
    }

    public static BeerDrinker create(String nickName, String pin, int beersAdded, int beersRemoved) {
        BeerDrinker drinker = new BeerDrinker(beersAdded, beersRemoved);
        drinker.setNickName(nickName);
        drinker.setPin(pin);
        return drinker;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getNetBeers() {
        return netBeers;
    }

    private void setNetBeers(int netBeers) {
        this.netBeers = netBeers;
    }

    void updateNetBeers() {
         setNetBeers(this.beersAdded - this.beersRemoved);
    }

    public int getBeersAdded() {
        return beersAdded;
    }

    public void setBeersAdded(int beersAdded) {
        this.beersAdded = beersAdded;
        updateNetBeers();
    }

    public int getBeersRemoved() {
        return beersRemoved;
    }

    public void setBeersRemoved(int beersRemoved) {
        this.beersRemoved = beersRemoved;
        updateNetBeers();
    }

    public List<String> getAchievements() {
        if(achievements == null){
            return new ArrayList<>();
        }
        List<String> achieveList = new ArrayList<>(Arrays.asList(achievements.split(",")));
        return achieveList;
    }

    public void setAchievements(List<String> achievements) {
        StringBuilder sb = new StringBuilder();
        for (String s : achievements) {
            sb.append(",").append(s);
        }

        if (sb.length() > 0) {
            this.achievements = sb.substring(1); // remove leading separator
        }
    }
}
