package com.example.android.moneyanalytics.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

import com.example.android.moneyanalytics.model.Entry;

/**
 * This class is responsible for creating the Room Database.
 */
@Database(entities = {Entry.class}, version = 1, exportSchema = false)
public abstract class EntriesDatabase extends RoomDatabase {

    private static final String LOG_TAG = EntriesDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "entries_database";
    private static EntriesDatabase sInstance;

    // Here we make sure we only have one instance of the Database.
    public static EntriesDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        EntriesDatabase.class, EntriesDatabase.DATABASE_NAME)
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }

    public abstract EntriesDao entriesDao();
}
