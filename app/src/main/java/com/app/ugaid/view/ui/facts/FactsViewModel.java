package com.app.ugaid.view.ui.facts;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FactsViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public FactsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is facts fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
