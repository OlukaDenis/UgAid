package com.app.ugaid.data.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioAttributes;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.app.ugaid.R;
import com.app.ugaid.utils.Config;
import com.app.ugaid.view.ui.HomeActivity;

import java.util.Map;
import java.util.Objects;

import static com.app.ugaid.utils.Config.FCM_NOTIFICATION_CHANNEL;
import static com.app.ugaid.utils.Config.FCM_NOTIFICATION_ID;
import static com.app.ugaid.utils.Config.NOTIFICATION_ID;

public class MessagingService extends FirebaseMessagingService {
    private static final String TAG = "MessagingService";

    @Override
    public void onNewToken(@NonNull String s) {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "getInstanceId failed", task.getException());
                        return;
                    }

                    // Get new Instance ID token
                    String token = Objects.requireNonNull(task.getResult()).getToken();

                    // Log and toast
                    Log.d(TAG, "New message token: "+token);
                });
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        int type = getSharedPreferences("login_info",MODE_PRIVATE).getInt("usertype",-1);

        Map<String, String> data = remoteMessage.getData();
        String body = data.get("body");
        String title = data.get("title");

        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);

        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(),
                NOTIFICATION_ID,
                intent,
                PendingIntent.FLAG_ONE_SHOT);

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);

        createMessageNotificationChannel(notificationManager);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(
                        getApplicationContext(), FCM_NOTIFICATION_ID)
                        .setContentTitle(title)
                        .setAutoCancel(true)
                        .setLargeIcon(((BitmapDrawable)getDrawable(R.drawable.logo)).getBitmap())
                        .setContentText(body)
                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                        .setSmallIcon(R.drawable.ic_platu_notification)
                        .setContentIntent(pi)
                ;

        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private void createMessageNotificationChannel(NotificationManager notificationManager){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    FCM_NOTIFICATION_ID,
                    FCM_NOTIFICATION_CHANNEL,
                    NotificationManager.IMPORTANCE_HIGH
            );

            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setLightColor(Color.BLUE);
            channel.setDescription("Notification from the Firebase Cloud Messaging");

            notificationManager.createNotificationChannel(channel);
        }
    }
}
