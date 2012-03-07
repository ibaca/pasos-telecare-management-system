
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

/**
 * Realiza el envio de mensajes de alerta al servidor. El mensaje se añade en el
 * extra {@link TmsConstants#EXTRA_KEY_MESSAGE_CONTENT}.
 * 
 * @author ibaca
 */
public class SendPasosMessageIntentService extends IntentService {

    private static final String TAG = "SendPasosMessageIntentService";

    protected String url;
    protected String senderNumber;

    public SendPasosMessageIntentService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "SendPasosMessageIntentService created");

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String host = prefs.getString(TmsConstants.KEY_IP_PREFERENCE, "");
        String port = prefs.getString(TmsConstants.KEY_PORT_PREFERENCE, "");
        String path = prefs.getString(TmsConstants.KEY_PATH_PREFERENCE, "");
        url = "http://" + host + ":" + port + "/" + path;
        senderNumber = prefs.getString(TmsConstants.KEY_PHONE_NUMBER_PREFERENCE, "");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle bundle = intent.getExtras();
        String message = bundle.getString(TmsConstants.EXTRA_KEY_MESSAGE_CONTENT);
        if (message == null) {
            Log.d(TAG, "No se ha incluido el extra con el mensaje, no se realizará el evío");
            return;
        }

        Log.d(TAG, "Enviando mensaje '" + message + "' al la dirección '" + url + "'");
        PasosHTTPTransmitter transmitter = new PasosHTTPTransmitter(url, senderNumber);
        try {
            transmitter.sendPasosMessage(message);
            Log.d(TAG, "Message de alerta enviado al servidor");
        } catch (ClientProtocolException e) {
            Log.w(TAG, e.getMessage(), e);
        } catch (URISyntaxException e) {
            Log.w(TAG, e.getMessage(), e);
        } catch (IOException e) {
            Log.w(TAG, e.getMessage(), e);
        }
    }

}
