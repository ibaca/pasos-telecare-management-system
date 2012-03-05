package org.inftel.tms.mobile.ui;

import org.inftel.tms.mobile.R;
import org.inftel.tms.mobile.R.id;
import org.inftel.tms.mobile.R.layout;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * ¿QUE HACE ESTA ACTIVIDAD????
 * @author Cristian
 *
 */
public class SendUserAlertActivity extends Activity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sending); 
        
        //Recuperar los datos a�adidos al intent padre
        Bundle userdata = getIntent().getBundleExtra("USERDATA");
        Bundle serverdata = getIntent().getBundleExtra("SERVERDATA");
        
        //Sacar algun dato por pantalla (opcional)
        TextView mssg = (TextView) findViewById(R.id.mssgSending);
        mssg.setText("Hola "+userdata.get("NAME").toString()+" envio tus datos a "+serverdata.get("URI").toString());
        
        //Enviando los datos de Ususario a traves de la API de MigueQ
        //con los datos recuperados del intent padre
        
        
    }
}
