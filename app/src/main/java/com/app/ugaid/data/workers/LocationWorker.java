package com.app.ugaid.data.workers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationManager;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.app.ugaid.R;
import com.app.ugaid.data.api.ApiService;
import com.app.ugaid.data.api.StatesApiClient;
import com.app.ugaid.data.db.CovidDatabase;
import com.app.ugaid.data.db.HospitalDao;
import com.app.ugaid.data.local.LocalDataSource;
import com.app.ugaid.model.DeviceLocation;
import com.app.ugaid.model.Hospital;
import com.app.ugaid.model.State;
import com.app.ugaid.utils.Config;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Timestamp;
import java.util.Date;


public class LocationWorker extends Worker {
    private static final String TAG = "LocationWorker";
    private FusedLocationProviderClient mFusedLocationClient;
    private FirebaseDatabase database;
    private SharedPreferences idSharedPref;

    public LocationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        database = FirebaseDatabase.getInstance();
        idSharedPref = context.getSharedPreferences(context.getString(R.string.unique_id_prefs), Context.MODE_PRIVATE);
        Log.d(TAG, "LocationWorker called: ");
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d(TAG, "Sending location data to firebase: ");
        Context context = getApplicationContext();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);

        try {
            //get timestamp
            Date date= new Date();
            long time = date.getTime();
            Timestamp stamp = new Timestamp(time);
            String timestamp = stamp.toString().replaceAll("[-+.^:,]","");

            //unique id
            String unique = idSharedPref.getString(context.getString(R.string.unique_device_id), "");
            getLastLocation(context, unique, timestamp);
            Log.d(TAG, "Unique Device ID: "+unique);
            return Result.success();

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Error sending location data", e);
            return Result.failure();
        }

    }

    @SuppressLint("MissingPermission")
    private void getLastLocation(Context context, String unique, String timestamp){
        Log.d(TAG, "Preparing the the exact device location...... ");
            if (isLocationEnabled(context)) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        task -> {
                            Location location = task.getResult();
                            if (location != null) {
                                DatabaseReference locationRef = database.getReference().child("device-locations");
                                DeviceLocation deviceLocation = new DeviceLocation(location.getLatitude(), location.getLongitude());
                                locationRef.child(unique).child(timestamp).setValue(deviceLocation);
                                Log.d(TAG, "LastLocation saved: " + deviceLocation.info());
                            }
                        }
                );
            }

    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData(Context context, String unique, String timestamp){

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, new LocationCallback(){
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        Location mLastLocation = locationResult.getLastLocation();

                        DatabaseReference locationRef = database.getReference().child("device-locations");
                        DeviceLocation deviceLocation = new DeviceLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                        locationRef.child(unique).child(timestamp).setValue(deviceLocation);
                    }
                },
                Looper.myLooper()
        );

    }

    private boolean isLocationEnabled(Context context){
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    @Override
    public void onStopped() {
        super.onStopped();
        Log.i(TAG, "OnStopped called for this Location worker");
    }
}
