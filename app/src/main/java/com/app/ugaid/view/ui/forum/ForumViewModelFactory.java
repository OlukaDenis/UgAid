package com.app.ugaid.view.ui.forum;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ForumViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private Application mApplication;

    public ForumViewModelFactory(Application application) {
        mApplication = application;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> viewModel) {
        return (T) new ForumViewModel(mApplication);
    }
}
