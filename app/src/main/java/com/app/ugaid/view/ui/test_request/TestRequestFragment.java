package com.app.ugaid.view.ui.test_request;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.app.ugaid.model.Symptom;
import com.app.ugaid.model.User;
import com.app.ugaid.utils.Config;
import com.app.ugaid.view.ui.hospitals.HospitalViewModel;
import com.app.ugaid.view.ui.hospitals.HospitalViewModelFactory;

import java.util.Objects;

import static com.app.ugaid.utils.Config.PERMISSION_ID;

public class TestRequestFragment extends Fragment {

    private  String[] GENDER = new String[] {"Male", "Female", "Prefer not to say"};
    private static final String TAG = "TestRequestFragment";

    private TextInputEditText userName, userPhone;
    private TextInputLayout hospitalLayout;
    private AutoCompleteTextView userGender, formHospital, userState;

    private AlertDialog.Builder builder;

    private RadioGroup rgBreathing, rgTemperature, rgCough, rgChestPain;
    private RadioButton answerOne, answerTwo, answerThree, answerFour;
    private Button btnSubmitForm;

    private String breathAnswer, tempAnswer, coughAnswer, chestAnswer;
    private String name, state, hospital, phone;

    private TestRequestViewModel viewModel;
    private static final int HOSPITAL_REQUEST_CODE = 100;

    private FirebaseAnalytics mFirebaseAnalytics;
    private FusedLocationProviderClient mFusedLocationClient;
    private double mLat, mLong;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_test_request, container, false);

        builder = new AlertDialog.Builder(getContext());
        //Init firebase analytics
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
        mFirebaseAnalytics.setCurrentScreen(getActivity(), this.getClass().getSimpleName(),
                this.getClass().getSimpleName());

        TestRequestViewModelFactory factory = new TestRequestViewModelFactory(getActivity().getApplication());
        viewModel = new ViewModelProvider(this, factory).get(TestRequestViewModel.class);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        getLastLocation();


        ArrayAdapter adapter = new ArrayAdapter<>(getContext(), R.layout.dropdown_pop_up_item, GENDER);

        userGender = root.findViewById(R.id.gender_exposed_dropdown);
        userGender.setKeyListener(null);
        userGender.setAdapter(adapter);

        userName = root.findViewById(R.id.user_name);
        userPhone = root.findViewById(R.id.phone_number);
        hospitalLayout = root.findViewById(R.id.hospital_name_layout);


        rgBreathing = root.findViewById(R.id.rg_breathing);
        rgChestPain = root.findViewById(R.id.rg_chest);
        rgCough = root.findViewById(R.id.rg_cough);
        rgTemperature = root.findViewById(R.id.rg_temperature);

        btnSubmitForm = root.findViewById(R.id.btn_submit_form);

        //Autocomplete textview for states
        userState = root.findViewById(R.id.state_name);
        userState.setKeyListener(null);
        ArrayAdapter sAdapter = new ArrayAdapter<>(getContext(), R.layout.dropdown_pop_up_item, LocalDataSource.districts());
        userState.setAdapter(sAdapter);

        //Autocomplete textview for hospitals
        formHospital = root.findViewById(R.id.hospital_name);
        formHospital.setKeyListener(null);
        HospitalViewModelFactory mfactory = new HospitalViewModelFactory(getActivity().getApplication());
        HospitalViewModel viewModel = new ViewModelProvider(this, mfactory).get(HospitalViewModel.class);
        viewModel.getAllHospitals().observe(getViewLifecycleOwner(), hospitals -> {

            ArrayAdapter mAdapter = new ArrayAdapter<>(getContext(), R.layout.dropdown_pop_up_item, LocalDataSource.testingCenters());
            formHospital.setAdapter(mAdapter);
        });


        rgBreathing.setOnCheckedChangeListener((group, checkedId) -> {
            answerOne = root.findViewById(checkedId);
            breathAnswer = answerOne.getText().toString();
        });

        rgChestPain.setOnCheckedChangeListener((group, checkedId) -> {
            answerTwo = root.findViewById(checkedId);
            chestAnswer = answerTwo.getText().toString();
        });

        rgTemperature.setOnCheckedChangeListener((group, checkedId) -> {
            answerThree = root.findViewById(checkedId);
            tempAnswer = answerThree.getText().toString();
        });

        rgCough.setOnCheckedChangeListener((group, checkedId) -> {
            answerFour = root.findViewById(checkedId);
            coughAnswer = answerFour.getText().toString();
        });


        btnSubmitForm.setOnClickListener(v ->{
            sumbitForm();
        });

        return root;
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
            Toast.makeText(getActivity(), "Please choose YES, NO or SOMETIMES", Toast.LENGTH_SHORT).show();
        } else if(name.isEmpty()){
            userName.setError("Please provide your name");
        }
        else if(state.isEmpty()){
            userState.setError("Please provide your State");
        }else if(phone.isEmpty()){
            userPhone.setError("Please provide your phone number");
        }else if(gender.isEmpty()){
            Toast.makeText(getActivity(), "Please you must provide your gender", Toast.LENGTH_SHORT).show();
        }else {
            //Form submission
            Form form = new Form(user, symptom, hospital, mLat, mLong);
            viewModel.sendFormData(form);

            builder.setTitle("Thank you for submitting your symptoms")
                    .setMessage("Our health staff will contact you soon")
                    .setPositiveButton("Okay", (dialog, which) -> {
                        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
                        navController.navigate(R.id.nav_home);
                    });
            builder.setCancelable(false);
            builder.show();
        }

    }

    @SuppressLint("MissingPermission")
    private void getLastLocation(){
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
            Toast.makeText(getActivity(), "Turn on the device location", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
         }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData(){

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
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
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    @Override
    public void onResume(){
        super.onResume();
        getLastLocation();
    }
}
