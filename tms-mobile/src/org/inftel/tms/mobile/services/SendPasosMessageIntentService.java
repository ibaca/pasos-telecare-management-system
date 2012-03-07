
package org.inftel.tms.mobile.services;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;
import org.inftel.tms.mobile.TmsConstants;
import org.inftel.tms.mobile.pasos.PasosHTTPTransmitter;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

public class SendPasosMessageIntentService extends IntentService {
    private static final String TAG = "SendPasosMessageIntentService";

    public SendPasosMessageIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "SendPasosMessageIntentService constructed");
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(this);
        String ip = prefs.getString(TmsConstants.KEY_IP_PREFERENCE, "");
        String port = prefs.getString(TmsConstants.KEY_PORT_PREFERENCE, "");
        String path = prefs.getString(TmsConstants.KEY_PATH_PREFERENCE, "");
        String url = "http://" + ip + ":" + port + "/" + path;
        String senderNumber = prefs.getString(TmsConstants.KEY_PHONE_NUMBER_PREFERENCE, "");

        Bundle bundle = intent.getExtras();
        String message = bundle.getString("message");

        PasosHTTPTransmitter transmitter = new PasosHTTPTransmitter(url, senderNumber);
        try {
            transmitter.sendPasosMessage(message);
            Log.i(TAG, "Message sent!");
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
