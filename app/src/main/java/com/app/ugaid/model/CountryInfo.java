package com.app.ugaid.model;

import com.google.gson.annotations.SerializedName;

public class CountryInfo {
    @SerializedName("_id")
    private long _id;

    @SerializedName("lat")
    private double latitude;

    @SerializedName("long")
    private double longitude;

    @SerializedName("flag")
    private String flag;

    @SerializedName("iso3")
    private String iso3;

    @SerializedName("iso2")
    private String iso2;

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(long latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(long longitude) {
        this.longitude = longitude;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getIso3() {
        return iso3;
    }

    public void setIso3(String iso3) {
        this.iso3 = iso3;
    }

    public String getIso2() {
        return iso2;
    }

    public void setIso2(String iso2) {
        this.iso2 = iso2;
    }
}
