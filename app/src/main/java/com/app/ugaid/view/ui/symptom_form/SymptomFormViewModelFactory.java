package com.app.ugaid.view.ui.symptom_form;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.app.ugaid.data.repository.CovidRepository;
import com.app.ugaid.view.ui.countries.CountriesViewModel;

public class SymptomFormViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private Application mApplication;

    public SymptomFormViewModelFactory(Application application) {
        mApplication = application;
    }
    @Override
    public <T extends ViewModel> T create(Class<T> viewModel) {
        return (T) new SymptomFormViewModel(mApplication);
    }


}
