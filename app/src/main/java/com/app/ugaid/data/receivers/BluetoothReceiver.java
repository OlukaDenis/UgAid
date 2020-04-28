package com.app.ugaid.data.receivers;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.app.ugaid.R;
import com.app.ugaid.view.ui.HomeActivity;

import java.util.ArrayList;

import static com.app.ugaid.utils.Config.BLUETOOTH_CHANNEL_ID;
import static com.app.ugaid.utils.Config.BLUETOOTH_NOTIFICATION_ID;
import static com.app.ugaid.utils.Config.INTERVAL_FIVE_MINUTES;

public class BluetoothReceiver extends BroadcastReceiver {
    private static final String TAG = "BluetoothReceiver";
    private NotificationManager notificationManager;
    private ArrayList<String> deviceList = new ArrayList<>();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Bluetooth receiver onReceive called ..... ");
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        final String action = intent.getAction();

        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            assert device != null;
            String deviceName = device.getName();
            String deviceHardwareAddress = device.getAddress(); // MAC address

            deviceList.add(deviceName +": "+ deviceHardwareAddress);


            //create the notification channel for the app
            createWorkerNotificationChannel();

            Intent bluetoothIntent = new Intent(context, HomeActivity.class);
            PendingIntent bluetoothPendingIntent = PendingIntent.getActivity(context,
                    BLUETOOTH_NOTIFICATION_ID,
                    bluetoothIntent,
                    PendingIntent.FLAG_NO_CREATE);

            long repeatInterval = INTERVAL_FIVE_MINUTES;
            long triggerTime = SystemClock.elapsedRealtime() + repeatInterval;
            if(alarmManager != null) {
                // starting the alarm
                alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                        triggerTime, repeatInterval, bluetoothPendingIntent);

                if (deviceList != null) {
                    NotificationCompat.Builder builder = getNotificationBuilder(context, deviceList.size());
                    builder.setContentIntent(bluetoothPendingIntent);
                    notificationManager.notify(BLUETOOTH_NOTIFICATION_ID, builder.build());
                }
            }
        }
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

    private NotificationCompat.Builder getNotificationBuilder(Context context, int size){
        return new NotificationCompat.Builder(context, BLUETOOTH_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("UgAID: Social Distance Alert")
                .setContentText("It has been noticed that you are close to "+ size +" people." +
                        "Please keep a 4 meter distance away from people to avoid risk of contracting COVID-19.")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("It has been noticed that you are close to "+ size +" people." +
                                "Please keep a 4 meter distance away from people to avoid risk of contracting COVID-19."))
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_SOUND)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
    }
}
