package com.app.ugaid.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CoronaCountryResponse {
    @SerializedName("")
    private List<CoronaCountry> countryList;

    public List<CoronaCountry> getCountryList() {
        return countryList;
    }

    public void setCountryList(List<CoronaCountry> countryList) {
        this.countryList = countryList;
    }
}
