package org.inftel.tms.mobile;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class PreferencesFormActivity extends PreferenceActivity {

	// private SharedPreferences mPrefs;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.preferences_form);

		addPreferencesFromResource(R.xml.preferences);

		// mPrefs = getSharedPreferences("MySamplePreferences", MODE_PRIVATE);
		// final Button button = (Button) findViewById(R.id.button1);
		// button.setOnClickListener(new View.OnClickListener() {
		// public void onClick(View v) {
		// EditText et = (EditText) findViewById(R.id.editText1);
		// String texto = et.getText().toString();
		// mPrefs.edit().putString("txt", texto).commit();
		// }
		// });

	}
}