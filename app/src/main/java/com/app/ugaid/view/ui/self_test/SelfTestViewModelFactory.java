package com.app.ugaid.view.ui.self_test;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class SelfTestViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private Application mApplication;

    public SelfTestViewModelFactory(Application application) {
        mApplication = application;
    }
    @Override
    public <T extends ViewModel> T create(Class<T> viewModel) {
        return (T) new SelfTestViewModel(mApplication);
    }
}
