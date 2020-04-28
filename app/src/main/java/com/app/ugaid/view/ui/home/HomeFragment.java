package com.app.ugaid.view.ui.home;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.app.ugaid.data.receivers.BluetoothReceiver;
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
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.app.ugaid.utils.Config.COUNTRY_STATS_WORKER;
import static com.app.ugaid.utils.Config.DEVICE_LOCATIONS_WORKER;
import static com.app.ugaid.utils.Config.EMERGENCY_NUMBER;
import static com.app.ugaid.utils.Config.REQUEST_ENABLE_BLUETOOTH;
import static com.app.ugaid.utils.Config.UGANDA;
import static com.app.ugaid.utils.Config.formatNumber;

public class HomeFragment extends Fragment {
    private TextView tvCases;
    private TextView tvDeaths;
    private TextView tvRecovered;
    private TextView ugCases;
    private TextView ugDeaths;
    private TextView ugRecovered;
    private TextView ugCasesToday;
    private TextView ugDeathsToday;
    private ImageView ugandaFlag;
    private TextView countryName;
    private static final String TAG = "HomeFragment";
    private FirebaseAnalytics mFirebaseAnalytics;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothReceiver receiver;
    private NavController navController;
    private LocationManager locationManager;
    private AlertDialog.Builder builder;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        receiver = new BluetoothReceiver();
        locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
        builder = new AlertDialog.Builder(getContext());

        //Check whether location is enabled
        checkLocationEnabled();
        startLocationWorker();

        //Enable bluetooth
        checkForBluetooth();
        startBroadCast();

        //Init firebase analytics
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(requireActivity());
        mFirebaseAnalytics.setCurrentScreen(requireActivity(), this.getClass().getSimpleName(),
                this.getClass().getSimpleName());

        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);

        //view reference
        tvCases = root.findViewById(R.id.cases);
        tvDeaths = root.findViewById(R.id.deaths);
        tvRecovered = root.findViewById(R.id.recovered);
        ugCases = root.findViewById(R.id.uganda_cases);
        ugDeaths = root.findViewById(R.id.uganda_deaths);
        ugRecovered = root.findViewById(R.id.uganda_recovered);
        TextView moreCountries = root.findViewById(R.id.more_countries);
        TextView moreFacts = root.findViewById(R.id.more_facts);
        ugCasesToday = root.findViewById(R.id.uganda_cases_today);
        ugDeathsToday = root.findViewById(R.id.uganda_deaths_today);
        ugandaFlag = root.findViewById(R.id.uganda_flag);
        countryName = root.findViewById(R.id.country_name_status);
        Button btnRequest = root.findViewById(R.id.btn_submit_info);
        Button btnTest = root.findViewById(R.id.btn_self_test);
        Button btn_donate = root.findViewById(R.id.btn_donate);
        MaterialButton callEmergency = root.findViewById(R.id.btn_call_emergency);

        btnRequest.setOnClickListener(v -> openTestRequestFragment() );
        moreCountries.setOnClickListener(v -> openCountryFragment());
        moreFacts.setOnClickListener(v -> openFactsFragment());
        btnTest.setOnClickListener(v -> openSelfTestFragment());
        btn_donate.setOnClickListener(v -> openDonateFragment());
        callEmergency.setOnClickListener(v -> callEmergencyNow());


        HomeViewModelFactory factory = new HomeViewModelFactory(requireActivity().getApplication());
        HomeViewModel viewModel = new ViewModelProvider(this, factory).get(HomeViewModel.class);


        if (Config.isNetworkAvailable(requireActivity())){
            getGlobalStats();
            getCountryCoronaStats();
        }

        // initializing a countries work manager
        WorkManager countriesWorker = WorkManager.getInstance(requireContext());
        PeriodicWorkRequest countryStatsRequest = new PeriodicWorkRequest.Builder(CovidWorker.class, 15, TimeUnit.MINUTES)
                .build();
        countriesWorker.enqueueUniquePeriodicWork(COUNTRY_STATS_WORKER,
                ExistingPeriodicWorkPolicy.KEEP,
                countryStatsRequest);
        return root;

    }

    private void startLocationWorker() {
        Log.d(TAG, "startLocationWorker called .... ");
        //Sending device locations worker
        WorkManager locationsWorkManager = WorkManager.getInstance(requireContext());
        PeriodicWorkRequest locationRequest = new PeriodicWorkRequest.Builder(LocationWorker.class,
                60,
                TimeUnit.MINUTES)
                .build();
        locationsWorkManager.enqueueUniquePeriodicWork(DEVICE_LOCATIONS_WORKER,
                ExistingPeriodicWorkPolicy.KEEP,
                locationRequest);
    }

    private void checkLocationEnabled() {
        if( !(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))){
                builder
                    .setTitle("Turn on device location")  // GPS not found
                    .setMessage("UgAID requires location, please click ok to turn on the location from your device settings") // Want to enable?
                    .setPositiveButton("OK", (dialogInterface, i) -> requireActivity().startActivity(
                            new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
                    .setCancelable(false);
                builder.show();
        } else {
            startLocationWorker();
        }
    }

    private void startBroadCast() {
        Log.d(TAG, "startBroadCast called ....");
        //An intent to notify the broadcast receiver
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        requireActivity().registerReceiver(receiver, filter);
        bluetoothAdapter.startDiscovery();
    }

    private void checkForBluetooth(){
        Log.d(TAG, "checkForBluetooth called..... ");
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            // Device doesn't support Bluetooth
            Toast.makeText(getActivity(), "Your device doesn't support bluetooth.", Toast.LENGTH_SHORT).show();
        } else {

            if (!bluetoothAdapter.isEnabled()) {
                Log.d(TAG, "Enabling the bluetooth.......");
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BLUETOOTH);
            } else {
                startBroadCast();
            }
        }
    }

    private void makeBluetoothDiscoverable() {
        Log.d(TAG, "makeBluetoothDiscoverable called ..... ");
        Intent discoverableIntent =
                new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
        startActivity(discoverableIntent);
        Toast.makeText(getActivity(), "Your device is now discoverable to nearby Bluetooth devices", Toast.LENGTH_SHORT).show();
    }

    private void callEmergencyNow() {
            startActivity(new Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:" + EMERGENCY_NUMBER)));
    }

    private void openTestRequestFragment() {
        navController.navigate(R.id.nav_request_form);

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Open request form");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle);
    }

    private void openCountryFragment() {
        navController.navigate(R.id.nav_countries);

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "View Country Stats");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle);
    }

    private void openSelfTestFragment() {
        navController.navigate(R.id.nav_faq);

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Start self-Test");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle);
    }

    private void openFactsFragment() {
        navController.navigate(R.id.nav_facts);

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "View Covid Facts");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle);
    }

    private void openDonateFragment() {
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
            public void onResponse(@NotNull Call<CoronaCountry> call, @NotNull Response<CoronaCountry> response) {
                if (response.isSuccessful()){
                    CoronaCountry country = response.body();
                    if (country != null) {
                        countryStats(country);
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<CoronaCountry> call, Throwable t) {
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
            public void onResponse(@NotNull Call<Covid> call, @NotNull Response<Covid> response) {
                if (response.isSuccessful()){
                    Covid covid = response.body();
                    if (covid != null) {
                       populateStats(covid);
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<Covid> call, Throwable t) {
                Log.e(TAG, "onFailure: ",t );
            }
        });

    }

    private void populateStats(Covid covid) {
        tvCases.setText(formatNumber(covid.getCases()));
        tvDeaths.setText(formatNumber(covid.getDeaths()));
        tvRecovered.setText(formatNumber(covid.getRecovered()));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ENABLE_BLUETOOTH && resultCode == RESULT_OK) {
            Toast.makeText(getActivity(), "Bluetooth enabled", Toast.LENGTH_SHORT).show();
            makeBluetoothDiscoverable();
        }
    }



}
