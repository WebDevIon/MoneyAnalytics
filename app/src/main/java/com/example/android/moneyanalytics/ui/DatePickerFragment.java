package com.example.android.moneyanalytics.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    public static final String DATE_KEY = "date";

    public interface OnCompleteListener {
        void onDatePicked(String date);
    }

    private OnCompleteListener mListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        try {
            this.mListener = (OnCompleteListener)getContext();
        }
        catch (final ClassCastException e) {
            throw new ClassCastException(getContext() + " must implement OnCompleteListener");
        }

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        int correctMonth = month + 1;
        String date = "" + day + "/" + correctMonth + "/" + year;
        this.mListener.onDatePicked(date);
    }
}
