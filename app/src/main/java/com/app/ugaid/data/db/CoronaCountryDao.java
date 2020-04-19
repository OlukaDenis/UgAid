package com.app.ugaid.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.app.ugaid.model.CoronaCountry;

import java.util.List;

@Dao
public interface CoronaCountryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCoronaByCountry(List<CoronaCountry> coronaCountries);

    //to fetch data required to display in each page
    @Query("SELECT * FROM corona_country WHERE id >= 1 AND country = :name LIMIT 1")
    CoronaCountry getOneCountry(String name);

    @Query("SELECT * FROM corona_country ORDER BY cases DESC")
    LiveData<List<CoronaCountry>> getAllCountries();

    @Query("DELETE FROM corona_country")
    void deleteAllCountries();

    @Query("SELECT * FROM corona_country WHERE country LIKE :item")
    LiveData<List<CoronaCountry>> getSearchResults(String item);
}
