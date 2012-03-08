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

import java.util.Calendar;

import org.inftel.tms.mobile.receivers.AutomaticAlarmReceiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/*
 * AlarmManager's configuration. Automatic sending message every MSEC_TO_REPEAT
 */
public class AlarmManagerConfigurationService extends Service {
    private static final String TAG = "AlarmManagerConfigurationService";
    private static final int MSEC_TO_REPEAT = 15000; // Interval repeat time

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onStart(final Intent intent, final int startId) {
        super.onStart(intent, startId);

        // Get a Calendar object with current time
        Calendar cal = Calendar.getInstance();
        // Add 5 minutes to the calendar object.
        // This time will be passed to Alarm manager to set the first start.
        cal.add(Calendar.MINUTE, 5);

        /*
         * Creation of the pending intent (targeting AutomaticAlarmReceiver)
         * that will be passed to the AlarmManager
         */
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getApplicationContext(), 234324243, new Intent(this,
                        AutomaticAlarmReceiver.class), 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                cal.getTimeInMillis(),
                MSEC_TO_REPEAT, pendingIntent);

        // /* Lista de sensores */
        // List<Sensor> mList = sm.getSensorList(Sensor.TYPE_ALL);
        // String sSensList = new String("");
        // Sensor tmp;
        // for (int i = 0; i < mList.size(); i++) {
        // tmp = mList.get(i);
        // sSensList = " " + sSensList + tmp.getName(); // Add the sensor name
        // // to the string of
        // // sensors available
        // }
        // /* END */
        //
        // Log.i(TAG, "Sensores: " + sSensList);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
