package com.app.ugaid.view.ui.self_test;

import android.app.Application;

import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.app.ugaid.data.repository.CovidRepository;
import com.app.ugaid.model.Test;

public class SelfTestViewModel extends ViewModel {

    private CovidRepository repository;

    public SelfTestViewModel(Application application) {
       repository = new CovidRepository(application);
    }

    public void saveTests(Test test){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference formReference = database.getReference().child("self-test");

        formReference.push().setValue(test);
    }
}