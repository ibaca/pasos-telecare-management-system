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

import org.inftel.tms.mobile.pasos.PasosHTTPTransmitter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.util.Log;

/**
 * The manifest Receiver is used to detect changes in battery state. When the
 * system broadcasts a "Battery Low" warning we turn off the passive location
 * updates to conserve battery when the app is in the background. When the
 * system broadcasts "Battery OK" to indicate the battery has returned to an
 * okay state, the passive location updates are resumed.
 */
public class PowerStateChangedReceiver extends BroadcastReceiver {
    private static final String TAG = "BatteryReceiver"; // for debug

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(TAG, "STARTED");

        boolean batteryLow = intent.getAction().equals(Intent.ACTION_BATTERY_LOW);

        String batteryLevel = parseBatteryLevel(intent);
        boolean isCharging = isCharging(intent);

        // if battery low, sends an alarm with the level of the battery,
        // location and if the
        // mobile is charging
        if (batteryLow) {
            sendTechnicalAlarm(null, batteryLevel, isCharging);
        }
        //
        // PackageManager pm = context.getPackageManager();
        // ComponentName passiveLocationReceiver =
        // new ComponentName(context, PassiveLocationChangedReceiver.class);
        //
        // // Disable the passive location update receiver when the battery
        // state
        // // is low.
        // // Disabling the Receiver will prevent the app from initiating the
        // // background
        // // downloads of nearby locations.
        // pm.setComponentEnabledSetting(passiveLocationReceiver, batteryLow ?
        // COMPONENT_ENABLED_STATE_DISABLED : COMPONENT_ENABLED_STATE_DEFAULT,
        // DONT_KILL_APP);
    }

    /**
     * Send a technical Alarm using HTTTPTransmitter
     */
    private void sendTechnicalAlarm(String location, String batteryLevel, boolean charging) {
        PasosHTTPTransmitter transmitter = new PasosHTTPTransmitter();
        // try {
        // transmitter.sendTechnicalAlarm(location, batteryLevel, charging);
        // } catch (UnsupportedEncodingException e) {
        // // TODO pantallita o algo para excepciones
        // e.printStackTrace();
        // } catch (URISyntaxException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // } catch (IOException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
    }

    /**
     * Converts the level of the battery in % value
     * 
     * @return the String value of the level
     */
    private String parseBatteryLevel(Intent intent) {
        int rawlevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        int level = -1;
        if (rawlevel >= 0 && scale > 0) {
            level = (rawlevel * 100) / scale;
        }
        Log.d(TAG, "nivel de bateria:" + level);
        return String.valueOf(level);
    }

    /**
     * Obtains if the mobile is charging
     * 
     * @return isCharging
     */
    private boolean isCharging(Intent intent) {
        int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;
        Log.d(TAG, "enchufado??:" + String.valueOf(isCharging));
        return isCharging;

    }
}
