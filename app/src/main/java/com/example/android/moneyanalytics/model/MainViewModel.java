package com.example.android.moneyanalytics.model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.util.Log;

import com.example.android.moneyanalytics.room.EntriesDatabase;

import java.util.Date;
import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private static final String TAG = MainViewModel.class.getSimpleName();
    private EntriesDatabase database;

    public MainViewModel(Application application) {
        super(application);
        database = EntriesDatabase.getInstance(this.getApplication());
        Log.d(TAG, "Actively retrieving the tasks from the DataBase");
    }

    public LiveData<List<Entry>> getSpecificDateEntries(Date entryDate) {
        return database.entriesDao().loadSpecificDate(entryDate);
    }

    public LiveData<List<Entry>> getPeriodEntries(Date startDate, Date endDate) {
        return database.entriesDao().loadPeriodDate(startDate, endDate);
    }
}