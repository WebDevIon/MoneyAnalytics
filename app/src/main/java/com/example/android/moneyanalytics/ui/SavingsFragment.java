package com.example.android.moneyanalytics.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.moneyanalytics.R;
import com.example.android.moneyanalytics.chart.DefaultPieConfig;
import com.example.android.moneyanalytics.chart.PieChartData;
import com.example.android.moneyanalytics.model.Entry;
import com.example.android.moneyanalytics.model.SavingsViewModel;
import com.example.android.moneyanalytics.utils.DateUtils;
import com.razerdp.widget.animatedpieview.AnimatedPieView;
import com.razerdp.widget.animatedpieview.AnimatedPieViewConfig;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

/**
 * The fragment responsible for displaying the savings status.
 */
public class SavingsFragment extends Fragment {

    public static final String TAG = SavingsFragment.class.getSimpleName();
    public static final String NO_GOAL_SET = "no goal set";

    private String mResult;
    private TextView mTotalTextView;
    private TextView mDueDateTextView;
    private TextView mDalyAverageTextView;
    private TextView mSavedSoFar;
    private AnimatedPieView mAnimatedPieView;

    public SavingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_savings, container, false);

        Button goalButton = rootView.findViewById(R.id.savings_fragment_goal_button);
        mTotalTextView = rootView.findViewById(R.id.savings_fragment_goal_value_tv);
        mAnimatedPieView = rootView.findViewById(R.id.savings_fragment_pie_view);
        mDueDateTextView = rootView.findViewById(R.id.savings_fragment_due_value_tv);
        mDalyAverageTextView = rootView.findViewById(R.id.savings_fragment_average_value_tv);
        mSavedSoFar = rootView.findViewById(R.id.savings_fragment_saved_so_far_value_tv);

        SharedPreferences prefs = getContext().getSharedPreferences(AddGoalActivity.PREFS_NAME, 0);
        mResult = prefs.getString(AddGoalActivity.SAVING_GOAL_KEY, NO_GOAL_SET);

        if (!mResult.equals(NO_GOAL_SET)) {
            goalButton.setText(getResources().getText(R.string.savings_fragment_edit_goal_text));
            mTotalTextView.setText(mResult);
        } else {
            goalButton.setText(getResources().getText(R.string.savings_fragment_set_goal_text));
            mTotalTextView.setText(getResources()
                    .getText(R.string.savings_fragment_add_saving_goal_text));
        }

        goalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddGoalActivity.class);
                if (!mResult.equals(NO_GOAL_SET)){
                    intent.putExtra(AddGoalActivity.SAVING_GOAL_KEY, mResult);
                } else {
                    intent.putExtra(AddGoalActivity.SAVING_GOAL_KEY, NO_GOAL_SET);
                }
                startActivity(intent);
            }
        });

        Long currentDate = new Date().getTime();
        setupViewModel(new DateUtils(currentDate).getFirstDayOfMonthDate(), currentDate);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences prefs = getContext().getSharedPreferences(AddGoalActivity.PREFS_NAME, 0);
        mResult = prefs.getString(AddGoalActivity.SAVING_GOAL_KEY, NO_GOAL_SET);

        if (!mResult.equals(NO_GOAL_SET)) {
            mTotalTextView.setText(mResult);
        } else {
            mTotalTextView.setText(getResources()
                    .getText(R.string.savings_fragment_add_saving_goal_text));
        }
    }

    /**
     * Method used to set up the View Model.
     * @param startDate the start date used in the queries.
     * @param endDate the end date used in the queries.
     */
    private void setupViewModel(Long startDate, final Long endDate) {
        SavingsViewModel viewModel = ViewModelProviders.of(this).get(SavingsViewModel.class);
        viewModel.getEntriesThisMonth(startDate, endDate)
                .observe(this, new Observer<List<Entry>>() {
                    @Override
                    public void onChanged(@Nullable List<Entry> entries) {
                        Log.d(TAG, "Updating list of tasks from LiveData in MainViewModel");
                        if (entries != null) {
                            processEntries(entries);
                        } else {
                            Log.d(TAG, "Query returned no results.");
                        }
                    }
                });

    }

    /**
     * This method is used to populate the Pie Chart and calculate the balance.
     * @param entries the list of entries passed by the ViewModel.
     */
    private void processEntries(List<Entry> entries) {
        Log.d(TAG, "Entry in savings fragment length: " + entries.size());

        DecimalFormat averageNumberFormat = new DecimalFormat("#.00 / day");
        DecimalFormat numberFormat = new DecimalFormat("#.00");
        Double totalIncome = 0d;
        Double totalExpense = 0d;
        AnimatedPieViewConfig mPieConfig = new DefaultPieConfig().getDefaultPieConfig(false);

        for (Entry entry : entries) {
            if (entry.getType().equals(AddExpenseActivity.DATA_EXPENSE_TYPE_KEY)) {
                totalExpense += entry.getAmount();
            } else {
                totalIncome += entry.getAmount();
            }
        }

        if (totalIncome != 0) {
            mPieConfig.addData(new PieChartData(totalIncome,
                    getResources().getColor(R.color.savingsColor),
                    getResources().getString(R.string.savings_fragment_savings_label)));
        }

        if (totalExpense != 0) {
            mPieConfig.addData(new PieChartData(totalExpense,
                    getResources().getColor(R.color.expensesColor),
                    getResources().getString(R.string.savings_fragment_expenses_label)));
        }

        mAnimatedPieView.start(mPieConfig);

        int daysRemaining = new DateUtils(new Date().getTime()).getTimeUntilEndOfMonth();
        String daysRemainingStr;
        if (daysRemaining != 1) {
            daysRemainingStr = daysRemaining + " " + getResources()
                    .getString(R.string.savings_fragment_days_remaining_text);
        } else {
            daysRemainingStr = daysRemaining + " " + getResources()
                    .getString(R.string.savings_fragment_day_remaining_text);
        }
        mDueDateTextView.setText(daysRemainingStr);

        Double dailyAverage = (totalIncome - totalExpense) /
                (new DateUtils(new Date().getTime()).getNmberOfDaysForCurrentMonth());

        String dailyAverageStr = averageNumberFormat.format(dailyAverage);
        mDalyAverageTextView.setText(dailyAverageStr);

        mSavedSoFar.setText(numberFormat.format(totalIncome - totalExpense));

        Log.d(TAG, "Entry in savings totalExpense: " + totalExpense);
        Log.d(TAG, "Entry in savings totalIncome: " + totalIncome);
    }
}
