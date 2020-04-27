package com.app.ugaid.view.ui.test_request;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class TestRequestViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private Application mApplication;

    public TestRequestViewModelFactory(Application application) {
        mApplication = application;
    }
    @Override
    public <T extends ViewModel> T create(Class<T> viewModel) {
        return (T) new TestRequestViewModel(mApplication);
    }


}
