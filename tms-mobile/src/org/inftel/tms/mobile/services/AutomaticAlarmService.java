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

/* WARNING!! uncomment the lines to NOT use the sensors simulator */

public class AutomaticAlarmService extends Service {
    private static final String TAG = "AutomaticAlarmService";
    // SensorManager sm;
    // SensorManagerSimulator sm;
    // Sensor mTemperature;
    private float temperature;

    @Override
    public void onCreate() {
        super.onCreate();
        // sm = SensorManagerSimulator.getSystemService(this, SENSOR_SERVICE);
        // sm.connectSimulator();

        // sm = (SensorManager) getSystemService(SENSOR_SERVICE);
    }

    @Override
    public void onStart(final Intent intent, final int startId) {
        super.onStart(intent, startId);
        // sm.registerListener(this,
        // sm.getDefaultSensor(Sensor.TYPE_TEMPERATURE),
        // SensorManager.SENSOR_DELAY_GAME);

        int batteryLevel = parseBatteryLevel();
        boolean isCharging = isCharging();

        Log.i(TAG, "ENVIANDO: nivel de bateria " + batteryLevel + "% , cargando = " + isCharging
                + " , temperatura = " + temperature + "ºC");
        sendAlarmMessage(batteryLevel, (int) temperature, isCharging);
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

    /**
     * Sends the Alarm message to the server using a own Builder.
     */
    protected void sendAlarmMessage(int batteryLevel, int temperature, boolean isCharging) {

        Location location = PlatformSpecificImplementationFactory.getLastLocationFinder(
                getApplicationContext()).getLastBestLocation(0, 0);

        /* Constructs the message */
        Builder messageBuilder = buildTechnicalAlarm();

        try {
            messageBuilder.location(location.getLatitude(), location.getLongitude());
        } catch (NullPointerException e) { // if there is no last position
                                           // probably we get a null pointer
        } finally {
            if (temperature <= 0) {
                messageBuilder
                        .cause(
                        "LowTemperature: La temperatura detectada en el dispositivo ha bajado de 0º");
            } else if (temperature > 30) {
                messageBuilder
                        .cause(
                        "HighTemperature: La temeratura detectada en el dispositivo ha superado 30º");
            } else {
                messageBuilder.cause(
                        "AutomaticSending:OK");
            }
            messageBuilder.charging(isCharging);
            messageBuilder.battery(batteryLevel);
            messageBuilder.temperature(temperature);
            PasosMessage message = messageBuilder.build();

            /* Sends the message */
            Intent sendService = new Intent(this, SendPasosMessageIntentService.class);
            sendService.putExtra(TmsConstants.EXTRA_KEY_MESSAGE_CONTENT, message.toString());
            startService(sendService);
        }
    }

    // @Override
    // public void onAccuracyChanged(Sensor arg0, int arg1) {
    // // TODO Do something
    //
    // }
    //
    // @Override
    // public void onSensorChanged(SensorEvent event) {
    // temperature = event.values[0];
    // sm.unregisterListener(this);
    // }
}
