package com.abdulaziz.egytronica.data.remote;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.abdulaziz.egytronica.R;
import com.abdulaziz.egytronica.data.local.PreferencesHelper;
import com.abdulaziz.egytronica.ui.home.HomeActivity;
import com.abdulaziz.egytronica.utils.GlobalEntities;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by abdulaziz on 10/6/16.
 */
public class EgyTronicaFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("FirebaseMessaging", "create service");
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String message = "";
        String title = "EgyTronica";
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d("FirebaseMessaging", "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            message = remoteMessage.getData().get(GlobalEntities.NOTIFICATION_TEXT_TAG);
            title = remoteMessage.getData().get(GlobalEntities.NOTIFICATION_TITLE_TAG);
            Log.d("FirebaseMessaging", "Message body: " + message);
            Log.d("FirebaseMessaging", "Message title: " + title);
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
//            message = remoteMessage.getNotification().getBody();
            Log.d("FirebaseMessaging", "Message Notification Body: " + message);
        }

//        sendBroadcast(new Intent(GlobalEntities.BROADCAST_NOTIFICATION_HANDLER_TAG));
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        sendNotification(title, message);

    }

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private void sendNotification(String title, String message) {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.putExtra(GlobalEntities.NOTIFICATION_TYPE_TAG, notificationType);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.img_logo_full)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

}
