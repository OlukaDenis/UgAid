package com.app.ugaid.view.ui.countries;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.app.ugaid.data.repository.CovidRepository;
import com.app.ugaid.model.CoronaCountry;

import java.util.List;

public class CountriesViewModel extends ViewModel {

    private CovidRepository repository;

    public CountriesViewModel(Application application) {
       repository = new CovidRepository(application);
    }

    public LiveData<List<CoronaCountry>> getStatsByCountry() {
        return repository.getAllCountries();
    }

    public LiveData<List<CoronaCountry>> searchCountries(String country){
        return repository.getSearchResults(country);
    }


}