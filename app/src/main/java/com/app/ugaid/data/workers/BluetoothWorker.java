package com.app.ugaid.data.workers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.app.ugaid.R;
import com.app.ugaid.data.receivers.BluetoothReceiver;
import com.app.ugaid.view.ui.HomeActivity;

import java.util.concurrent.Executor;

import static com.app.ugaid.utils.Config.BLUETOOTH_CHANNEL_ID;
import static com.app.ugaid.utils.Config.BLUETOOTH_NOTIFICATION_ID;

public class BluetoothWorker extends Worker {
    private static final String TAG = "BluetoothWorker";
    private NotificationManager notificationManager;

    public BluetoothWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        Log.d(TAG, "BluetoothWorker called.... ");
    }

    @NonNull
    @Override
    public Result doWork() {
        Context context = getApplicationContext();

        int people = getInputData().getInt("people", 1);
        if (people == 1) {
            String num = people + " person. ";
            showNotification(context, num);
        } else {
            String number = people + " people. ";
            showNotification(context, number);
        }


        return Result.success();

    }

    private void showNotification(Context context, String people) {
        notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        //create the notification channel for the app
        createWorkerNotificationChannel();

        Intent workerIntent = new Intent(context, HomeActivity.class);
        PendingIntent workerPendingIntent = PendingIntent.getActivity(context,
                BLUETOOTH_NOTIFICATION_ID,
                workerIntent,
                PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder = getNotificationBuilder(context, people);
        builder.setContentIntent(workerPendingIntent);

        notificationManager.notify(BLUETOOTH_NOTIFICATION_ID, builder.build());
    }


    private void createWorkerNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            NotificationChannel channel = new NotificationChannel(BLUETOOTH_CHANNEL_ID,
                    "Bluetooth Notification",
                    NotificationManager.IMPORTANCE_HIGH);
            channel.enableLights(true);
            channel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);
            channel.enableVibration(true);
            channel.setLightColor(Color.BLUE);
            channel.setDescription("Bluetooth available devices notification");

            notificationManager.createNotificationChannel(channel);
        }
    }

    private NotificationCompat.Builder getNotificationBuilder(Context context, String people){
        return new NotificationCompat.Builder(context, BLUETOOTH_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("UgAID: Social Distance Alert")
                .setContentText("It has been noticed that you are close to "+ people +
                        "Please keep a 4 meter distance away from people to avoid risk of contracting COVID-19.")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("It has been noticed that you are close to " +people +
                                "Please keep a 4 meter distance away from people to avoid risk of contracting COVID-19."))
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_SOUND)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
    }


}
