package com.example.android.moneyanalytics.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.moneyanalytics.R;
import com.example.android.moneyanalytics.chart.PieChartData;
import com.example.android.moneyanalytics.model.Entry;
import com.example.android.moneyanalytics.model.EntryByCategory;
import com.example.android.moneyanalytics.model.MainViewModel;
import com.razerdp.widget.animatedpieview.AnimatedPieView;
import com.razerdp.widget.animatedpieview.AnimatedPieViewConfig;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Main activity of the app which displays expenses and the balance
 * available for a selected period of time.
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        DatePickerFragment.OnCompleteListener{

    public static final String TAG = MainActivity.class.getSimpleName();
    public static final String PREFS_NAME = "Main Activity";
    public static final String DETAIL_ACTIVITY_KEY = "detail activity";
    public static final String ADD_INCOME_ACTIVITY_KEY = "add income activity";
    public static final String START_DATE_KEY = "start date";
    public static final String END_DATE_KEY = "end date";
    public static final String PERIOD_TEXT_VIEW_KEY = "period";
    private TextView mPeriodTv, mBalanceTv;
    Button mAddIncome, mAddExpense;
    private List<EntryByCategory> mEntries;
    private Long mStartDate = new Date().getTime();
    private Long mEndDate = new Date().getTime();
    private AnimatedPieView mAnimatedPieView;
    private AnimatedPieViewConfig mPieConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Here we initialize the views that we use in this activity.
        mPeriodTv = findViewById(R.id.main_period_tv);
        mBalanceTv = findViewById(R.id.main_activity_balance_value_tv);
        mAddIncome = findViewById(R.id.add_income_button);
        mAddExpense = findViewById(R.id.add_expense_button);
        mAnimatedPieView = findViewById(R.id.main_activity_pie_view);

        // Add income button functionality.
        // We launch the add income activity when the button is clicked.
        mAddIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddIncomeActivity.class);
                startActivity(intent);
            }
        });

        // Add expense button functionality.
        // We launch the add expense activity when the button is clicked.
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

        setupViewModel(mStartDate, mEndDate);
    }

    // Here we save the data that we want to keep during screen rotation,
    // changing activities or leaving the app and returning to it at another time.
    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor prefs =
                getApplicationContext().getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PERIOD_TEXT_VIEW_KEY, mPeriodTv.getText().toString());
        prefs.putLong(START_DATE_KEY, mStartDate);
        prefs.putLong(END_DATE_KEY, mEndDate);
        prefs.apply();
    }

    // Here we restore the data that we saved in onPause.
    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        mPeriodTv.setText(prefs.getString(PERIOD_TEXT_VIEW_KEY,
                getResources().getString(R.string.spinner_period_prompt)));
        mStartDate = prefs.getLong(START_DATE_KEY, new Date().getTime());
        mEndDate = prefs.getLong(END_DATE_KEY, new Date().getTime());
        setupViewModel(mStartDate, mEndDate);
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
     * For about the About Activity will be launched.
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
            mEndDate = new Date().getTime();
            mStartDate = mEndDate - mEndDate % (24 * 60 * 60 * 1000);
            setupViewModel(mStartDate, mEndDate);

        } else if (id == R.id.nav_this_week) {

            mPeriodTv.setText(R.string.nav_drawer_week_string);
            mEndDate = new Date().getTime();
            mStartDate = mEndDate - 604800000;
            setupViewModel(mStartDate, mEndDate);

        } else if (id == R.id.nav_this_month) {

            mPeriodTv.setText(R.string.nav_drawer_month_string);
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.DAY_OF_MONTH, 1);
            mEndDate = new Date().getTime();
            mStartDate = cal.getTimeInMillis() - mEndDate % (24 * 60 * 60 * 1000);
            setupViewModel(mStartDate, mEndDate);

            Log.d(TAG, "Start date: " + mStartDate + "\nEndDate: " + mEndDate);

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

    /**
     * This method returns the date that was selected in the calendar.
     * @param date the string that will be passed to the text view which displays the period.
     * @param timeInMillis the date that was selected in milliseconds.
     */
    @Override
    public void onDatePicked(String date, Long timeInMillis) {
        mPeriodTv.setText(date);
        Long timeFromMidnight = new Date().getTime() % (24 * 60 * 60 * 1000);
        if (timeInMillis > timeInMillis - timeFromMidnight) {
            mStartDate = timeInMillis - timeFromMidnight;
        } else {
            mStartDate = timeInMillis;
        }
        mEndDate = timeInMillis + 86400000 - timeFromMidnight;
        setupViewModel(mStartDate, mEndDate);
    }

    /**
     * Method used to set up the View Model.
     * @param startDate the start date used in the queries.
     * @param endDate the end date used in the queries.
     */
    private void setupViewModel(Long startDate, final Long endDate) {
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getEntriesGroupedByCategory(startDate, endDate)
                .observe(this, new Observer<List<EntryByCategory>>() {
            @Override
            public void onChanged(@Nullable List<EntryByCategory> entries) {
                Log.d(TAG, "Updating list of tasks from LiveData in MainViewModel");
                mEntries = entries;
                if (entries != null) {
                    processEntries(entries);
                } else {
                    Log.d(TAG, "Query returned no results.");
                }
            }
        });
        viewModel.getEntriesByDate(startDate, endDate)
                .observe(this, new Observer<List<Entry>>() {
                    @Override
                    public void onChanged(@Nullable List<Entry> entries) {
                        if (entries != null) {
                            Log.d(TAG, "Normal Entry size: " + entries.size());

                            Double balance = 0d;
                            Long date = 0L;
                            boolean isIncome = false;

                            for (Entry entry : entries) {
                                if (entry.getType().equals(AddIncomeActivity.DATA_INCOME_TYPE_KEY)) {
                                    isIncome = true;
                                    break;
                                }
                            }

                            if (isIncome) {
                                for (Entry entry : entries) {
                                    if (entry.getType().equals(AddIncomeActivity
                                            .DATA_INCOME_TYPE_KEY)) {
                                        balance += entry.getAmount();
                                        date = entry.getDate();
                                    } else if (entry.getType().equals(AddExpenseActivity
                                            .DATA_EXPENSE_TYPE_KEY)) {
                                        if (entry.getDate() > date) {
                                            balance -= entry.getAmount();
                                        }
                                    }
                                }
                            } else {
                                for (Entry entry : entries) {
                                    balance -= entry.getAmount();
                                }
                            }

                            String balanceStr = balance.toString();
                            mBalanceTv.setText(balanceStr);

                        } else {
                            Log.d(TAG, "Normal Entry null!");
                        }

                    }
                });
    }

    private void processEntries(List<EntryByCategory> entries) {
        Log.d(TAG, "EntryByCategory length: " + entries.size());

        Double totalIncome = 0d;
        Double totalExpense = 0d;
        mPieConfig = new AnimatedPieViewConfig();
        mPieConfig.startAngle(-90)
                .strokeWidth(200)
                .canTouch(false)
                .drawText(true)
                .textSize(80)
                .textMargin(8)
                .guidePointRadius(8)
                .guideLineWidth(6)
                .textGravity(AnimatedPieViewConfig.ECTOPIC)
                .duration(700);

        for (EntryByCategory entry : entries) {
            Log.d(TAG, "Entry category: " + entry.getCategory());
            Log.d(TAG, "Entry amount: " + entry.getAmount());
            Log.d(TAG, "Entry type: " + entry.getType());
            if (entry.getType().equals(AddExpenseActivity.DATA_EXPENSE_TYPE_KEY)) {
                totalExpense += entry.getAmount();

                mPieConfig.addData(new PieChartData
                        (entry.getAmount(), getRandomColor(), entry.getCategory()));

            } else {
                totalIncome += entry.getAmount();
            }
        }

        mAnimatedPieView.start(mPieConfig);

//        Double balance = totalIncome - totalExpense;
//        String balanceStr = balance.toString();
//        mBalanceTv.setText(balanceStr);

        Log.d(TAG, "EntryByCategory totalExpense: " + totalExpense);
        Log.d(TAG, "EntryByCategory totalIncome: " + totalIncome);
    }

    private int getRandomColor() {
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256),
                rnd.nextInt(256), rnd.nextInt(256));
    }
}