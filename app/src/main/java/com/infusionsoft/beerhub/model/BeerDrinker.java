package com.infusionsoft.beerhub.model;

/**
 * Created by stuart-hostler on 10/27/16.
 */

public class BeerDrinker {

    //The net number of beers (added minus removed)
    private int netBeers;

    private int beersAdded;

    private int beersRemoved;

    private String pin;

    private String nickName;

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

    public BeerDrinker() {
    }

    public BeerDrinker(int beersAdded, int beersRemoved) {
        this.beersAdded = beersAdded;
        this.beersRemoved = beersRemoved;
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
