package com.app.ugaid.view.ui.splashscreen;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.app.ugaid.R;
import com.app.ugaid.data.workers.GlobalCoronaWorker;
import com.app.ugaid.data.workers.LocationWorker;
import com.app.ugaid.utils.Config;
import com.app.ugaid.view.ui.HomeActivity;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.app.ugaid.utils.Config.DEVICE_LOCATIONS_WORKER;
import static com.app.ugaid.utils.Config.GLOBAL_STATS_WORKER;
import static com.app.ugaid.utils.Config.PERMISSIONS;
import static com.app.ugaid.utils.Config.PERMISSION_ID;

public class SplashActivity extends AppCompatActivity {
    private AlertDialog.Builder builder;
    private static final String TAG = "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        builder = new AlertDialog.Builder(this);
        generateUniqueID();

        if (!Config.hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ID);
        }

        if (Config.isNetworkAvailable(this)) {
            //global stats periodic work request
            WorkManager globalStatsWorkManger = WorkManager.getInstance(this);
            PeriodicWorkRequest globalStatsRequest = new PeriodicWorkRequest.Builder(GlobalCoronaWorker.class, 15, TimeUnit.MINUTES)
                    .build();
            globalStatsWorkManger.enqueueUniquePeriodicWork(GLOBAL_STATS_WORKER,
                    ExistingPeriodicWorkPolicy.KEEP,
                    globalStatsRequest);

            new Handler().postDelayed(() -> {
                // This method will be executed once the timer is over
                Intent i = new Intent(SplashActivity.this, HomeActivity.class);
                startActivity(i);
                finish();
            }, 4000);


        } else {
            builder.setMessage("No internet connection!")
                    .setPositiveButton("Try Again", (dialog, which) -> startActivity(new Intent(this, SplashActivity.class)));
            builder.setCancelable(false);
            builder.show();
        }
    }

    private void generateUniqueID() {

        String uniqueID = UUID.randomUUID().toString();
        SharedPreferences idSharedPref = getApplicationContext().getSharedPreferences(getString(R.string.unique_id_prefs), Context.MODE_PRIVATE);

        if (!idSharedPref.contains(getString(R.string.unique_device_id))) {
            SharedPreferences.Editor editor = idSharedPref.edit();
            editor.putString(getString(R.string.unique_device_id), uniqueID);
            editor.apply();
        }
    }
}
