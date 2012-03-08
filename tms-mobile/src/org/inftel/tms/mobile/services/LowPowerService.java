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

package org.inftel.tms.mobile.services;

import static org.inftel.tms.mobile.pasos.PasosMessage.buildTechnicalAlarm;

import org.inftel.tms.mobile.TmsConstants;
import org.inftel.tms.mobile.pasos.PasosMessage;
import org.inftel.tms.mobile.pasos.PasosMessage.Builder;
import org.inftel.tms.mobile.util.PlatformSpecificImplementationFactory;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.BatteryManager;
import android.os.IBinder;
import android.util.Log;

public class LowPowerService extends Service {
    private static final String TAG = "LowPowerService";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onStart(final Intent intent, final int startId) {
        super.onStart(intent, startId);
        int batteryLevel = parseBatteryLevel();
        boolean isCharging = isCharging();

        Log.i(TAG, "RULANDOOOO  " + batteryLevel + " " + isCharging);

        sendAlarmMessage(batteryLevel, isCharging);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * Converts the level of the battery in % value
     * 
     * @return the String value of the level
     */
    private int parseBatteryLevel() {
        Context context = getApplicationContext();
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);

        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        return (int) (level * 100 / (float) scale);
    }

    /**
     * Obtains if the mobile is charging
     * 
     * @return isCharging
     */

    private boolean isCharging() {
        Context context = getApplicationContext();
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);

        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;
        return isCharging;

    }

    /* Sends an Alarm */
    protected void sendAlarmMessage(int batteryLevel, boolean isCharging) {

        Location location = PlatformSpecificImplementationFactory.getLastLocationFinder(
                getApplicationContext()).getLastBestLocation(0, 0);

        /* Constructs the message */
        Builder messageBuilder = buildTechnicalAlarm();

        double latitude, longitude;
        try {
            latitude = location.getLatitude();
            longitude = location.getLongitude();

            if (isCharging) {
                messageBuilder.location(latitude, longitude)
                        .cause("LowBattery:Batería baja pero cargando");
            } else {
                messageBuilder.location(latitude, longitude)
                        .cause("LowBattery:Batería baja y SIN CARGAR");
            }
        } catch (NullPointerException e) {
        } finally {
            messageBuilder.charging(isCharging);
            messageBuilder.battery(batteryLevel);
            PasosMessage message = messageBuilder.build();

            /* Sends the message */
            Intent sendService = new Intent(this, SendPasosMessageIntentService.class);
            sendService.putExtra(TmsConstants.EXTRA_KEY_MESSAGE_CONTENT, message.toString());
            startService(sendService);
        }
    }
}
