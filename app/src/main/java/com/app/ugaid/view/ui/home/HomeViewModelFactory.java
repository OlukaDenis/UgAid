package com.app.ugaid.view.ui.home;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;


public class HomeViewModelFactory extends ViewModelProvider.NewInstanceFactory  {

    private Application mApplication;

    public HomeViewModelFactory(Application application) {
        mApplication = application;
    }
    @Override
    public <T extends ViewModel> T create(Class<T> viewModel) {
        return (T) new HomeViewModel(mApplication);
    }

}
