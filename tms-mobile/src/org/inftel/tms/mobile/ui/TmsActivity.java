package org.inftel.tms.mobile.ui;

import static com.beoui.geocell.GeocellManager.generateGeoCell;
import static java.math.RoundingMode.CEILING;
import static java.text.DateFormat.SHORT;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.inftel.tms.mobile.R;
import org.inftel.tms.mobile.TmsConstants;
import org.inftel.tms.mobile.util.PlatformSpecificImplementationFactory;
import org.inftel.tms.mobile.util.base.SharedPreferenceSaver;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.beoui.geocell.model.Point;

public class TmsActivity extends Activity {

	static final int PICK_CONTACT_REQUEST = 0;

	protected PendingIntent singleUpatePI;
	protected LocationManager locationManager;
	protected LocationListener locationListener;
	protected Criteria criteria;
	protected DateFormat dateFormat = java.text.DateFormat.getDateInstance(SHORT, Locale.FRANCE);

	private TextView text;
	private ScrollView scroll;

	/* Preferences */
	private SharedPreferences prefs;
	private Editor prefsEditor;
	private SharedPreferenceSaver preferenceSaver;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// Get a reference to the Shared Preferences and a Shared Preference Editor.
		prefs = getSharedPreferences(TmsConstants.SHARED_PREFERENCE_FILE, Context.MODE_PRIVATE);
		prefsEditor = prefs.edit();
		// Instantiate a SharedPreferenceSaver class based on the available platform version.
		preferenceSaver = PlatformSpecificImplementationFactory.getSharedPreferenceSaver(this);

		// Save that we've been run once.
		prefsEditor.putBoolean(TmsConstants.SP_KEY_RUN_ONCE, true);
		preferenceSaver.savePreferences(prefsEditor, false);

		// por si queremos poner algo!
		scroll = (ScrollView) findViewById(R.id.scroll);
		text = (TextView) findViewById(R.id.text);
		append(""); // salto line mensaje hello!

		// Hook up button presses to the appropriate event handler.
		((Button) findViewById(R.id.dialButton)).setOnClickListener(mDialListener);
		((Button) findViewById(R.id.pickButton)).setOnClickListener(mPickListener);
		((Button) findViewById(R.id.sendButton)).setOnClickListener(mSendListener);
		((Button) findViewById(R.id.batteryButton)).setOnClickListener(mBatteryListener);
		((Button) findViewById(R.id.locationButton)).setOnClickListener(mLocationListener);
		((Button) findViewById(R.id.formButton)).setOnClickListener(mPreferencesForm);
		((Button) findViewById(R.id.fencesButton)).setOnClickListener(mFencesActivity);
	    ((Button) findViewById(R.id.buttonUserAlarm)).setOnClickListener(mUserAlarm);

	}

	@Override
	protected void onResume() {
		super.onResume();
	};

	private void pickContact() {
		// Create an intent to "pick" a contact, as defined by the content provider URI
		Intent intent = new Intent(Intent.ACTION_PICK, Contacts.CONTENT_URI);
		startActivityForResult(intent, PICK_CONTACT_REQUEST);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// If the request went well (OK) and the request was PICK_CONTACT_REQUEST
		if (resultCode == Activity.RESULT_OK && requestCode == PICK_CONTACT_REQUEST) {
			// Perform a query to the contact's content provider for the contact's name
			Cursor cursor = getContentResolver().query(data.getData(),
				new String[] { Contacts.DISPLAY_NAME }, null, null, null);
			if (cursor.moveToFirst()) { // True if the cursor is not empty
				int columnIndex = cursor.getColumnIndex(Contacts.DISPLAY_NAME);
				String name = cursor.getString(columnIndex);
				// Do something with the selected contact's name...
				append("has elegido a " + name);
			}
		}
	}

	private void dialPhoneNumber() {
		startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:617445049")));
	}

	private void sendMail() {
		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		String[] recipients = new String[] { "my@email.com", "", };
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, recipients);
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Test");
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "This is email's message");
		emailIntent.setType("text/plain");
		startActivity(Intent.createChooser(emailIntent, "Send mail..."));
	}

	/**
	 * Computes the battery level by registering a receiver to the intent triggered by a battery
	 * status/level change.
	 */
	private void batteryLevel() {
		BroadcastReceiver batteryLevelReceiver = new BroadcastReceiver() {
			public void onReceive(Context context, Intent intent) {
				context.unregisterReceiver(this);
				int rawlevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
				int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
				int level = -1;
				if (rawlevel >= 0 && scale > 0) {
					level = (rawlevel * 100) / scale;
				}
				append("Battery Level Remaining: " + level + "%");
			}
		};
		IntentFilter batteryLevelFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		registerReceiver(batteryLevelReceiver, batteryLevelFilter);
	}

	private void location() {
		// initialize
		Context context = getApplicationContext();
		locationManager = (LocationManager) getApplicationContext().getSystemService(
			Context.LOCATION_SERVICE);
		// Coarse accuracy is specified here to get the fastest possible result.
		// The calling Activity will likely (or have already) request ongoing
		// updates using the Fine location provider.
		criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_COARSE);

		append("Consulta última localización...");
		locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		List<String> matchingProviders = locationManager.getAllProviders();
		Location validLocation = null;
		for (String provider : matchingProviders) {
			Location location = locationManager.getLastKnownLocation(provider);
			printLocationByType(location, provider, "Last");
			// Se le pide a cada proveedor que intente obtener la posicion actual
			locationManager.requestLocationUpdates(provider, 0, 0, singeUpdateListener,
				context.getMainLooper());
			if (location != null) {
				validLocation = location;
			}
		}

		if (validLocation != null) {
			List<String> cells = null;
			cells = generateGeoCell(new Point(validLocation.getAltitude(),
				validLocation.getLongitude()));
			append("GeoCells");
			for (String cell : cells) {
				append(cell);
			}
		}

		// Construct the Pending Intent that will be broadcast by the oneshot
		// location update.
		// Intent updateIntent = new Intent(SINGLE_LOCATION_UPDATE_ACTION);
		// singleUpatePI = PendingIntent.getBroadcast(context, 0, updateIntent,
		// PendingIntent.FLAG_UPDATE_CURRENT);
	}

	protected LocationListener singeUpdateListener = new LocationListener() {
		public void onLocationChanged(Location location) {
			Log.d("HelloWorldActivity",
				"Single Location Update Received: " + location.getLatitude() + ","
					+ location.getLongitude());
			printLocationByType(location, location.getProvider(), "Actual");
			locationManager.removeUpdates(singeUpdateListener);
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

		public void onProviderEnabled(String provider) {
		}

		public void onProviderDisabled(String provider) {
		}
	};

	private void printLocationByType(Location location, String provider, String type) {
		append(type + " location " + provider + ": " + ((location == null) ? "none" : ""));
		if (location != null) {
			float accuracy = location.getAccuracy();
			long time = location.getTime();
			append("\n  accuracy=" + accuracy + ", time=" + dateFormat.format(new Date(time))
				+ "\n  lat=" + new BigDecimal(location.getLatitude()).setScale(4, CEILING)
				+ ", longitude=" + new BigDecimal(location.getLongitude()).setScale(4, CEILING));

		}
		append("");
	}

	public void append(CharSequence line) {
		text.append(line);
		text.append("\n");
		scroll.post(new Runnable() {
			public void run() {
				scroll.smoothScrollTo(0, text.getBottom());
			}
		});
	}

	OnClickListener mPickListener = new OnClickListener() {
		public void onClick(View v) {
			pickContact();
		}
	};

	OnClickListener mDialListener = new OnClickListener() {
		public void onClick(View v) {
			dialPhoneNumber();
		}
	};

	OnClickListener mSendListener = new OnClickListener() {
		public void onClick(View v) {
			sendMail();
		}
	};

	OnClickListener mBatteryListener = new OnClickListener() {
		public void onClick(View v) {
			batteryLevel();
		}
	};

	OnClickListener mLocationListener = new OnClickListener() {
		public void onClick(View v) {
			location();
		}
	};

	OnClickListener mPreferencesForm = new OnClickListener() {
		public void onClick(View v) {
			startActivity(new Intent(TmsActivity.this, PreferencesActivity.class));
		}
	};

	OnClickListener mFencesActivity = new OnClickListener() {
		public void onClick(View v) {
			startActivity(new Intent(TmsActivity.this, FencesActivity.class));
		}
	};
	
	OnClickListener mUserAlarm = new OnClickListener() {
        public void onClick(View v) {
            startActivity(new Intent(MainActivity.this, ExampleAlarmUserActivity.class));
        }
    };

}