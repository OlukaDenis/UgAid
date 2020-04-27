package com.app.ugaid.view.ui.test_request;

import android.app.Application;

import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.app.ugaid.data.repository.CovidRepository;
import com.app.ugaid.model.Form;

public class TestRequestViewModel extends ViewModel {

    private CovidRepository repository;

    public TestRequestViewModel(Application application) {
        repository = new CovidRepository(application);
    }

    public void sendFormData(Form form){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference formReference = database.getReference().child("symptom-form");

        formReference.push().setValue(form);
    }
}
