
package org.inftel.tms.mobile.services;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;
import org.inftel.tms.mobile.pasos.PasosHTTPTransmitter;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class SendPasosMessageIntentService extends IntentService {
    private static final String TAG = "SendPasosMessageIntentService";

    public SendPasosMessageIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "SendPasosMessageIntentService constructed");
        Bundle bundle = intent.getExtras();
        String message = bundle.getString("message");
        String url = bundle.getString("url");
        String senderNumber = bundle.getString("senderNumber");
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
