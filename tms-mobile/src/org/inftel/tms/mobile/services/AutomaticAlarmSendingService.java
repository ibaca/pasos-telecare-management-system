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

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class AutomaticAlarmSendingService extends Service {
    private static final String TAG = "AutomaticAlarmSendingService";
    private static final int MSEC_TO_REPEAT = 15000;
    private Timer timer;

    @Override
    public void onCreate() {
        super.onCreate();
        timer = new Timer();
    }

    @Override
    public void onStart(final Intent intent, final int startId) {
        super.onStart(intent, startId);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Log.i(TAG, "RULANDOOOO");
            }
        }, 0, MSEC_TO_REPEAT);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
