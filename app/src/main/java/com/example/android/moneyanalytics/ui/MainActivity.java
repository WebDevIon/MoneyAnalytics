package com.example.android.moneyanalytics.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.moneyanalytics.R;
import com.example.android.moneyanalytics.chart.PieChartData;
import com.razerdp.widget.animatedpieview.AnimatedPieView;
import com.razerdp.widget.animatedpieview.AnimatedPieViewConfig;
import com.razerdp.widget.animatedpieview.data.SimplePieInfo;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        DatePickerFragment.OnCompleteListener{

    public static final String DETAIL_ACTIVITY_KEY = "detail activity";
    public static final String ADD_INCOME_ACTIVITY_KEY = "add income activity";
    private TextView mPeriodTv;
    private Button mAddIncome, mAddExpense;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mPeriodTv = findViewById(R.id.main_period_tv);
        mAddIncome = findViewById(R.id.add_income_button);
        mAddExpense = findViewById(R.id.add_expense_button);

        mAddIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddIncomeActivity.class);
                startActivity(intent);
            }
        });

        mAddExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddExpenseActivity.class);
                startActivity(intent);
            }
        });

        // Here we initialize the drawer and we handle it's states (open or closed)
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //TODO: Remove dummy data for testing purposes only
        AnimatedPieView mAnimatedPieView = findViewById(R.id.main_activity_pie_view);
        AnimatedPieViewConfig config = new AnimatedPieViewConfig();
        config.startAngle(-90)
                .addData(new SimplePieInfo(10, getColor(R.color.colorPrimary), "Other"))
                .addData(new SimplePieInfo(60, getColor(R.color.colorPrimaryDark), "Food"))
                .addData(new PieChartData(30, getResources().getColor(R.color.colorAccent), "Car"))
                .strokeWidth(200)
                .canTouch(false)
                .drawText(true)
                .textSize(80)
                .textMargin(8)
                .guidePointRadius(8)
                .guideLineWidth(6)
                .textGravity(AnimatedPieViewConfig.ECTOPIC)
                .duration(700);

        mAnimatedPieView.start(config);
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

            mPeriodTv.setText(R.string.nav_drawer_today_string);

        } else if (id == R.id.nav_this_week) {

            mPeriodTv.setText(R.string.nav_drawer_week_string);

        } else if (id == R.id.nav_this_month) {

            mPeriodTv.setText(R.string.nav_drawer_month_string);

        } else if (id == R.id.nav_pick_a_date) {

            DialogFragment datePickerFragment = new DatePickerFragment();
            datePickerFragment.show(getSupportFragmentManager(), "datePicker");

        } else if (id == R.id.nav_about) {

            Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
            startActivity(intent);

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onDatePicked(String date, Long timeInMillis) {
        mPeriodTv.setText(date);
        Toast.makeText(this, timeInMillis.toString(), Toast.LENGTH_SHORT).show();
    }
}