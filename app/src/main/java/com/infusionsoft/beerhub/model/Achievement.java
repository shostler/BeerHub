package com.infusionsoft.beerhub.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stuart-hostler on 10/27/16.
 */

public enum Achievement {

    BRONZE_DONATOR ("Bronze Donator", "Donated at least 12 beers"),
    SILVER_DONATOR ("Silver Donator", "Donated at least 36 beers"),
    GOLD_DONATOR ("Gold Donator", "Donated at least 120 beers"),
    LIGHTWEIGHT ("Lightweight", "Drank at least 12 beers"),
    PROFESSIONAL ("Professional", "Drank at least 36 beers"),
    LIVER_FAILURE ("Liver Failure", "Drank at least 120 beers"),
    LEECH("Leech", "Reached -12 net beers"),
    MAJOR_PARASITE("Major Parasite", "Reached -36 net beers"),
    FREELOADING_JERK("Freeloading Jerk", "Reached -120 net beers")
    ;

    private final String name;
    private final String description;

    Achievement(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public static List<String> getAchievedAchievements(int totalDonated, int totalDrank){
        int netTotal = totalDonated-totalDrank;
        List<String> achieved = new ArrayList<>();


        //BRONZE_DONATOR
        if(totalDonated >= 12){
            achieved.add(BRONZE_DONATOR.name());
        }
        //SILVER_DONATOR
        if(totalDonated >= 36){
            achieved.add(SILVER_DONATOR.name());
        }
        //GOLD_DONATOR
        if(totalDonated >= 120){
            achieved.add(GOLD_DONATOR.name());
        }
        //LIGHTWEIGHT
        if(totalDrank >= 12){
            achieved.add(LIGHTWEIGHT.name());
        }
        //PROFESSIONAL
        if(totalDrank >= 36){
            achieved.add(PROFESSIONAL.name());
        }
        //LIVER_FAILURE
        if(totalDrank >= 120){
            achieved.add(LIVER_FAILURE.name());
        }
        //LEECH
        if(netTotal <= -12){
            achieved.add(LEECH.name());
        }
        //MAJOR_PARASITE
        if(netTotal <= -36){
            achieved.add(MAJOR_PARASITE.name());
        }
        //FREELOADING_JERK
        if(netTotal <= -120){
            achieved.add(FREELOADING_JERK.name());
        }

        return achieved;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
