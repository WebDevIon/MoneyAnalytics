package com.example.android.moneyanalytics.room;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.android.moneyanalytics.model.Entry;
import com.example.android.moneyanalytics.model.EntryByCategory;

import java.util.List;

/**
 * This interface is used to create the CRUD operations on the Database.
 */
@Dao
public interface EntriesDao {
    // Query used to retrieve all Entries from the table.
    @Query("SELECT * FROM entries ORDER BY id")
    LiveData<List<Entry>> loadAllEntries();

    // Query used to populate Animated Pie Charts.
    @Query("SELECT SUM(amount) AS amount, category, type FROM entries WHERE date BETWEEN " +
            ":startDate AND :endDate GROUP BY category")
    LiveData<List<EntryByCategory>> loadGroupedByCategory(Long startDate, Long endDate);

    // Query used to populate Recycler views with information about a category.
    @Query("SELECT * FROM entries WHERE category LIKE :entryCategory AND date " +
            "BETWEEN :startDate AND :endDate")
    LiveData<List<Entry>> loadByDateAndCategory(String entryCategory, Long startDate, Long endDate);

    // Query used to retrieve the Entries between two specified dates.
    @Query("SELECT * FROM entries WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    LiveData<List<Entry>> loadByDate(Long startDate, Long endDate);

    // Query used to retrieve the Entries between two specified dates for the widget.
    @Query("SELECT * FROM entries WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    List<Entry> loadByDateForWidget(Long startDate, Long endDate);

    // Query used to retrieve only recurring entries.
    @Query("SELECT * FROM entries WHERE recurring LIKE :isRecurring")
    LiveData<List<Entry>> loadIfRecurring(boolean isRecurring);

    @Insert
    void insertTask(Entry entry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateTask(Entry entry);

    @Delete
    void deleteTask(Entry entry);
}
