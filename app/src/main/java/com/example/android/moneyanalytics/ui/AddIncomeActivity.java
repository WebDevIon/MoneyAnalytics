package com.example.android.moneyanalytics.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.moneyanalytics.R;

public class AddIncomeActivity extends AppCompatActivity implements DatePickerFragment.OnCompleteListener {

    private static final String DATE_RESET_KEY = "dd-mm-yyyy";
    private Button mCancel, mOk;
    private TextView mDateField;
    private RadioButton mToday, mOtherDate;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_income);

        setTitle(R.string.add_income_activity_title);

        mCancel = findViewById(R.id.add_income_activity_cancel_button);
        mOk = findViewById(R.id.add_income_activity_ok_button);

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Add functionality.
                Toast.makeText(getApplicationContext(), "You made it!", Toast.LENGTH_SHORT).show();
            }
        });

        mDateField = findViewById(R.id.add_income_activity_date_tv);
        mOtherDate = findViewById(R.id.add_income_activity_other_date_rb);
        mToday = findViewById(R.id.add_income_activity_today_rb);

        mToday.toggle();
        mToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDateField.setVisibility(View.GONE);
                mDateField.setText(DATE_RESET_KEY);
            }
        });

        mOtherDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDateField.setVisibility(View.VISIBLE);
                DialogFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

    }

    @Override
    public void onDatePicked(String date) {
        mDateField.setText(date);
    }
}
