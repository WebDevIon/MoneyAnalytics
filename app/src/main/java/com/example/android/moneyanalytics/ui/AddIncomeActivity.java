package com.example.android.moneyanalytics.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.moneyanalytics.R;
import com.example.android.moneyanalytics.model.Entry;
import com.example.android.moneyanalytics.room.AppExecutors;
import com.example.android.moneyanalytics.room.EntriesDatabase;

import java.util.Calendar;
import java.util.Date;

public class AddIncomeActivity extends AppCompatActivity implements DatePickerFragment.OnCompleteListener {

    private static final String DATE_RESET_KEY = "dd-mm-yyyy";
    public static final String DATA_INCOME_TYPE_KEY = "income";
    Button mCancel, mOk;
    private TextView mDateField;
    RadioButton mToday, mOtherDate;
    private EditText mAmount, mDescription;
    private Spinner mSpinner;
    private CheckBox mRecurring;
    private Entry mEntry = null;
    private Long mTimeInMillis;
    private EntriesDatabase mDb;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_income);

        setTitle(R.string.add_income_activity_title);

        mDb = EntriesDatabase.getInstance(getApplicationContext());

        mCancel = findViewById(R.id.add_income_activity_cancel_button);
        mOk = findViewById(R.id.add_income_activity_ok_button);
        mDateField = findViewById(R.id.add_income_activity_date_tv);
        mOtherDate = findViewById(R.id.add_income_activity_other_date_rb);
        mToday = findViewById(R.id.add_income_activity_today_rb);
        mAmount = findViewById(R.id.add_income_activity_amount_et);
        mDescription = findViewById(R.id.add_income_activity_description_et);
        mSpinner = findViewById(R.id.add_income_activity_category_spinner);
        mRecurring = findViewById(R.id.add_income_activity_monthly_cb);

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAmount.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(),
                            R.string.add_income_activity_value_warning_text, Toast.LENGTH_SHORT).show();
                } else if(mDescription.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(),
                            R.string.add_income_activity_description_warning_text,
                            Toast.LENGTH_SHORT).show();
                } else {
                    Double amount = Double.parseDouble(mAmount.getText().toString());
                    String name = mDescription.getText().toString();
                    String category = mSpinner.getSelectedItem().toString();
                    boolean recurring = mRecurring.isChecked();
                    Long date = null;

                    if (mToday.isChecked()) {
                        date = new Date().getTime();
                    } else if (mOtherDate.isChecked()) {
                        Calendar c = Calendar.getInstance();
                        c.setTimeInMillis(mTimeInMillis);
                        date = c.getTimeInMillis();

                        if (date > new Date().getTime()) {
                            date = new Date().getTime();

                            Calendar calendar = Calendar.getInstance();
                            int year = calendar.get(Calendar.YEAR);
                            int month = calendar.get(Calendar.MONTH);
                            int day = calendar.get(Calendar.DAY_OF_MONTH);

                            int correctMonth = month + 1;
                            String dateField = "" + day + "/" + correctMonth + "/" + year;
                            mDateField.setText(dateField);
                        }
                    }

                    mEntry = new Entry(amount, name, category, recurring, date, DATA_INCOME_TYPE_KEY);

                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            mDb.entriesDao().insertTask(mEntry);
                        }
                    });

                    Toast.makeText(AddIncomeActivity.this,
                            "Income added!", Toast.LENGTH_SHORT).show();

                    finish();
                }
            }
        });

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
    public void onDatePicked(String date, Long timeInMillis) {
        mDateField.setText(date);
        mTimeInMillis = timeInMillis;
    }
}
