package com.example.android.moneyanalytics.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.android.moneyanalytics.R;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String DETAIL_ACTIVITY_KEY = "detail activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Here we initialize the drawer and we handle it's states (open or closed)
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    /**
     * Method used to close the Drawer if back is pressed.
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Method responsible for clicking on items from the Navigation Drawer.
     * For income, expenses and savings Detail Activity is launched and a string is passed
     * which is the retrieved in the activity so that it displays the proper fragment.
     * When a tracking period is clicked the Database will be queried accordingly to the user
     * selection.
     * For about the About Activity will vbe launched.
     * @param item the item that was clicked
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_income) {

            Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
            intent.putExtra(DETAIL_ACTIVITY_KEY, getString(R.string.nav_drawer_income_string));
            startActivity(intent);

        } else if (id == R.id.nav_expenses) {

            Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
            intent.putExtra(DETAIL_ACTIVITY_KEY, getString(R.string.nav_drawer_expenses_string));
            startActivity(intent);

        } else if (id == R.id.nav_savings) {

            Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
            intent.putExtra(DETAIL_ACTIVITY_KEY, getString(R.string.nav_drawer_savings_string));
            startActivity(intent);

        } else if (id == R.id.nav_today) {

        } else if (id == R.id.nav_this_week) {

        } else if (id == R.id.nav_this_month) {

        } else if (id == R.id.nav_pick_a_date) {

        } else if (id == R.id.nav_about) {

            Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
            startActivity(intent);

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
