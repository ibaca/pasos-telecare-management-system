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

import org.inftel.tms.mobile.receivers.AutomaticAlarmReceiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/*
 * AlarmManager's configuration. Automatic sending message every MSEC_TO_REPEAT
 */
public class AutomaticAlarmSendingService extends Service {
    private static final String TAG = "AutomaticAlarmSendingService";
    private static final int MSEC_TO_REPEAT = 15000;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onStart(final Intent intent, final int startId) {
        super.onStart(intent, startId);

        /*
         * Creation of the pending intent (targeting AutomaticAlarmReceiver)
         * that will be passed to the AlarmManager
         */
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this.getApplicationContext(), 234324243, new Intent(this,
                        AutomaticAlarmReceiver.class), 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
                + MSEC_TO_REPEAT, pendingIntent);

        // timer.scheduleAtFixedRate(new TimerTask() {
        // SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        //
        // @Override
        // public void run() {
        //
        // Context context = getApplicationContext();
        // IntentFilter ifilter = new
        // IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        // Intent batteryStatus = context.registerReceiver(null, ifilter);
        //
        // int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL,
        // -1);
        // int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE,
        // -1);
        //
        // float batteryPct = level / (float) scale;
        //
        // Log.i(TAG, "RULANDOOOO  " + batteryPct);
        //
        //
        //
        // // sendPasosMessage(PasosMessage.technicalAlarm(batteryLevel,
        // // null, null,false));
        // }
        // }, 0, MSEC_TO_REPEAT);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
