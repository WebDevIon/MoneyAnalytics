package com.example.android.moneyanalytics.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.moneyanalytics.R;

public class AddGoalActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "AddGoalActivity prefs";
    public static final String SAVING_GOAL_KEY = "goal amount";
    Button mCancel, mOk;
    private EditText mAmount;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goal);

        mCancel = findViewById(R.id.add_savings_activity_cancel_button);
        mOk = findViewById(R.id.add_savings_activity_ok_button);
        mAmount = findViewById(R.id.add_savings_activity_amount_et);

        // Here we set the title and the name of the button accordingly
        Intent intent = getIntent();
        if (intent != null) {
            String goal = intent.getStringExtra(SAVING_GOAL_KEY);
            if (!goal.equals(SavingsFragment.NO_GOAL_SET)) {
                mAmount.setText(goal);
                setTitle(R.string.edit_savings_activity_title);
            } else {
                setTitle(R.string.add_savings_activity_title);
            }
        } else {
            setTitle(R.string.add_savings_activity_title);
        }

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
                } else {
                    Double amount = Double.parseDouble(mAmount.getText().toString());

                    SharedPreferences.Editor prefs =
                            getApplicationContext().getSharedPreferences(PREFS_NAME, 0).edit();
                    prefs.putString(SAVING_GOAL_KEY, amount.toString());
                    prefs.apply();

                    Toast.makeText(AddGoalActivity.this,
                            "Goal added!", Toast.LENGTH_SHORT).show();

                    finish();
                }
            }
        });

    }
}
