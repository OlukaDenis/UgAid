package com.app.ugaid.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;


@Entity(tableName = "covid_global")
public class Covid implements Parcelable {
    @ColumnInfo(name = "cases")
    @SerializedName("cases")
    private long cases;

    @ColumnInfo(name = "deaths")
    @SerializedName("deaths")
    private long deaths;

    @ColumnInfo(name = "recovered")
    @SerializedName("recovered")
    private long recovered;

    @PrimaryKey
    @ColumnInfo(name = "updated")
    @SerializedName("updated")
    private long updated;

    @Ignore
    public Covid() {
    }

    public Covid(long cases, long deaths, long recovered, long updated) {
        this.cases = cases;
        this.deaths = deaths;
        this.recovered = recovered;
        this.updated = updated;
    }

    protected Covid(Parcel in) {
        cases = in.readLong();
        deaths = in.readLong();
        recovered = in.readLong();
        updated = in.readLong();
    }

    public static final Creator<Covid> CREATOR = new Creator<Covid>() {
        @Override
        public Covid createFromParcel(Parcel in) {
            return new Covid(in);
        }

        @Override
        public Covid[] newArray(int size) {
            return new Covid[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(cases);
        dest.writeLong(deaths);
        dest.writeLong(recovered);
        dest.writeLong(updated);
    }

    public long getCases() {
        return cases;
    }

    public long getDeaths() {
        return deaths;
    }

    public long getRecovered() {
        return recovered;
    }

    public long getUpdated() {
        return updated;
    }
}
