package com.app.ugaid.data.receivers;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import com.app.ugaid.data.workers.BluetoothWorker;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static com.app.ugaid.utils.Config.BLUETOOTH_DISCOVERY_WORKER;


public class BluetoothReceiver extends BroadcastReceiver {
    private static final String TAG = "BluetoothReceiver";
    private ArrayList<String> deviceList = new ArrayList<>();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Bluetooth receiver onReceive called ..... ");

        final String action = intent.getAction();

        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            assert device != null;
            String deviceName = device.getName();
            String deviceHardwareAddress = device.getAddress(); // MAC address

            deviceList.add(deviceName +": "+ deviceHardwareAddress);

            if (deviceList != null){
                WorkManager workManager = WorkManager.getInstance(context);

                Data.Builder data = new Data.Builder();
                data.putInt("people", deviceList.size());

                PeriodicWorkRequest notifyWork = new PeriodicWorkRequest.Builder(BluetoothWorker.class,
                        5,
                        TimeUnit.MINUTES)
                        .setInputData(data.build())
                        .build();

                workManager.enqueueUniquePeriodicWork(BLUETOOTH_DISCOVERY_WORKER,
                        ExistingPeriodicWorkPolicy.REPLACE,
                        notifyWork);
            }
        }
    }
}
