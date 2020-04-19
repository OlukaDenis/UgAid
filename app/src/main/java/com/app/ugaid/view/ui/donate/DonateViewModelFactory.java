package com.app.ugaid.view.ui.donate;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class DonateViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private Application mApplication;

    public DonateViewModelFactory(Application application) {
            mApplication = application;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> viewModel) {
        return (T) new DonateViewModel(mApplication);
    }
}
