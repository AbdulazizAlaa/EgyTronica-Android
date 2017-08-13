package com.abdulaziz.egytronica.data.remote;

import android.content.Intent;
import android.util.Log;

import com.abdulaziz.egytronica.utils.GlobalEntities;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by abdulaziz on 10/6/16.
 */
public class EgyTronicaFirebaseInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("FirebaseInstanceID", "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        // sendRegistrationToServer(refreshedToken);
        Intent broadcast = new Intent(GlobalEntities.REGISTRATION_ID_UPDATED_EVENT);
        broadcast.putExtra(GlobalEntities.REGISTRATION_ID_TAG, refreshedToken);
        sendBroadcast(broadcast);
    }
}
