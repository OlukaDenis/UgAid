package com.app.ugaid.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.app.ugaid.model.CoronaCountry;
import com.app.ugaid.model.Hospital;

import java.util.List;

@Dao
public interface HospitalDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertHospitals(List<Hospital> hospitals);

    @Query("SELECT * FROM hospital")
    LiveData<List<Hospital>> getAllHospitals();

    @Query("DELETE FROM hospital")
    void deleteHospitals();
}
