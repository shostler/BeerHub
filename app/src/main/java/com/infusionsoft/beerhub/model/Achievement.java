package com.infusionsoft.beerhub.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stuart-hostler on 10/27/16.
 */

public enum Achievement {

    BRONZE_DONATOR ("bronze donator", "Donated at least 12 beers"),
    SILVER_DONATOR ("silver donator", "Donated at least 36 beers"),
    GOLD_DONATOR ("gold donator", "Donated at least 120 beers"),
    LIGHTWEIGHT ("lightweight", "Drank at least 12 beers"),
    PROFESSIONAL ("professional", "Drank at least 36 beers"),
    LIVER_FAILURE ("liver failure", "Drank at least 120 beers"),
    LEECH("leech", "Reached -12 net beers"),
    MAJOR_PARASITE("major parasite", "Reached -36 net beers"),
    FREELOADING_JERK("freeloading jerk", "Reached -120 net beers")
    ;

    private final String name;
    private final String description;

    Achievement(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public static List<Achievement> getAchievedAchievements(int totalDonated, int totalDrank){
        int netTotal = totalDonated-totalDrank;
        List<Achievement> achieved = new ArrayList<>();


        //BRONZE_DONATOR
        if(totalDonated >= 12){
            achieved.add(BRONZE_DONATOR);
        }
        //SILVER_DONATOR
        if(totalDonated >= 36){
            achieved.add(SILVER_DONATOR);
        }
        //GOLD_DONATOR
        if(totalDonated >= 120){
            achieved.add(GOLD_DONATOR);
        }
        //LIGHTWEIGHT
        if(totalDrank >= 12){
            achieved.add(LIGHTWEIGHT);
        }
        //PROFESSIONAL
        if(totalDrank >= 36){
            achieved.add(PROFESSIONAL);
        }
        //LIVER_FAILURE
        if(totalDrank >= 120){
            achieved.add(LIVER_FAILURE);
        }
        //LEECH
        if(netTotal <= -12){
            achieved.add(LEECH);
        }
        //MAJOR_PARASITE
        if(netTotal <= -36){
            achieved.add(MAJOR_PARASITE);
        }
        //FREELOADING_JERK
        if(netTotal <= -120){
            achieved.add(FREELOADING_JERK);
        }

        return achieved;
    }
}
