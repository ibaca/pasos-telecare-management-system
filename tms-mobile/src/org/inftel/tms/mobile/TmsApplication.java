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

import org.inftel.tms.mobile.util.PlatformSpecificImplementationFactory;
import org.inftel.tms.mobile.util.base.IStrictMode;

import android.app.Application;

public class TmsApplication extends Application {

	// TODO Insert your Google Places API into MY_API_KEY in PlacesConstants.java
	// TODO Insert your Backup Manager API into res/values/strings.xml : backup_manager_key

	@Override
	public final void onCreate() {
		super.onCreate();

		if (TmsConstants.DEVELOPER_MODE) {
			IStrictMode strictMode = PlatformSpecificImplementationFactory.getStrictMode();
			if (strictMode != null)
				strictMode.enableStrictMode();
		}
	}
}