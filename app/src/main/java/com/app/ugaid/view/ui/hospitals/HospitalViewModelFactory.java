package com.app.ugaid.view.ui.hospitals;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.app.ugaid.view.ui.countries.CountriesViewModel;

public class HospitalViewModelFactory extends ViewModelProvider.NewInstanceFactory  {
    private Application mApplication;

    public HospitalViewModelFactory(Application application) {
        mApplication = application;
    }
    @Override
    public <T extends ViewModel> T create(Class<T> viewModel) {
        return (T) new HospitalViewModel(mApplication);
    }
}
