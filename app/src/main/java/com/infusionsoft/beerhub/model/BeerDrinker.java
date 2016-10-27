package com.infusionsoft.beerhub.model;

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

    public BeerDrinker() {
    }

    public BeerDrinker(int beersAdded, int beersRemoved) {
        this.beersAdded = beersAdded;
        this.beersRemoved = beersRemoved;
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

    public void setNetBeers(int netBeers) {
        this.netBeers = netBeers;
    }

    public int getBeersAdded() {
        return beersAdded;
    }

    public void setBeersAdded(int beersAdded) {
        this.beersAdded = beersAdded;
    }

    public int getBeersRemoved() {
        return beersRemoved;
    }

    public void setBeersRemoved(int beersRemoved) {
        this.beersRemoved = beersRemoved;
    }
}
