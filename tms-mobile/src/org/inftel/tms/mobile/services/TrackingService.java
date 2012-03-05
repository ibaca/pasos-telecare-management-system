
package org.inftel.tms.mobile.services;

import static android.net.ConnectivityManager.TYPE_MOBILE;
import static org.inftel.tms.mobile.TmsConstants.SP_KEY_LAST_TRACKING_LAT;
import static org.inftel.tms.mobile.TmsConstants.SP_KEY_LAST_TRACKING_LNG;
import static org.inftel.tms.mobile.TmsConstants.SP_KEY_LAST_TRACKING_TIME;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;
import org.inftel.tms.mobile.TmsConstants;
import org.inftel.tms.mobile.pasos.PasosHTTPTransmitter;
import org.inftel.tms.mobile.pasos.PasosMessage;
import org.inftel.tms.mobile.pasos.PasosTransmitter;
import org.inftel.tms.mobile.receivers.ConnectivityChangedReceiver;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;

/**
 * Service that requests a list of nearby locations from the underlying web
 * service. TODO Update the URL and XML parsing to correspond with your
 * underlying web service.
 */
public class TrackingService extends IntentService {

    protected static String TAG = "TrackingService";

    protected ContentResolver contentResolver;
    protected SharedPreferences prefs;
    protected Editor prefsEditor;
    protected ConnectivityManager cm;
    protected boolean lowBattery = false;
    protected boolean mobileData = false;

    public TrackingService() {
        super(TAG);
        setIntentRedeliveryMode();
    }

    /**
     * Set the Intent Redelivery mode to true to ensure the Service starts
     * "Sticky". Defaults to "true" on legacy devices. And force Intent
     * redelivery on Eclaire+ devices, where this defaults to false.
     */
    protected void setIntentRedeliveryMode() {
        // legacy default true
        if (TmsConstants.SUPPORTS_ECLAIR) {
            setIntentRedelivery(true);
        }
    }

    /**
     * Returns battery status. True if less than 10% remaining.
     * 
     * @param battery Battery Intent
     * @return Battery is low
     */
    protected boolean getIsLowBattery(Intent battery) {
        float pctLevel = (float) battery.getIntExtra(BatteryManager.EXTRA_LEVEL, 1) /
                battery.getIntExtra(BatteryManager.EXTRA_SCALE, 1);
        return pctLevel < 0.15;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        contentResolver = getContentResolver();
        prefs = getSharedPreferences(TmsConstants.SHARED_PREFERENCE_FILE, Context.MODE_PRIVATE);
        prefsEditor = prefs.edit();
    }

    /**
     * {@inheritDoc} Checks the battery and connectivity state before removing
     * stale venues and initiating a server poll for new venues around the
     * specified location within the given radius.
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        // Check if we're running in the foreground, if not, check if
        // we have permission to do background updates.
        boolean backgroundAllowed = cm.getBackgroundDataSetting();

        if (!backgroundAllowed) {
            Log.d(TAG, "Skipping tracking for background process not allowed");
            return;
        }

        // Extract the location and radius around which to conduct our search.
        Location location = new Location(TmsConstants.CONSTRUCTED_LOCATION_PROVIDER);
        int radius = TmsConstants.DEFAULT_RADIUS;

        Bundle extras = intent.getExtras();
        if (intent.hasExtra(TmsConstants.EXTRA_KEY_LOCATION)) {
            location = (Location) (extras.get(TmsConstants.EXTRA_KEY_LOCATION));
            radius = extras.getInt(TmsConstants.EXTRA_KEY_RADIUS, TmsConstants.DEFAULT_RADIUS);
        }

        Log.d(TAG, "Tracking location " + location.getLatitude() + ", " + location.getLongitude());

        // Check if we're in a low battery situation.
        IntentFilter batIntentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent battery = registerReceiver(null, batIntentFilter);
        lowBattery = getIsLowBattery(battery);

        // Check if we're connected to a data network, and if so - if it's a
        // mobile network.
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        mobileData = activeNetwork != null && activeNetwork.getType() == TYPE_MOBILE;

        /*
         * If we're not connected, enable the connectivity receiver and disable
         * the location receiver. There's no point trying to poll the server for
         * updates if we're not connected, and the connectivity receiver will
         * turn the location-based updates back on once we have a connection.
         */
        if (!isConnected) {
            ConnectivityChangedReceiver.notConnected(this, getPackageManager());
        } else {
            /*
             * If we are connected check to see if this is a forced update
             * (typically triggered when the location has changed).
             */
            boolean doTrack = intent.getBooleanExtra(TmsConstants.EXTRA_KEY_FORCETRACK, false);

            // If it's not a forced update (for example from the Activity being
            // restarted) then check to see if we've moved far enough, or
            // there's been a long enough delay since the last update and if so,
            // enforce a new update.
            if (!doTrack) {
                // Retrieve the last update time and place.
                long lastTime = prefs.getLong(SP_KEY_LAST_TRACKING_TIME, Long.MIN_VALUE);
                float lastLat = prefs.getFloat(SP_KEY_LAST_TRACKING_LAT, Float.MIN_VALUE);
                float lastLng = prefs.getFloat(SP_KEY_LAST_TRACKING_LNG, Float.MIN_VALUE);
                Location lastLocation = new Location(TmsConstants.CONSTRUCTED_LOCATION_PROVIDER);
                lastLocation.setLatitude(lastLat);
                lastLocation.setLongitude(lastLng);

                // If update time and distance bounds have been passed, do an
                // update.
                if ((lastTime < System.currentTimeMillis() - TmsConstants.MAX_TIME) ||
                        (lastLocation.distanceTo(location) > TmsConstants.MAX_DISTANCE))
                    doTrack = true;
            }

            if (doTrack) {
                sendLocation(location, radius);
            }
            else {
                Log.d(TAG, "Tracking is fresh: Not sending location");
            }

        }
        Log.d(TAG, "Tracking Service Complete");
    }

    /**
     * Polls the underlying service to return a list of places within the
     * specified radius of the specified Location.
     * 
     * @param location Location
     * @param radius Radius
     */
    protected void sendLocation(Location location, int radius) {
        Log.d(TAG, "Sending location");
        long currentTime = System.currentTimeMillis();
        float latitude = Double.valueOf(location.getLatitude()).floatValue();
        float longitude = Double.valueOf(location.getLongitude()).floatValue();

        PasosTransmitter transmitter = new PasosHTTPTransmitter();
        try {
            // TODO debería enviarse un mensaje tipo tracking

            PasosMessage message = PasosMessage.userAlarm(latitude + "", longitude + "");
            transmitter.sendPasosMessage(message);

            // Actualizar utlimos tracking enviado
            prefsEditor.putLong(SP_KEY_LAST_TRACKING_TIME, currentTime);
            prefsEditor.putFloat(SP_KEY_LAST_TRACKING_LAT, latitude);
            prefsEditor.putFloat(SP_KEY_LAST_TRACKING_LNG, longitude);
            prefsEditor.commit();
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, e.getMessage());
        } catch (ClientProtocolException e) {
            Log.e(TAG, e.getMessage());
        } catch (URISyntaxException e) {
            Log.e(TAG, e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
    }

}
