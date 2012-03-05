/*
 * Copyright 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.inftel.tms.mobile.receivers;

import static android.content.Context.MODE_PRIVATE;
import static org.inftel.tms.mobile.TmsConstants.DEFAULT_RADIUS;
import static org.inftel.tms.mobile.TmsConstants.SHARED_PREFERENCE_FILE;
import static org.inftel.tms.mobile.TmsConstants.SP_KEY_LAST_FENCES_CHECK_LAT;
import static org.inftel.tms.mobile.TmsConstants.SP_KEY_LAST_FENCES_CHECK_LNG;
import static org.inftel.tms.mobile.TmsConstants.SP_KEY_LAST_FENCES_CHECK_TIME;

import org.inftel.tms.mobile.TmsConstants;
import org.inftel.tms.mobile.services.TrackingService;
import org.inftel.tms.mobile.util.LegacyLastLocationFinder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

/**
 * This Receiver class is used to listen for Broadcast Intents that announce
 * that a location change has occurred while this application isn't visible.
 * Where possible, this is triggered by a Passive Location listener.
 */
public class PassiveLocationChangedReceiver extends BroadcastReceiver {

    protected static String TAG = "PassiveLocationChangedReceiver";

    /**
     * When a new location is received, extract it from the Intent and use it to
     * start the Service used to update the list of nearby places. This is the
     * Passive receiver, used to receive Location updates from third party apps
     * when the Activity is not visible.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "PassivlyLocationChanged called.");
        String key = LocationManager.KEY_LOCATION_CHANGED;
        Location location = null;

        if (intent.hasExtra(key)) {
            Log.d(TAG, "Location changed from a passive provider.");
            // ...so we can extract the location directly.
            location = (Location) intent.getExtras().get(key);
        } else {
            Log.d(TAG, "Location changed from a recurring alarm.");
            // This update came from a recurring alarm (legacy location update).
            // We need to determine if there has been a more recent Location
            // received than the last location we used.

            // Get the best last location detected from the providers.
            LegacyLastLocationFinder lastLocationFinder = new LegacyLastLocationFinder(context);
            location = lastLocationFinder.getLastBestLocation(TmsConstants.MAX_DISTANCE,
                    System.currentTimeMillis() - TmsConstants.MAX_TIME);
            SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFERENCE_FILE,
                    MODE_PRIVATE);

            // Get the last location we used to check fences.
            long lastTime = prefs.getLong(SP_KEY_LAST_FENCES_CHECK_TIME, Long.MIN_VALUE);
            float lastLat = prefs.getFloat(SP_KEY_LAST_FENCES_CHECK_LAT, Float.MIN_VALUE);
            float lastLng = prefs.getFloat(SP_KEY_LAST_FENCES_CHECK_LNG, Float.MIN_VALUE);
            Location lastLocation = new Location(TmsConstants.CONSTRUCTED_LOCATION_PROVIDER);
            lastLocation.setLatitude(lastLat);
            lastLocation.setLongitude(lastLng);

            /*
             * Check if the last location detected from the providers is either
             * too soon, or too close to the last value we used. If it is within
             * those thresholds we set the location to null to prevent the
             * update Service being run unnecessarily (and spending battery on
             * data transfers). El objetivo de esta comprobacion es evitar que
             * la misma localizacion se procese varia veces, ya que
             * LegacyUpdateLocationRequester utiliza una alarma recurrente que
             * puede lanzarse sin cambios en la localizacion.
             */
            if ((lastTime > System.currentTimeMillis() - TmsConstants.PASSIVE_LOCATION_MAX_TIME)
                    || (lastLocation.distanceTo(location) < TmsConstants.PASSIVE_LOCATION_MAX_DISTANCE)) {
                location = null;
            }
        }

        /*
         * Start the Service used to find nearby points of interest based on the
         * last detected location.
         */
        if (location != null) {
            Log.d(TAG, "Passively tracking location");
            Intent checkServiceIntent = new Intent(context, TrackingService.class);
            checkServiceIntent.putExtra(TmsConstants.EXTRA_KEY_LOCATION, location);
            checkServiceIntent.putExtra(TmsConstants.EXTRA_KEY_RADIUS, DEFAULT_RADIUS);
            checkServiceIntent.putExtra(TmsConstants.EXTRA_KEY_FORCETRACK, false);
            context.startService(checkServiceIntent);
        }
    }
}
