package org.inftel.tms.mobile.ui;

import org.inftel.tms.mobile.R;

import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

/**
 * ¿QUE HACE ESTA ACTIVIDAD????
 * 
 * @author Cristian
 * 
 */
public class SendUserAlertActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sending);

		// Recuperar los datos a�adidos al intent padre
		Bundle userdata = getIntent().getBundleExtra("USERDATA");
		Bundle serverdata = getIntent().getBundleExtra("SERVERDATA");

		// Sacar algun dato por pantalla (opcional)
		TextView mssg = (TextView) findViewById(R.id.mssgSending);
		mssg.setText("Hola " + userdata.get("NAME").toString()
				+ " envio tus datos a " + serverdata.get("URI").toString());

		// Enviando los datos de Ususario a traves de la API de MigueQ
		// con los datos recuperados del intent padre
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		}

	}
}
