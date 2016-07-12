package com.example.hungpd.gcmsample;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

/**
 * Created by hung.pd on 7/12/2016.
 */
public class RegistrationIntentService extends IntentService {


    private static final String TAG = "dinhhung.ph";
    private static final String[] TOPICS = {"global"};
    private Context mContext;

    public RegistrationIntentService() {
        super("RegistrationIntentService");
        mContext = this;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            Log.i(TAG, "GCM Registration Token");
            InstanceID instanceID = InstanceID.getInstance(mContext);
            String token = instanceID.getToken(getString(R.string.sender_id), GoogleCloudMessaging.INSTANCE_ID_SCOPE);
            subscribeTopics(token);
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
            sharedPreferences.edit().putBoolean(GCMPreferences.SENT_TOKEN_TO_SERVER, true).apply();
            Log.i(TAG, "GCM Registration Token: " + token);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Intent registrationComplete = new Intent(GCMPreferences.REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    /**
     * Subscribe to any GCM topics of interest, as defined by the TOPICS constant.
     *
     * @param token GCM token
     * @throws IOException if unable to reach the GCM PubSub service
     */
    // [START subscribe_topics]
    private void subscribeTopics(String token) throws IOException {
        GcmPubSub pubSub = GcmPubSub.getInstance(this);
        for (String topic : TOPICS) {
            pubSub.subscribe(token, "/topics/" + topic, null);
        }
    }
    // [END subscribe_topics]
}
