package com.app.ugaid.view.ui.countries;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class CountriesViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private Application mApplication;

    public CountriesViewModelFactory(Application application) {
        mApplication = application;
    }
    @Override
    public <T extends ViewModel> T create(Class<T> viewModel) {
        return (T) new CountriesViewModel(mApplication);
    }
}