package com.zing.notification;

import android.app.Service;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.zing.util.SessionManagement;

/**
 * Created by savita on 2/5/18.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    SessionManagement session;
    private static final String TAG = "MyFirebaseIIDService";

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.e(TAG, refreshedToken);

        session = new SessionManagement(this);
        session.setDeviceId(refreshedToken);
    }
}
