package com.example.android.moneyanalytics.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.android.moneyanalytics.chart.DefaultPieConfig;
import com.example.android.moneyanalytics.chart.PieChartData;
import com.example.android.moneyanalytics.model.Entry;
import com.example.android.moneyanalytics.model.EntryByCategory;
import com.example.android.moneyanalytics.model.MainViewModel;
import com.example.android.moneyanalytics.room.AppExecutors;
import com.example.android.moneyanalytics.room.EntriesDatabase;
import com.example.android.moneyanalytics.utils.ColorUtils;
import com.example.android.moneyanalytics.utils.DateUtils;
import com.razerdp.widget.animatedpieview.AnimatedPieView;
import com.razerdp.widget.animatedpieview.AnimatedPieViewConfig;

import java.util.Date;
import java.util.List;

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
    public static final String START_DATE_KEY = "start date";
    public static final String END_DATE_KEY = "end date";
    public static final String PERIOD_TEXT_VIEW_KEY = "period";
    private TextView mPeriodTv, mBalanceTv;
    Button mAddIncome, mAddExpense;
    private Entry mRecurringEntry;
    private Long mStartDate = new Date().getTime();
    private Long mEndDate = new Date().getTime();
    private AnimatedPieView mAnimatedPieView;
    private EntriesDatabase mDb;

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

        mDb = EntriesDatabase.getInstance(getApplicationContext());

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

        // Here we set up the ViewModel and we pass the checkRecurring parameter as true to
        // check for recurring entries that need to be updated.
        setupViewModel(mStartDate, mEndDate, true);
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

        if (prefs.getLong(END_DATE_KEY, new Date().getTime()) < new DateUtils(new Date().getTime()).getMidnightDate()) {
            mEndDate = prefs.getLong(END_DATE_KEY, new Date().getTime());
        } else {
            mEndDate = new Date().getTime();
        }
        setupViewModel(mStartDate, mEndDate, false);
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
            intent.putExtra(START_DATE_KEY, mStartDate);
            intent.putExtra(END_DATE_KEY, mEndDate);
            startActivity(intent);

        } else if (id == R.id.nav_expenses) {

            Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
            intent.putExtra(DETAIL_ACTIVITY_KEY, getString(R.string.nav_drawer_expenses_string));
            intent.putExtra(START_DATE_KEY, mStartDate);
            intent.putExtra(END_DATE_KEY, mEndDate);
            startActivity(intent);

        } else if (id == R.id.nav_savings) {

            Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
            intent.putExtra(DETAIL_ACTIVITY_KEY, getString(R.string.nav_drawer_savings_string));
            intent.putExtra(START_DATE_KEY, mStartDate);
            intent.putExtra(END_DATE_KEY, mEndDate);
            startActivity(intent);

        } else if (id == R.id.nav_today) {

            mPeriodTv.setText(R.string.nav_drawer_today_string);
            mEndDate = new Date().getTime();
            DateUtils dateUtils = new DateUtils(mEndDate);
            mStartDate = dateUtils.getMidnightDate();
            setupViewModel(mStartDate, mEndDate, false);

        } else if (id == R.id.nav_this_week) {

            mPeriodTv.setText(R.string.nav_drawer_week_string);
            mEndDate = new Date().getTime();
            DateUtils dateUtils = new DateUtils(mEndDate);
            mStartDate = dateUtils.getAWeekAgoDate();
            setupViewModel(mStartDate, mEndDate, false);

        } else if (id == R.id.nav_this_month) {

            mPeriodTv.setText(R.string.nav_drawer_month_string);
            mEndDate = new Date().getTime();
            DateUtils dateUtils = new DateUtils(mEndDate);
            mStartDate = dateUtils.getFirstDayOfMonthDate();
            setupViewModel(mStartDate, mEndDate, false);

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
        DateUtils dateUtils = new DateUtils(timeInMillis);
        mStartDate = dateUtils.getASpecificDayDate();
        mEndDate = mStartDate + 86400000;
        setupViewModel(mStartDate, mEndDate, false);
    }

    /**
     * Method used to set up the View Model.
     * @param startDate the start date used in the queries.
     * @param endDate the end date used in the queries.
     */
    private void setupViewModel(Long startDate, final Long endDate, boolean checkRecurring) {
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getEntriesGroupedByCategory(startDate, endDate)
                .observe(this, new Observer<List<EntryByCategory>>() {
            @Override
            public void onChanged(@Nullable List<EntryByCategory> entries) {
                Log.d(TAG, "Updating list of tasks from LiveData in MainViewModel");
                if (entries != null) {
                    processEntries(entries);
                } else {
                    Log.d(TAG, "Query returned no results.");
                }
            }
        });
        if (checkRecurring) {
            Log.d(TAG, "Checking for recurring entries.");
            viewModel.getRecurringEntries(true)
                    .observe(this, new Observer<List<Entry>>() {
                        @Override
                        public void onChanged(@Nullable List<Entry> entries) {
                            if (entries != null) {
                                Long currentDate = new Date().getTime();
                                DateUtils dateUtils = new DateUtils(currentDate);
                                Long targetDate = dateUtils.getTimeAMonthAgo();
                                for (Entry entry : entries) {
                                    if (entry.getDate() < targetDate) {
                                        recreateEntry(entry);
                                        Log.d(TAG, "Updating recurring entry.");
                                    }
                                }
                            } else {
                                Log.d(TAG, "No recurring entries updated.");
                            }
                        }
                    });
        }

    }

    /**
     * This method is used to populate the Pie Chart and calculate the balance.
     * @param entries the list of entries passed by the ViewModel.
     */
    private void processEntries(List<EntryByCategory> entries) {
        Log.d(TAG, "EntryByCategory length: " + entries.size());

        ColorUtils colorUtils = new ColorUtils(getApplicationContext());
        Double totalIncome = 0d;
        Double totalExpense = 0d;
        AnimatedPieViewConfig mPieConfig = new DefaultPieConfig().getDefaultPieConfig(false);

        for (EntryByCategory entry : entries) {
            Log.d(TAG, "Entry category: " + entry.getCategory());
            Log.d(TAG, "Entry amount: " + entry.getAmount());
            Log.d(TAG, "Entry type: " + entry.getType());
            if (entry.getType().equals(AddExpenseActivity.DATA_EXPENSE_TYPE_KEY)) {
                totalExpense += entry.getAmount();

                mPieConfig.addData(new PieChartData (entry.getAmount(),
                        colorUtils.getColor(entry.getCategory()), entry.getCategory()));

            } else {
                totalIncome += entry.getAmount();
            }
        }

        mAnimatedPieView.start(mPieConfig);

        Double balance = totalIncome - totalExpense;
        String balanceStr = balance.toString();
        mBalanceTv.setText(balanceStr);

        Log.d(TAG, "EntryByCategory totalExpense: " + totalExpense);
        Log.d(TAG, "EntryByCategory totalIncome: " + totalIncome);
    }

    /**
     * Method used to update the recurring entries in the database.
     * @param entry the entry that is updated.
     */
    private void recreateEntry(final Entry entry){
        int entryId = entry.getId();
        Log.d(TAG, "Id of the recurring entry is: " + entryId);

        DateUtils dateUtils = new DateUtils(entry.getDate());
        Log.d(TAG, "Recurring entry initial date: " + entry.getDate());
        Long entryUpdatedDate = dateUtils.updateRecurringDate();
        Log.d(TAG, "Recurring entry updated date: " + entryUpdatedDate);

        mRecurringEntry = new Entry(entry.getAmount(), entry.getName(), entry.getCategory(),
                entry.isRecurring(), entry.getDate(), entry.getType());
        mRecurringEntry.setDate(entryUpdatedDate);

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.entriesDao().insertTask(mRecurringEntry);

                Log.d(TAG, "Recurring entry inserted: " + mRecurringEntry.getName());

                entry.setRecurring(false);
                mDb.entriesDao().updateTask(entry);

                Log.d(TAG, "Recurring entry updated: " + mRecurringEntry.getName());
            }
        });
    }
}