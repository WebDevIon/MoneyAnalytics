package com.example.android.moneyanalytics.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.android.moneyanalytics.R;

/**
 * Detail activity that displays the correct fragment according to the user selection.
 */
public class DetailActivity extends AppCompatActivity implements
        IncomeFragment.OnFragmentInteractionListener,
        ExpenseFragment.OnFragmentInteractionListener,
        SavingsFragment.OnFragmentInteractionListener {

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
            Toast.makeText(this, fragmentName + " was clicked!", Toast.LENGTH_LONG).show();

            // Set the title of the activity accordingly to the option that was selected by the user.
            setTitle(fragmentName);

            // Here we launch the fragment for the option that was selected by the user.
            if (fragmentName.equals(getString(R.string.nav_drawer_income_string))) {
                IncomeFragment incomeFragment = IncomeFragment.newInstance(fragmentName, "hello");
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.detail_activity_frame_layout, incomeFragment)
                        .commit();
            } else if (fragmentName.equals(getString(R.string.nav_drawer_expenses_string))) {
                ExpenseFragment expenseFragment = ExpenseFragment.newInstance(fragmentName, "hello");
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.detail_activity_frame_layout, expenseFragment)
                        .commit();
            } else if (fragmentName.equals(getString(R.string.nav_drawer_savings_string))) {
                SavingsFragment savingsFragment = SavingsFragment.newInstance(fragmentName, "hello");
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.detail_activity_frame_layout, savingsFragment)
                        .commit();
            }

        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}