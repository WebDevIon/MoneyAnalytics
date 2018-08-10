package com.example.android.moneyanalytics.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.moneyanalytics.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        setTitle(R.string.nav_drawer_about_string);

        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
}
