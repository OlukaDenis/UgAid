package com.app.ugaid.model;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class DataConverter {
    @TypeConverter
    public String fromCountryInfoList(CountryInfo countryInfos){
        if (countryInfos == null){
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<CountryInfo>(){}.getType();
        return gson.toJson(countryInfos, type);
    }

    @TypeConverter
    public CountryInfo toCountryInfoList(String countryInfoString){
        if (countryInfoString == null){
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<CountryInfo>(){}.getType();
        return gson.fromJson(countryInfoString, type);
    }

}
