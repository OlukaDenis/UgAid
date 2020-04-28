package com.app.ugaid.view.ui.self_test;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.app.ugaid.R;
import com.app.ugaid.model.Test;
import com.app.ugaid.utils.Config;

import static com.app.ugaid.utils.Config.BODY_ACHES;
import static com.app.ugaid.utils.Config.BREATHING_DIFFICULTY;
import static com.app.ugaid.utils.Config.COLD;
import static com.app.ugaid.utils.Config.COUGH;
import static com.app.ugaid.utils.Config.DIARRHEA;
import static com.app.ugaid.utils.Config.DIRECT_CONTACT;
import static com.app.ugaid.utils.Config.FATIGUE;
import static com.app.ugaid.utils.Config.FEVER;
import static com.app.ugaid.utils.Config.HEADACHE;
import static com.app.ugaid.utils.Config.PERMISSION_ID;
import static com.app.ugaid.utils.Config.SORE_THROAT;
import static com.app.ugaid.utils.Config.TRAVEL;
import static com.app.ugaid.utils.Config.TRAVEL_HISTORY;

public class SelfTestFragment extends Fragment {
    private TextView test_question, probableText, actionText;
    private Button btnYes, btnNo, btn_reset;
    private Test self_test;
    private Resources res;
    private Animation txtAnim;
    private int probability, selectedTests = 0;
    private static int cough, cold, diarrhea, sore_throat, body_aches, headache, fever, breathing, fatigue, travel, travel_history, direct_contact;
    private CardView resultsCard, testCard;
    private SelfTestViewModel viewModel;
    private FirebaseAnalytics mFirebaseAnalytics;
    private FusedLocationProviderClient mFusedLocationClient;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View root = inflater.inflate(R.layout.fragment_faq, container, false);

        //Init firebase analytics
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
        mFirebaseAnalytics.setCurrentScreen(getActivity(), this.getClass().getSimpleName(), this.getClass().getSimpleName());

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        getLastLocation();

        btnYes = root.findViewById(R.id.btn_yes);
        btnNo = root.findViewById(R.id.btn_no);
        test_question = root.findViewById(R.id.test_question);
        resultsCard = root.findViewById(R.id.test_results_card);
        testCard = root.findViewById(R.id.test_questions_card);
        probableText = root.findViewById(R.id.probability_value);
        actionText = root.findViewById(R.id.action_text);
        btn_reset = root.findViewById(R.id.btn_reset);

        resultsCard.setVisibility(View.GONE);

        txtAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.to_left);

        self_test = new Test();
        res = getResources();

        SelfTestViewModelFactory mfactory = new SelfTestViewModelFactory(getActivity().getApplication());
        viewModel = new ViewModelProvider(this, mfactory).get(SelfTestViewModel.class);

        TestOne();
        return root;
    }

    private void setAnim(){
        txtAnim.reset();
        test_question.clearAnimation();
        test_question.startAnimation(txtAnim);
    }

    private void TestOne(){
        setAnim();
        test_question.setText(R.string.test1);
        btnYes.setOnClickListener(v -> {
            self_test.setCough(res.getString(R.string.response_1));
            cough = COUGH;
            selectedTests += 1;
            TestTwo();
        });
        btnNo.setOnClickListener(v -> {
            self_test.setCough(res.getString(R.string.response_2));
            cough = 0;
            TestTwo();
        });
    }

    private void TestTwo(){
        setAnim();
        test_question.setText(R.string.test2);
        btnYes.setOnClickListener(v -> {
            self_test.setCold(res.getString(R.string.response_1));
            cold = COLD;
            selectedTests += 1;
            TestThree();
        });
        btnNo.setOnClickListener(v -> {
            self_test.setCold(res.getString(R.string.response_2));
            cold = 0;
            TestThree();
        });
    }

    private void TestThree(){
        setAnim();

        test_question.setText(R.string.test3);
        btnYes.setOnClickListener(v -> {
            self_test.setBody_aches(res.getString(R.string.response_1));
            body_aches = BODY_ACHES;
            selectedTests += 1;
            TestFour();
        });
        btnNo.setOnClickListener(v -> {
            self_test.setBody_aches(res.getString(R.string.response_2));
            body_aches = 0;
            TestFour();
        });
    }

    private void TestFour(){
       setAnim();
        test_question.setText(R.string.test4);
        btnYes.setOnClickListener(v -> {
            self_test.setSore_throat(res.getString(R.string.response_1));
            sore_throat = SORE_THROAT;
            selectedTests += 1;
            TestFive();
        });
        btnNo.setOnClickListener(v -> {
            self_test.setSore_throat(res.getString(R.string.response_2));
            sore_throat = 0;
            TestFive();
        });
    }

    private void TestFive(){
       setAnim();
        test_question.setText(R.string.test5);
        btnYes.setOnClickListener(v -> {
            self_test.setDiarrhea(res.getString(R.string.response_1));
            diarrhea = DIARRHEA;
            selectedTests += 1;
            TestSix();
        });
        btnNo.setOnClickListener(v -> {
            self_test.setDiarrhea(res.getString(R.string.response_2));
            diarrhea = 0;
            TestSix();
        });
    }

    private void TestSix(){
       setAnim();

        test_question.setText(R.string.test6);
        btnYes.setOnClickListener(v -> {
            self_test.setHeadache(res.getString(R.string.response_1));
            headache = HEADACHE;
            selectedTests += 1;
            TestSeven();
        });
        btnNo.setOnClickListener(v -> {
            self_test.setHeadache(res.getString(R.string.response_2));
            headache = 0;
            TestSeven();
        });
    }

    private void TestSeven(){
        setAnim();
        test_question.setText(R.string.test7);
        btnYes.setOnClickListener(v -> {
            self_test.setFever(res.getString(R.string.response_1));
            fever = FEVER;
            selectedTests += 1;
            TestEight();
        });
        btnNo.setOnClickListener(v -> {
            self_test.setFever(res.getString(R.string.response_2));
            fever = 0;
            TestEight();
        });
    }

    private void TestEight(){
        setAnim();
        test_question.setText(R.string.test8);
        btnYes.setOnClickListener(v -> {
            self_test.setBreathing_difficulty(res.getString(R.string.response_1));
            breathing = BREATHING_DIFFICULTY;
            selectedTests += 1;
            TestNine();
        });
        btnNo.setOnClickListener(v -> {
            self_test.setBreathing_difficulty(res.getString(R.string.response_2));
            breathing = 0;
            TestNine();
        });
    }

    private void TestNine(){
        setAnim();
        test_question.setText(R.string.test9);
        btnYes.setOnClickListener(v -> {
            self_test.setFatigue(res.getString(R.string.response_1));
            fatigue = FATIGUE;
            selectedTests += 1;
            TestTen();
        });
        btnNo.setOnClickListener(v -> {
            self_test.setFatigue(res.getString(R.string.response_2));
            fatigue = 0;
            TestTen();
        });
    }

    private void TestTen(){
        setAnim();
        test_question.setText(R.string.test10);
        btnYes.setOnClickListener(v -> {
            self_test.setTravelled(res.getString(R.string.response_1));
            travel = TRAVEL;
            selectedTests += 1;
            TestEleven();
        });
        btnNo.setOnClickListener(v -> {
            self_test.setTravelled(res.getString(R.string.response_2));
            travel = 0;
            TestEleven();
        });
    }

    private void TestEleven(){
        setAnim();
        test_question.setText(R.string.test11);
        btnYes.setOnClickListener(v -> {
            self_test.setTravel_history(res.getString(R.string.response_1));
            travel_history = TRAVEL_HISTORY;
            selectedTests += 1;
            TestTwelve();
        });
        btnNo.setOnClickListener(v -> {
            self_test.setTravel_history(res.getString(R.string.response_2));
            travel_history = 0;
            TestTwelve();
        });
    }

    private void TestTwelve(){
        setAnim();
        test_question.setText(R.string.test12);
        btnYes.setOnClickListener(v -> {
            self_test.setDirect_contact(res.getString(R.string.response_1));
            direct_contact = DIRECT_CONTACT;
            selectedTests += 1;

//            Toast.makeText(getActivity(), self_test.info(), Toast.LENGTH_LONG).show();
            calculateProbability();
        });
        btnNo.setOnClickListener(v -> {
            self_test.setDirect_contact(res.getString(R.string.response_2));
            direct_contact = 0;
//            Toast.makeText(getActivity(), self_test.info(), Toast.LENGTH_LONG).show();
            calculateProbability();
        });
    }

    private void calculateProbability(){
        if (selectedTests == 0) {
            selectedTests += 12;
        }
        probability = (cough + cold + diarrhea +
                sore_throat + body_aches + headache +
                fever + breathing + fatigue + travel +
                travel_history + direct_contact) / selectedTests;


        displayResults();
    }

    private void displayResults() {
        testCard.setVisibility(View.GONE);
        resultsCard.setVisibility(View.VISIBLE);


        txtAnim.reset();
        resultsCard.clearAnimation();
        resultsCard.startAnimation(txtAnim);

        if(probability >= 75){
            probableText.setText(res.getString(R.string.severe));
            self_test.setResult(res.getString(R.string.severe));
            probableText.setTextColor(res.getColor(R.color.red));
            actionText.setText(res.getString(R.string.action_severe));
        } else if(probability >= 65){
            probableText.setText(res.getString(R.string.high));
            self_test.setResult(res.getString(R.string.high));
            probableText.setTextColor(res.getColor(R.color.red));
            actionText.setText(res.getString(R.string.action_high));
        } else if(probability >= 50) {
            probableText.setText(res.getString(R.string.medium));
            self_test.setResult(res.getString(R.string.medium));
            probableText.setTextColor(res.getColor(R.color.orange));
            actionText.setText(res.getString(R.string.action_medium));
        }else {
            probableText.setText(res.getString(R.string.low));
            self_test.setResult(res.getString(R.string.low));
            probableText.setTextColor(res.getColor(R.color.green));
            actionText.setText(res.getString(R.string.action_low));
        }

        viewModel.saveTests(self_test);

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Submit symptom form");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        btn_reset.setOnClickListener(v -> {
            probability = 0;
            cough = 0;
            cold = 0;
            diarrhea = 0;
            sore_throat = 0;
            body_aches = 0;
            headache = 0;
            fever = 0;
            breathing = 0;
            fatigue = 0;
            travel = 0;
            travel_history = 0;
            direct_contact = 0;
            selectedTests = 0;

            testCard.setVisibility(View.VISIBLE);
            resultsCard.setVisibility(View.GONE);
            txtAnim.reset();
            testCard.clearAnimation();
            testCard.startAnimation(txtAnim);
            self_test = new Test();
            TestOne();
        });
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
                            self_test.setLatitude(location.getLatitude());
                            self_test.setLongitude(location.getLongitude());
                        }
                    }
            );
        } else {
            Toast.makeText(getContext(), "Turn on the device location", Toast.LENGTH_LONG).show();
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
            self_test.setLatitude(mLastLocation.getLatitude());
            self_test.setLongitude(mLastLocation.getLongitude());
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