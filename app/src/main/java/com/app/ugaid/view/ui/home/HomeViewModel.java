package com.app.ugaid.view.ui.home;

import android.app.Application;

import androidx.lifecycle.ViewModel;

import com.app.ugaid.data.repository.CovidRepository;
import com.app.ugaid.model.Covid;

public class HomeViewModel extends ViewModel {

    private CovidRepository repository;

    public HomeViewModel(Application application){
        repository = new CovidRepository(application);
    }

    public Covid getGlobalStats(String orderby){
        return repository.getGlobalStat(orderby);
    }


}