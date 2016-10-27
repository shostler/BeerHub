package com.infusionsoft.beerhub;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void clickSignIn(View view) {

        //TODO verify the pin
        //TODO pullup the user of that pin
        //TODO pass user info to next page
        //TODO global for logged in user that gets wipped out when signing out
        Intent intent = new Intent(this, PersonalDashboardActivity.class);
        startActivity(intent);
    }
}
