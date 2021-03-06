package com.example.android.moneyanalytics.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.android.moneyanalytics.R;

/**
 * Detail activity that displays the correct fragment according to the user selection.
 */
public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Enable the up button to navigate up to the Main Activity.
        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Get the intent extra which will be used to launch the correct fragment.
        Intent intent = getIntent();
        if (intent != null) {
            String fragmentName = intent.getStringExtra(MainActivity.DETAIL_ACTIVITY_KEY);
            Long startDate = intent.getLongExtra(MainActivity.START_DATE_KEY, 0L);
            Long endDate = intent.getLongExtra(MainActivity.END_DATE_KEY, 0L);

            // Set the title of the activity accordingly to the option that was selected by the user.
            setTitle(fragmentName);

            // Here we launch the fragment for the option that was selected by the user.
            if (fragmentName.equals(getString(R.string.nav_drawer_income_string))) {
                IncomeFragment incomeFragment = IncomeFragment.newInstance(startDate, endDate);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.detail_activity_frame_layout, incomeFragment)
                        .commit();
            } else if (fragmentName.equals(getString(R.string.nav_drawer_expenses_string))) {
                ExpenseFragment expenseFragment = ExpenseFragment.newInstance(startDate, endDate);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.detail_activity_frame_layout, expenseFragment)
                        .commit();
            } else if (fragmentName.equals(getString(R.string.nav_drawer_savings_string))) {
                SavingsFragment savingsFragment = new SavingsFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.detail_activity_frame_layout, savingsFragment)
                        .commit();
            }

        }
    }
}