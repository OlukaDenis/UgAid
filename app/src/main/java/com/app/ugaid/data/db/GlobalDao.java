package com.app.ugaid.data.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.app.ugaid.model.Covid;

@Dao
public interface GlobalDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCovids(Covid covid);

    @Query("SELECT * FROM covid_global ORDER BY :orderBy ASC LIMIT 1")
    Covid globalStats(String orderBy);

    @Query("DELETE FROM covid_global")
    void deleteAllCovids();

}
