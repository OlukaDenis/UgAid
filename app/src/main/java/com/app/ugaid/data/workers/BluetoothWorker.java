package com.app.ugaid.data.workers;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.IntentFilter;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.app.ugaid.data.receivers.BluetoothReceiver;

import java.util.concurrent.Executor;

public class BluetoothWorker extends Worker {
    private static final String TAG = "BluetoothWorker";
    private BluetoothReceiver receiver;
    private BluetoothAdapter bluetoothAdapter;

    public BluetoothWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        receiver = new BluetoothReceiver();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Log.d(TAG, "BluetoothWorker called.... ");
    }

    @NonNull
    @Override
    public Result doWork() {
        Context context = getApplicationContext();

        if (bluetoothAdapter.isEnabled()) {

            Log.d(TAG, "Bluetooth enabled, starting discovery...... ");
            // Register for broadcasts when a device is discovered.
            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            context.registerReceiver(receiver, filter);
        }

        return Result.success();

    }


}
