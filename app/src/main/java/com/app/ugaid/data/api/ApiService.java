package com.app.ugaid.data.api;

import com.app.ugaid.model.CoronaCountry;
import com.app.ugaid.model.Covid;
import com.app.ugaid.model.State;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {

    @GET("all")
    Call<Covid> getWorldStats();

    @GET("countries")
    Call<List<CoronaCountry>> getAllCountries();

    @GET("countries/{country}")
    Call<CoronaCountry> getOneCountry(@Path("country") String country);

    @GET("states")
    Call<List<State>> getAllStates();
}
