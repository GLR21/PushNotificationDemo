package com.example.pushnotificationdemo;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    // Channel ID must match @string/default_notification_channel_id
    private static final String CHANNEL_ID =
            "high_importance_channel";

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        // TODO: send this token to your server if needed
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Ensure the channel exists on Oreo+ devices
        createNotificationChannel();

        String title = remoteMessage.getNotification() != null
                ? remoteMessage.getNotification().getTitle()
                : "FCM Message";
        String body = remoteMessage.getNotification() != null
                ? remoteMessage.getNotification().getBody()
                : "You have a new message.";

        Intent intent = new Intent(this, MainActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, intent,
                PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent);

        NotificationManager manager =
                (NotificationManager) getSystemService(NotificationManager.class);
        manager.notify(0, notificationBuilder.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "High Importance Notifications",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Used for important FCM notifications");
            NotificationManager manager =
                    getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }
}
