package com.example.android.moneyanalytics.room;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.android.moneyanalytics.model.Entry;

import java.util.Date;
import java.util.List;

/**
 * This interface is used to create the CRUD operations on the Database.
 */
@Dao
public interface EntriesDao {
    @Query("SELECT * FROM entries ORDER BY id")
    LiveData<List<Entry>> loadAllEntries();

    @Query("SELECT * FROM entries WHERE category LIKE :entryCategory")
    LiveData<List<Entry>> loadCategory(String entryCategory);

    @Query("SELECT * FROM entries WHERE date LIKE :entryDate")
    LiveData<List<Entry>> loadDate(Date entryDate);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTask(Entry recipe);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateTask(Entry recipe);

    @Delete
    void deleteTask(Entry recipe);
}
