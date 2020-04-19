package com.app.ugaid.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "corona_country")
public class CoronaCountry implements Parcelable{
    @PrimaryKey(autoGenerate = true)
    private int id = 0;

    @ColumnInfo(name = "country")
    @SerializedName("country")
    private String country;

    @TypeConverters(DataConverter.class)
    @ColumnInfo(name = "countryInfo")
    @SerializedName("countryInfo")
    private CountryInfo countryInfo;

    @ColumnInfo(name = "cases")
    @SerializedName("cases")
    private long cases;

    @ColumnInfo(name = "todayCases")
    @SerializedName("todayCases")
    private long todayCases;

    @ColumnInfo(name = "deaths")
    @SerializedName("deaths")
    private long deaths;

    @ColumnInfo(name = "todayDeaths")
    @SerializedName("todayDeaths")
    private long todayDeaths;

    @ColumnInfo(name = "recovered")
    @SerializedName("recovered")
    private long recovered;

    @ColumnInfo(name = "critical")
    @SerializedName("critical")
    private long critical;

    @ColumnInfo(name = "casesPerOneMillion")
    @SerializedName("casesPerOneMillion")
    private double casesPerOneMillion;

    @Ignore
    public CoronaCountry() {
    }


    public CoronaCountry(int id, String country, long cases, long todayCases, long deaths, long todayDeaths, long recovered, long critical, double casesPerOneMillion) {
        this.id = id;
        this.country = country;
        this.cases = cases;
        this.todayCases = todayCases;
        this.deaths = deaths;
        this.todayDeaths = todayDeaths;
        this.recovered = recovered;
        this.critical = critical;
        this.casesPerOneMillion = casesPerOneMillion;
    }

    @Ignore
    public CoronaCountry(int id, String country, CountryInfo countryInfo, long cases, long todayCases, long deaths, long todayDeaths, long recovered, long critical, double casesPerOneMillion) {
        this.id = id;
        this.country = country;
        this.countryInfo = countryInfo;
        this.cases = cases;
        this.todayCases = todayCases;
        this.deaths = deaths;
        this.todayDeaths = todayDeaths;
        this.recovered = recovered;
        this.critical = critical;
        this.casesPerOneMillion = casesPerOneMillion;
    }

    protected CoronaCountry(Parcel in) {
        id = in.readInt();
        country = in.readString();
        cases = in.readLong();
        todayCases = in.readLong();
        deaths = in.readLong();
        todayDeaths = in.readLong();
        recovered = in.readLong();
        critical = in.readLong();
        casesPerOneMillion = in.readDouble();
    }

    public static final Creator<CoronaCountry> CREATOR = new Creator<CoronaCountry>() {
        @Override
        public CoronaCountry createFromParcel(Parcel in) {
            return new CoronaCountry(in);
        }

        @Override
        public CoronaCountry[] newArray(int size) {
            return new CoronaCountry[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(country);
        dest.writeLong(cases);
        dest.writeLong(todayCases);
        dest.writeLong(deaths);
        dest.writeLong(todayDeaths);
        dest.writeLong(recovered);
        dest.writeLong(critical);
        dest.writeDouble(casesPerOneMillion);
    }

    public CountryInfo getCountryInfo() {
        return countryInfo;
    }

    public void setCountryInfo(CountryInfo countryInfo) {
        this.countryInfo = countryInfo;
    }

    public int getId() {
        return id;
    }

    public String getCountry() {
        return country;
    }

    public long getCases() {
        return cases;
    }

    public long getTodayCases() {
        return todayCases;
    }

    public long getDeaths() {
        return deaths;
    }

    public long getTodayDeaths() {
        return todayDeaths;
    }

    public long getRecovered() {
        return recovered;
    }

    public long getCritical() {
        return critical;
    }

    public double getCasesPerOneMillion() {
        return casesPerOneMillion;
    }
}
