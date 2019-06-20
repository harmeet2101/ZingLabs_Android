package com.zing.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.zing.R;
import com.zing.activity.DashboardActivity;
import com.zing.util.SessionManagement;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by savita on 2/5/18.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    //    SessionManagement session;
    private static final String TAG = "MyFirebaseMsgService";
    SessionManagement session;

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);
        session = new SessionManagement(this);
        session.setDeviceId(token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getData());

            try {
                JSONObject jsonObject = new JSONObject(remoteMessage.getData().get("message"));
                String message = jsonObject.optString("message");
                String shift_id = jsonObject.optString("shift_id");
                String deepLink = jsonObject.optString("deeplink");
                String label = jsonObject.optString("label");
                String dateStamp = jsonObject.optString("date");
                boolean isShiftClaimed = jsonObject.optBoolean("isClaimed");
                if(label.equalsIgnoreCase("")){
                    label = "Zira";
                }
                sendNotification(message, deepLink, shift_id,label,dateStamp,isShiftClaimed);
            } catch (JSONException e) {
                e.printStackTrace();
            }


    }

    private void sendNotification(String messageBody, String deepLink,
                                  String shift_id,String label,String dateStamp,boolean isShiftClaimed) {
        Intent intent = new Intent(this, DashboardActivity.class);
        intent.putExtra("from", "notification");
        intent.putExtra("shift_id", shift_id);
        intent.putExtra("deeplink",deepLink);
        intent.putExtra("dateStamp",dateStamp);
        intent.putExtra("isClaimed",isShiftClaimed);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        int num = (int) System.currentTimeMillis();
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 100 /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(label)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri).setChannelId("100")
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("100"+num, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(100, notificationBuilder.build());

    }
}
