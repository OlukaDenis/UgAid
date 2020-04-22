package com.app.ugaid.view.ui.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.app.ugaid.data.workers.LocationWorker;
import com.app.ugaid.utils.Config;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.app.ugaid.R;
import com.app.ugaid.data.api.ApiClient;
import com.app.ugaid.data.api.ApiService;
import com.app.ugaid.data.workers.CovidWorker;
import com.app.ugaid.model.CoronaCountry;
import com.app.ugaid.model.CountryInfo;
import com.app.ugaid.model.Covid;
import com.app.ugaid.view.ui.hospitals.HospitalViewModel;
import com.app.ugaid.view.ui.hospitals.HospitalViewModelFactory;
import com.app.ugaid.view.ui.symptom_form.SymptomFormActivity;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.ugaid.utils.Config.COUNTRY_STATS_WORKER;
import static com.app.ugaid.utils.Config.DEVICE_LOCATIONS_WORKER;
import static com.app.ugaid.utils.Config.EMERGENCY_NUMBER;
import static com.app.ugaid.utils.Config.PERMISSIONS;
import static com.app.ugaid.utils.Config.PERMISSION_ID;
import static com.app.ugaid.utils.Config.UGANDA;
import static com.app.ugaid.utils.Config.UPDATED;
import static com.app.ugaid.utils.Config.formatNumber;

public class HomeFragment extends Fragment {
    private WorkManager locationWorker;

    private TextView tvCases, tvDeaths, tvRecovered, ugCases, ugDeaths, ugRecovered, ugCasesToday, ugDeathsToday, moreCountries, moreFacts;
    private ImageView ugandaFlag;
    private Button btnSymptom, btnTest, btn_donate;
    private MaterialButton callEmergency;
    private TextView countryName;
    private HomeViewModel viewModel;
    private static final String TAG = "HomeFragment";
    private FirebaseAnalytics mFirebaseAnalytics;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        //check for permissions
        checkForPermissions();

        //Init firebase analytics
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
        mFirebaseAnalytics.setCurrentScreen(getActivity(), this.getClass().getSimpleName(),
                this.getClass().getSimpleName());

        //view reference
        tvCases = root.findViewById(R.id.cases);
        tvDeaths = root.findViewById(R.id.deaths);
        tvRecovered = root.findViewById(R.id.recovered);
        ugCases = root.findViewById(R.id.uganda_cases);
        ugDeaths = root.findViewById(R.id.uganda_deaths);
        ugRecovered = root.findViewById(R.id.uganda_recovered);
        moreCountries = root.findViewById(R.id.more_countries);
        moreFacts = root.findViewById(R.id.more_facts);
        ugCasesToday = root.findViewById(R.id.uganda_cases_today);
        ugDeathsToday = root.findViewById(R.id.uganda_deaths_today);
        ugandaFlag = root.findViewById(R.id.uganda_flag);
        countryName = root.findViewById(R.id.country_name_status);
        btnSymptom = root.findViewById(R.id.btn_submit_info);
        btnTest = root.findViewById(R.id.btn_self_test);
        btn_donate = root.findViewById(R.id.btn_donate);
        callEmergency = root.findViewById(R.id.btn_call_emergency);

        btnSymptom.setOnClickListener(v -> startActivity(new Intent(getActivity(), SymptomFormActivity.class)) );
        moreCountries.setOnClickListener(v -> openCountryFragment());
        moreFacts.setOnClickListener(v -> openFactsFragment());
        btnTest.setOnClickListener(v -> openSelfTestFragment());
        btn_donate.setOnClickListener(v -> openDonateFragment());
        callEmergency.setOnClickListener(v -> callEmergencyNow());


        com.app.ugaid.view.ui.home.HomeViewModelFactory factory = new HomeViewModelFactory(this.getActivity().getApplication());
        viewModel = new ViewModelProvider(this, factory).get(HomeViewModel.class);


        if (Config.isNetworkAvailable(getActivity())){
            getGlobalStats();
            getCountryCoronaStats();
        }

        // initializing a countries work manager
        WorkManager countriesWorker = WorkManager.getInstance(getActivity());
        PeriodicWorkRequest countryStatsRequest = new PeriodicWorkRequest.Builder(CovidWorker.class, 15, TimeUnit.MINUTES)
                .build();
        countriesWorker.enqueueUniquePeriodicWork(COUNTRY_STATS_WORKER,
                ExistingPeriodicWorkPolicy.KEEP,
                countryStatsRequest);


        return root;

    }

    private void callEmergencyNow() {
        if (Config.hasPermissions(getContext(), PERMISSIONS)){
            startActivity(new Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:" + EMERGENCY_NUMBER)));
        } else {
            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, PERMISSION_ID);
        }
    }

    private void openCountryFragment() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        navController.navigate(R.id.nav_countries);

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "View Country Stats");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle);
    }

    private void openSelfTestFragment() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        navController.navigate(R.id.nav_faq);

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Start self-Test");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle);
    }

    private void openFactsFragment() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        navController.navigate(R.id.nav_facts);

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "View Covid Facts");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle);
    }

    private void openDonateFragment() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        navController.navigate(R.id.nav_donate);

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Start donation");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle);
    }

    private void getCountryCoronaStats(){
        ApiService service = ApiClient.getApiService(ApiService.class);
        Call<CoronaCountry> call = service.getOneCountry(UGANDA);
        call.enqueue(new Callback<CoronaCountry>() {
            @Override
            public void onResponse(Call<CoronaCountry> call, Response<CoronaCountry> response) {
                if (response.isSuccessful()){
                    CoronaCountry country = response.body();
                    if (country != null) {
                        countryStats(country);
                    }
                }
            }

            @Override
            public void onFailure(Call<CoronaCountry> call, Throwable t) {
                Log.e(TAG, "onFailure: ",t );
            }
        });

    }

    private void countryStats(CoronaCountry country){
        CountryInfo countryInfo = country.getCountryInfo();
        Picasso.get()
                .load(countryInfo.getFlag())
                .placeholder(R.drawable.ic_flag)
                .error(R.drawable.ic_flag)
                .into(ugandaFlag);
        ugCases.setText(formatNumber(country.getCases()));
        ugDeaths.setText(formatNumber(country.getDeaths()));
        ugRecovered.setText(formatNumber(country.getRecovered()));

        Resources res = getResources();

        countryName.setText(String.format(res.getString(R.string.country_corona_status) , country.getCountry()));

        ugCasesToday.setText(String.format(res.getString(R.string.today), country.getTodayCases()));
        ugDeathsToday.setText(String.format(res.getString(R.string.today), country.getTodayDeaths()));
    }

    private void getGlobalStats(){
        ApiService service = ApiClient.getApiService(ApiService.class);
        Call<Covid> call = service.getWorldStats();
        call.enqueue(new Callback<Covid>() {
            @Override
            public void onResponse(Call<Covid> call, Response<Covid> response) {
                if (response.isSuccessful()){
                    Covid covid = response.body();
                    if (covid != null) {
                       populateStats(covid);
                    }
                }
            }

            @Override
            public void onFailure(Call<Covid> call, Throwable t) {
                Log.e(TAG, "onFailure: ",t );
            }
        });

    }

    private void populateStats(Covid covid) {
        tvCases.setText(formatNumber(covid.getCases()));
        tvDeaths.setText(formatNumber(covid.getDeaths()));
        tvRecovered.setText(formatNumber(covid.getRecovered()));
    }

    private void checkForPermissions() {
        if (!Config.hasPermissions(getActivity(), PERMISSIONS)) {
            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, PERMISSION_ID);
        } else {
            startLocationWorker();
        }
    }

    private void startLocationWorker() {
        //Sending device locations worker
        WorkManager locationsWorkManager = WorkManager.getInstance(getActivity());
        Constraints locationConstraints = new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build();
        PeriodicWorkRequest locationRequest = new PeriodicWorkRequest.Builder(LocationWorker.class,
                5,
                TimeUnit.MINUTES)
                .build();
        locationsWorkManager.enqueueUniquePeriodicWork(DEVICE_LOCATIONS_WORKER,
                ExistingPeriodicWorkPolicy.KEEP,
                locationRequest);
    }
}
