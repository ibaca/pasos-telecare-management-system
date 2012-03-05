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

package org.inftel.tms.mobile;

import static org.inftel.tms.mobile.util.PlatformSpecificImplementationFactory.getLocationUpdateRequester;

import org.inftel.tms.mobile.receivers.LocationChangedReceiver;
import org.inftel.tms.mobile.receivers.PassiveLocationChangedReceiver;
import org.inftel.tms.mobile.util.PlatformSpecificImplementationFactory;
import org.inftel.tms.mobile.util.base.IStrictMode;
import org.inftel.tms.mobile.util.base.LocationUpdateRequester;

import android.app.Application;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class TmsApplication extends Application {

    private static final String TAG = "TmsApplication";

    private LocationManager locationManager;
    private LocationUpdateRequester locationUpdateRequester;
    private PendingIntent locationListenerPendingIntent;
    private PendingIntent locationListenerPassivePendingIntent;

    // TODO Insert your Google Places API into MY_API_KEY in
    // TmsConstants.java
    // TODO Insert your Backup Manager API into res/values/strings.xml :
    // backup_manager_key

    @Override
    public final void onCreate() {
        super.onCreate();
        Log.d(TAG, "OnCreate application.");

        if (TmsConstants.DEVELOPER_MODE) {
            IStrictMode strictMode = PlatformSpecificImplementationFactory.getStrictMode();
            if (strictMode != null) {
                // strictMode.enableStrictMode();
            }
        }

        // TODO Se activan los filtros! esto supuestamente se activa solo on
        // boot
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationUpdateRequester = getLocationUpdateRequester(locationManager);

        // Setup the location update Pending Intents
        Intent activeIntent = new Intent(this, LocationChangedReceiver.class);
        locationListenerPendingIntent = PendingIntent.getBroadcast(
                this, 0, activeIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Intent passiveIntent = new Intent(this, PassiveLocationChangedReceiver.class);
        locationListenerPassivePendingIntent = PendingIntent.getBroadcast(
                this, 0, passiveIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Activa las suscripciones a cambios de posicion
        requestLocationUpdates();
    }

    /**
     * Start listening for location updates.
     */
    protected void requestLocationUpdates() {
        Criteria criteria = new Criteria(); // TODO define best criteria

        // Normal updates while activity is visible.
        locationUpdateRequester.requestLocationUpdates(TmsConstants.MAX_TIME,
                TmsConstants.MAX_DISTANCE, criteria, locationListenerPendingIntent);

        // Passive location updates from 3rd party apps when the Activity isn't
        // visible.
        locationUpdateRequester.requestPassiveLocationUpdates(TmsConstants.PASSIVE_MAX_TIME,
                TmsConstants.PASSIVE_MAX_DISTANCE, locationListenerPassivePendingIntent);

        // Register a receiver that listens for when the provider I'm using has
        // been disabled.
        IntentFilter intentFilter = new IntentFilter(
                TmsConstants.ACTIVE_LOCATION_UPDATE_PROVIDER_DISABLED);
        registerReceiver(locProviderDisabledReceiver, intentFilter);

        // Register a receiver that listens for when a better provider than I'm
        // using becomes available.
        String bestProvider = locationManager.getBestProvider(criteria, false);
        String bestAvailableProvider = locationManager.getBestProvider(criteria, true);

        // Si el mejor proveedor no esta activo, se suscribe listener
        if (bestProvider != null && !bestProvider.equals(bestAvailableProvider)) {
            locationManager.requestLocationUpdates(bestProvider, 0, 0,
                    bestInactiveLocationProviderListener, getMainLooper());
        }
    }

    /**
     * Stop listening for location updates
     */
    protected void disableLocationUpdates() {
        unregisterReceiver(locProviderDisabledReceiver);
        locationManager.removeUpdates(locationListenerPendingIntent);
        locationManager.removeUpdates(bestInactiveLocationProviderListener);
        if (TmsConstants.DISABLE_PASSIVE_LOCATION_WHEN_USER_EXIT) {
            locationManager.removeUpdates(locationListenerPassivePendingIntent);
        }
    }

    /**
     * If the Location Provider we're using to receive location updates is
     * disabled while the app is running, this Receiver will be notified,
     * allowing us to re-register our Location Receivers using the best
     * available Location Provider is still available.
     */
    protected BroadcastReceiver locProviderDisabledReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean providerDisabled = !intent.getBooleanExtra(
                    LocationManager.KEY_PROVIDER_ENABLED, false);
            // Re-register the location listeners using the best available
            // Location Provider.
            if (providerDisabled) {
                requestLocationUpdates();
            }
        }
    };

    /**
     * If the best Location Provider (usually GPS) is not available when we
     * request location updates, this listener will be notified if / when it
     * becomes available. It calls requestLocationUpdates to re-register the
     * location listeners using the better Location Provider.
     */
    protected LocationListener bestInactiveLocationProviderListener = new LocationListener() {
        public void onLocationChanged(Location l) {
        }

        public void onProviderDisabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        public void onProviderEnabled(String provider) {
            // Re-register the location listeners using the better Location
            // Provider.
            requestLocationUpdates();
        }
    };
}
