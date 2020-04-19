package com.app.ugaid.view.ui.symptom_form;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.app.ugaid.R;
import com.app.ugaid.data.local.LocalDataSource;
import com.app.ugaid.model.Form;
import com.app.ugaid.model.Hospital;
import com.app.ugaid.model.Symptom;
import com.app.ugaid.model.User;
import com.app.ugaid.utils.Config;
import com.app.ugaid.view.ui.HomeActivity;
import com.app.ugaid.view.ui.hospitals.HospitalActivity;
import com.app.ugaid.view.ui.hospitals.HospitalViewModel;
import com.app.ugaid.view.ui.hospitals.HospitalViewModelFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SymptomFormActivity extends AppCompatActivity {
    private  String[] GENDER = new String[] {"Male", "Female", "Prefer not to say"};
    private static final String TAG = "SymptomFormActivity";

    private TextInputEditText userName, userPhone;
    private TextInputLayout hospitalLayout;
    private AutoCompleteTextView userGender, formHospital, userState;

    private AlertDialog.Builder builder;

    private RadioGroup rgBreathing, rgTemperature, rgCough, rgChestPain;
    private RadioButton answerOne, answerTwo, answerThree, answerFour;
    private Button btnSubmitForm;

    private String breathAnswer, tempAnswer, coughAnswer, chestAnswer;
    private String name, state, hospital, phone;

    private  SymptomFormViewModel viewModel;
    private static final int HOSPITAL_REQUEST_CODE = 100;

    private FirebaseAnalytics mFirebaseAnalytics;
    private FusedLocationProviderClient mFusedLocationClient;
    private double mLat, mLong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptom_form);

        builder = new AlertDialog.Builder(this);
        //Init firebase analytics
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        SymptomFormViewModelFactory factory = new SymptomFormViewModelFactory(this.getApplication());
        viewModel = new ViewModelProvider(this, factory).get(SymptomFormViewModel.class);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();


        ArrayAdapter adapter = new ArrayAdapter<>(this, R.layout.dropdown_pop_up_item, GENDER);

        userGender = findViewById(R.id.gender_exposed_dropdown);
        userGender.setKeyListener(null);
        userGender.setAdapter(adapter);

        userName = findViewById(R.id.user_name);
        userPhone = findViewById(R.id.phone_number);
        hospitalLayout = findViewById(R.id.hospital_name_layout);


        rgBreathing = findViewById(R.id.rg_breathing);
        rgChestPain = findViewById(R.id.rg_chest);
        rgCough = findViewById(R.id.rg_cough);
        rgTemperature = findViewById(R.id.rg_temperature);

        btnSubmitForm = findViewById(R.id.btn_submit_form);

        //Autocomplete textview for states
        userState = findViewById(R.id.state_name);
        userState.setKeyListener(null);
        ArrayAdapter sAdapter = new ArrayAdapter<>(this, R.layout.dropdown_pop_up_item, LocalDataSource.districts());
        userState.setAdapter(sAdapter);

        //Autocomplete textview for hospitals
        formHospital = findViewById(R.id.hospital_name);
        formHospital.setKeyListener(null);
        HospitalViewModelFactory mfactory = new HospitalViewModelFactory(this.getApplication());
        HospitalViewModel viewModel = new ViewModelProvider(this, mfactory).get(HospitalViewModel.class);
        viewModel.getAllHospitals().observe(this, hospitals -> {

            ArrayAdapter mAdapter = new ArrayAdapter<>(this, R.layout.dropdown_pop_up_item, LocalDataSource.testingCenters());
            formHospital.setAdapter(mAdapter);
        });

        formHospital.setOnClickListener(v -> {
//            Intent getHospitalIntent = new Intent(this, HospitalActivity.class);
//            startActivityForResult(getHospitalIntent, HOSPITAL_REQUEST_CODE);
        });

        rgBreathing.setOnCheckedChangeListener((group, checkedId) -> {
            answerOne = findViewById(checkedId);
            breathAnswer = answerOne.getText().toString();
        });

        rgChestPain.setOnCheckedChangeListener((group, checkedId) -> {
            answerTwo = findViewById(checkedId);
            chestAnswer = answerTwo.getText().toString();
        });

        rgTemperature.setOnCheckedChangeListener((group, checkedId) -> {
            answerThree = findViewById(checkedId);
            tempAnswer = answerThree.getText().toString();
        });

        rgCough.setOnCheckedChangeListener((group, checkedId) -> {
            answerFour = findViewById(checkedId);
            coughAnswer = answerFour.getText().toString();
        });


        btnSubmitForm.setOnClickListener(v ->{
            sumbitForm();
        });
    }

    private void sumbitForm() {

        hospital = Objects.requireNonNull(formHospital.getText()).toString();

        //User
        name = Objects.requireNonNull(userName.getText()).toString();
        state = Objects.requireNonNull(userState.getText()).toString();
        phone = Objects.requireNonNull(userPhone.getText()).toString();
        String gender = userGender.getText().toString();
        User user = new User(name, state, gender, phone);

        //Symptoms
        Symptom symptom = new Symptom(breathAnswer, chestAnswer, tempAnswer, coughAnswer);

        if (rgBreathing.getCheckedRadioButtonId() == -1 || rgCough.getCheckedRadioButtonId() == -1 ||
            rgTemperature.getCheckedRadioButtonId() == -1 || rgChestPain.getCheckedRadioButtonId() == -1){
            Toast.makeText(this, "Please choose YES, NO or SOMETIMES", Toast.LENGTH_SHORT).show();
        } else if(name.isEmpty()){
            userName.setError("Please provide your name");
        }
        else if(state.isEmpty()){
            userState.setError("Please provide your State");
        }else if(phone.isEmpty()){
            userPhone.setError("Please provide your phone number");
        }else if(gender.isEmpty()){
            Toast.makeText(this, "Please you must provide your gender", Toast.LENGTH_SHORT).show();
        }else {
            //Form submission
            Form form = new Form(user, symptom, hospital, mLat, mLong);
            viewModel.sendFormData(form);

            builder.setTitle("Thank you for submitting your symptoms")
                    .setMessage("Our health staff will contact you soon")
                    .setPositiveButton("Okay", (dialog, which) -> {
                        startActivity(new Intent(this, HomeActivity.class));
                        finish();
                    });
            builder.setCancelable(false);
            builder.show();
        }

    }

    @SuppressLint("MissingPermission")
    private void getLastLocation(){
        if (Config.checkLocationPermissions(this)) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        task -> {
                            Location location = task.getResult();
                            if (location == null) {
                                requestNewLocationData();
                            } else {
                                mLat = location.getLatitude();
                                mLong = location.getLongitude();
                            }
                        }
                );
            } else {
                Toast.makeText(this, "Turn on the device location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            Config.requestLocationPermissions(this);
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData(){

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            mLat = mLastLocation.getLatitude();
            mLong = mLastLocation.getLongitude();
        }
    };

    private boolean isLocationEnabled(){
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    @Override
    public void onResume(){
        super.onResume();
        if (Config.checkLocationPermissions(this)) {
            getLastLocation();
        }
    }
}
