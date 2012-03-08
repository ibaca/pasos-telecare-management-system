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

import org.openintents.sensorsimulator.hardware.Sensor;
import org.openintents.sensorsimulator.hardware.SensorEvent;
import org.openintents.sensorsimulator.hardware.SensorEventListener;
import org.openintents.sensorsimulator.hardware.SensorManagerSimulator;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.SensorManager;
import android.os.BatteryManager;
import android.os.IBinder;
import android.util.Log;

/* WARNING!! uncomment the lines to NOT use the sensors simulator */

public class AutomaticAlarmService extends Service implements SensorEventListener {
    private static final String TAG = "AutomaticAlarmService";
    // SensorManager sm;
    SensorManagerSimulator sm;
    Sensor mTemperature;
    private float temperature;

    @Override
    public void onCreate() {
        super.onCreate();
        sm = SensorManagerSimulator.getSystemService(this, SENSOR_SERVICE);
        sm.connectSimulator();

        // sm = (SensorManager) getSystemService(SENSOR_SERVICE);
    }

    @Override
    public void onStart(final Intent intent, final int startId) {
        super.onStart(intent, startId);
        sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_TEMPERATURE),
                SensorManager.SENSOR_DELAY_GAME);

        Log.i(TAG, "RULANDOOOO  " + parseBatteryLevel() + " " + isCharging() + " " + temperature);

        // PasosHTTPTransmitter transmitter = new PasosHTTPTransmitter();
        // transmitter.sendPasosMessage(PasosMessage.technicalAlarm(parseBatteryLevel(),
        // latitude, longitude,isCharging()))
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
    private String parseBatteryLevel() {
        Context context = getApplicationContext();
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);

        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        return String.valueOf(((int) level * 100 / (float) scale));
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

    /**
     * Obtains battery temperature
     * 
     * @return the String value of the temperature
     */
    private String getBatteryTemperature() {
        Context context = getApplicationContext();
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);

        int batteryLevel = batteryStatus.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0);

        return String.valueOf(batteryLevel);
    }

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {
        // TODO Do something

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        temperature = event.values[0];
        sm.unregisterListener(this);
    }
}
