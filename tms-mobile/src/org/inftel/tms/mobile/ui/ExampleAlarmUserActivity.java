
package org.inftel.tms.mobile.ui;

import static java.lang.System.currentTimeMillis;

import org.inftel.tms.mobile.R;
import org.inftel.tms.mobile.TmsConstants;
import org.inftel.tms.mobile.pasos.PasosMessage;
import org.inftel.tms.mobile.services.SendPasosMessageIntentService;
import org.inftel.tms.mobile.util.PlatformSpecificImplementationFactory;
import org.inftel.tms.mobile.util.base.ILastLocationFinder;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class ExampleAlarmUserActivity extends Activity {

    private static final String TAG = "ExampleAlarmUserActivity";
    private ILastLocationFinder lastLocationFinder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarmuser);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Log.i(TAG, prefs.getString(TmsConstants.KEY_IP_PREFERENCE, ""));
        Log.i(TAG, prefs.getString(TmsConstants.KEY_PORT_PREFERENCE, ""));

        // Configura location finder
        lastLocationFinder = PlatformSpecificImplementationFactory.getLastLocationFinder(this);

        // Configura el listener para el boton 'enviar alerta'
        findViewById(R.id.button1).setOnClickListener(sendAlarmListener);
    }

    // Delega en envio al serviceIntent SendPasosMessage
    OnClickListener sendAlarmListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            Location location = lastLocationFinder.getLastBestLocation(
                    TmsConstants.MAX_DISTANCE, TmsConstants.MAX_TIME);
            PasosMessage message = PasosMessage.buildUserAlarm().location(
                    location.getLatitude(), location.getLongitude()).build();

            Intent sendService = new Intent(view.getContext(), SendPasosMessageIntentService.class);
            sendService.putExtra(TmsConstants.EXTRA_KEY_MESSAGE_CONTENT, message.toString());
            startService(sendService);
        }
    };

    protected void getLastLocation(int maxDistance, int maxTime) {
        final AsyncTask<Void, Void, Location> findLationTask = new AsyncTask<Void, Void, Location>() {
            @Override
            protected Location doInBackground(Void... params) {
                Location location = lastLocationFinder.getLastBestLocation(
                        TmsConstants.MAX_DISTANCE, currentTimeMillis() - TmsConstants.MAX_TIME);
                return location;
            }

            protected void onPostExecute(Location result) {
                // Aqui se puede actualizar el IU
            }
        };
        findLationTask.execute();
    }
}
