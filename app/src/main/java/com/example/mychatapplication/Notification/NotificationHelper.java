package com.example.mychatapplication.Notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.mychatapplication.R;

public class NotificationHelper extends ContextWrapper {
    public static final String CHANNEL_ID = "channel 1";
    public static final String CHANNEL_NAME = "alarm channel";

    public static final String CHANNEL2_ID = "channel 2";
    public static final String CHANNEL2_NAME = "alarm channel2";

    NotificationManager notificationManager;
    public NotificationHelper(Context base) {
        super(base);
        createChannels();
    }

    public void createChannels() {
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel1 = new NotificationChannel(CHANNEL_ID
                    ,"alarm channel", NotificationManager.IMPORTANCE_DEFAULT);
            channel1.enableLights(true);
            channel1.enableVibration(true);
            channel1.setLightColor(R.color.colorPrimary);
            channel1.setDescription("music notification listening");
            getNotificationManager().createNotificationChannel(channel1);

            NotificationChannel channel2 = new NotificationChannel(CHANNEL2_ID
                    ,"alarm channel", NotificationManager.IMPORTANCE_DEFAULT);
            channel2.enableLights(true);
            channel2.enableVibration(true);
            channel2.setLightColor(R.color.colorPrimary);
            channel2.setDescription("music notification listening");
            getNotificationManager().createNotificationChannel(channel2);
        }
    }
    public NotificationManager getNotificationManager(){
        if (notificationManager == null){
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return notificationManager;
    }

    public NotificationCompat.Builder getChannel1Notification(String title, String message){
        return new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_launcher_background);
    }
    public NotificationCompat.Builder getChannel2Notification(String title, String message){
        return new NotificationCompat.Builder(getApplicationContext(), CHANNEL2_ID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_launcher_background);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Notification.Builder getOnNotifications(String title, String body,
                                                   PendingIntent pIntent, Uri soundUri,
                                                   String icon){
        return new Notification.Builder(getApplicationContext(), CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(body)
                .setSound(soundUri)
                .setAutoCancel(true)
                .setSmallIcon(Integer.parseInt(icon))
                .setContentIntent(pIntent);
    }
}
