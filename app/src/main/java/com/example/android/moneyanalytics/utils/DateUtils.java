package com.example.android.moneyanalytics.utils;

import java.util.Calendar;

/**
 * This class is responsible for calculating the time in milliseconds
 * at specific intervals from a specified date.
 */
public class DateUtils {

    // The date from which the other dates are calculated.
    private Long currentDate;

    // Constructor.
    public DateUtils(Long currentDate) {
        this.currentDate = currentDate;
    }

    // Time that has passed since midnight in milliseconds.
    public Long getTimeFromMidnight() {
        return currentDate % (24 * 60 * 60 * 1000);
    }

    // Time at midnight in milliseconds.
    public Long getMidnightDate() {
        return currentDate - currentDate % (24 * 60 * 60 * 1000);
    }

    // Time a week ago in milliseconds.
    public Long getAWeekAgoDate() {
        return currentDate - 604800000 - getTimeFromMidnight();
    }

    // Time at midnight at the start of the current month in milliseconds.
    public Long getFirstDayOfMonthDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return cal.getTimeInMillis() - currentDate % (24 * 60 * 60 * 1000);
    }

    // Time a month ago in milliseconds.
    public Long getTimeAMonthAgo() {
        Calendar cal = Calendar.getInstance();
        int previousMonth = cal.get(Calendar.MONTH) - 1;
        if (previousMonth == -1) previousMonth = 11;
        cal.set(Calendar.MONTH, previousMonth);
        return cal.getTimeInMillis() - getTimeFromMidnight();
    }

    // Time at a specific date at midnight in milliseconds
    public Long getASpecificDayDate() {
        return currentDate - getTimeFromMidnight();
    }

    // Time a month from a specified date in milliseconds.
    public Long updateRecurringDate() {
        Calendar initialCal = Calendar.getInstance();
        initialCal.setTimeInMillis(currentDate);
        int initialDay = initialCal.get(Calendar.DAY_OF_MONTH);
        Calendar updatedCal = Calendar.getInstance();
        updatedCal.set(Calendar.DAY_OF_MONTH, initialDay);
        return updatedCal.getTimeInMillis();
    }

    // String with the selected date from milliseconds to "dd/mm/yyyy".
    public String getDateToString() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(currentDate);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return  "" + day + "/" + month + "/" + year;
    }
}
