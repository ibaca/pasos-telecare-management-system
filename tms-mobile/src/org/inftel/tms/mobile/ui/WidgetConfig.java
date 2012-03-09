
package org.inftel.tms.mobile.ui;

import static android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_ID;
import static android.appwidget.AppWidgetManager.INVALID_APPWIDGET_ID;

import org.inftel.tms.mobile.receivers.WidgetReceiver;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;

public class WidgetConfig extends Activity {

    private int widgetId = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Obtenemos el Intent que ha lanzado esta ventana
        // y recuperamos sus parámetros
        Intent intentOrigen = getIntent();
        Bundle params = intentOrigen.getExtras();

        // Obtenemos el ID del widget que se está configurando
        widgetId = params.getInt(EXTRA_APPWIDGET_ID, INVALID_APPWIDGET_ID);

        // Establecemos el resultado por defecto
        setResult(RESULT_OK);

        // Actualizamos el widget tras la configuración
        AppWidgetManager appWidgetManager = AppWidgetManager
                .getInstance(this);
        WidgetReceiver.actualizarWidget(this, appWidgetManager, widgetId);

        // Devolvemos como resultado: ACEPTAR (RESULT_OK)
        Intent resultado = new Intent();
        resultado.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        setResult(RESULT_OK, resultado);
        finish();
    }
}
