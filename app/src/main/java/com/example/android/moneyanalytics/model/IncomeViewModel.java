package com.example.android.moneyanalytics.model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.util.Log;

import com.example.android.moneyanalytics.room.EntriesDatabase;

import java.util.List;

public class IncomeViewModel extends AndroidViewModel {

    private static final String TAG = MainViewModel.class.getSimpleName();
    private EntriesDatabase database;

    public IncomeViewModel(Application application) {
        super(application);
        database = EntriesDatabase.getInstance(this.getApplication());
        Log.d(TAG, "Actively retrieving the tasks from the DataBase");
    }

    public LiveData<List<Entry>> getSpecificDateEntries(Long entryDate) {
        return database.entriesDao().loadSpecificDate(entryDate);
    }

    public LiveData<List<Entry>> getPeriodEntries(Long startDate, Long endDate) {
        return database.entriesDao().loadPeriodDate(startDate, endDate);
    }

    public LiveData<List<Entry>> getSpecificDateCategoryEntries(String entryCategory, Long entryDate) {
        return database.entriesDao().loadSpecificDateCategory(entryCategory, entryDate);
    }

    public LiveData<List<Entry>> getPeriodDateCategoryEntries
            (String entryCategory, Long startDate, Long endDate) {
        return database.entriesDao().loadPeriodDateCategory(entryCategory, startDate, endDate);
    }
}
