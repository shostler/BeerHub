package com.infusionsoft.beerhub;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class PersonalDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_dashboard);
        //TODO show current totals on startup?
    }

    public void clickAddCase(View view) {
        addBeers(12);
    }

    public void clickAddSixPack(View view) {
        addBeers(6);
    }

    public void clickAddBeer(View view) {
        addBeers(1);
    }

    public void clickTakeBeer(View view) {
        addBeers(-1);
    }

    public void addBeers(int numAdded){
        //TODO get the current user and update the beers
        showInteractionSummary();
    }

    public void showInteractionSummary(){
        //TODO new NetBeers
        //TODO earned badges?
        //TODO drop back to mainActitivy
    }

}
