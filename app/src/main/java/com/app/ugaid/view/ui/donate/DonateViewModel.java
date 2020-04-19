package com.app.ugaid.view.ui.donate;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.app.ugaid.data.repository.CovidRepository;
import com.app.ugaid.model.Donation;

public class DonateViewModel extends ViewModel {
    private CovidRepository repository;

    public DonateViewModel(Application application) {
        repository = new CovidRepository(application);
    }

    public void sendDonation(Donation donation){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference formReference = database.getReference().child("donations");

        formReference.push().setValue(donation);
    }
}