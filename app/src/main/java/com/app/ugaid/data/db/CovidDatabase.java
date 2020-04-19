package com.app.ugaid.data.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.app.ugaid.model.CoronaCountry;
import com.app.ugaid.model.Covid;
import com.app.ugaid.model.DataConverter;
import com.app.ugaid.model.Hospital;

@Database(entities = {CoronaCountry.class, Covid.class, Hospital.class}, version = 4, exportSchema = false )
@TypeConverters(DataConverter.class)

public abstract class CovidDatabase extends RoomDatabase {
    private static CovidDatabase INSTANCE;
    public abstract CoronaCountryDao countryDao();
    public abstract GlobalDao globalDao();
    public abstract HospitalDao hospitalDao();

    public static CovidDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (CovidDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = buildDatabase(context);
                }
            }
        }
        return INSTANCE;
    }

    private static CovidDatabase buildDatabase(Context context) {
        return Room.databaseBuilder(context,
                CovidDatabase.class, "Covid.db")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();

    }
}
