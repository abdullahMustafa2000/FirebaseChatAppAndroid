package com.example.mychatapplication.Notification;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.mychatapplication.UI.ChatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static com.example.mychatapplication.Adapters.AdapterUsers.HIS_UID;
import static com.example.mychatapplication.UI.DashboardActivity.CURRENT_UID;
import static com.example.mychatapplication.UI.DashboardActivity.UID_SHARED_PREF;

public class FirebaseService extends FirebaseMessagingService {

    public static final String TOKEN_REF = "Token";
    private SharedPreferences preferences;
    SharedPreferences.Editor editor;
    public static final String  DEVICE_TOKEN = "device_token";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Toast.makeText(this, "EEEE", Toast.LENGTH_SHORT).show();
        preferences = getSharedPreferences(UID_SHARED_PREF, MODE_PRIVATE);
        String savedUID = preferences.getString(CURRENT_UID, "null");
        String sent = remoteMessage.getData().get("sent");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null && sent.equals(user.getUid())){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                    sendOreoAndAbove(remoteMessage);
                } else {
                    sendNormalNotification(remoteMessage);
                }
        }
    }

    private void sendNormalNotification(RemoteMessage remoteMessage) {
        String user = remoteMessage.getData().get("user");
        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("title");
        String body= remoteMessage.getData().get("body");

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        int i = Integer.parseInt(user.replaceAll("[\\D]", ""));
        Intent intent = new Intent(this, ChatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(HIS_UID, user);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, i, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri defSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(Integer.parseInt(icon))
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        int j = 0;
        if (i > 0){
            j = i;
        }

        notificationManager.notify(j, builder.build());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendOreoAndAbove(RemoteMessage remoteMessage) {
        String user = remoteMessage.getData().get("user");
        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("title");
        String body= remoteMessage.getData().get("body");

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        int i = Integer.parseInt(user.replaceAll("[\\D]", ""));
        Intent intent = new Intent(this, ChatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(HIS_UID, user);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, i, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri defSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationHelper notificationHelper = new NotificationHelper(this);
        Notification.Builder builder = notificationHelper.getOnNotifications(title,
                body, pendingIntent, defSoundUri, icon);
        int j = 0;
        if (i > 0){
            j = i;
        }

        notificationHelper.getNotificationManager().notify(j, builder.build());
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String tokenRefresh = FirebaseInstanceId.getInstance().getToken();
        preferences = getSharedPreferences(UID_SHARED_PREF, MODE_PRIVATE);
        editor = preferences.edit();
        editor.putString(DEVICE_TOKEN, tokenRefresh);
        editor.apply();
        if (user != null){
            updateToken(tokenRefresh);
        }
    }

    private void updateToken(String tokenRefresh) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(TOKEN_REF);
        Token token = new Token(tokenRefresh);
        databaseReference.child(user.getUid()).setValue(token);
    }
}
