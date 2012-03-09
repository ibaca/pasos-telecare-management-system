
package org.inftel.tms.mobile.receivers;

import static org.inftel.tms.mobile.TmsConstants.MAX_DISTANCE;
import static org.inftel.tms.mobile.TmsConstants.MAX_TIME;
import static org.inftel.tms.mobile.pasos.PasosMessage.buildUserAlarm;
import static org.inftel.tms.mobile.util.PlatformSpecificImplementationFactory.getLastLocationFinder;

import org.inftel.tms.mobile.R;
import org.inftel.tms.mobile.TmsConstants;
import org.inftel.tms.mobile.pasos.PasosMessage;
import org.inftel.tms.mobile.pasos.PasosMessage.Builder;
import org.inftel.tms.mobile.services.SendPasosMessageIntentService;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.util.Log;
import android.widget.RemoteViews;

public class WidgetReceiver extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
            int[] appWidgetIds) {

        // Iteramos la lista de widgets en ejecucion
        for (int i = 0; i < appWidgetIds.length; i++) {
            // ID del widget actual
            int widgetId = appWidgetIds[i];

            // Actualizamos el widget actual
            actualizarWidget(context, appWidgetManager, widgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // Accedemos a las preferencias de la aplicaci�n
        SharedPreferences prefs = context.getSharedPreferences("WidgetPrefs",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        // Eliminamos las preferencias de los widgets borrados
        for (int i = 0; i < appWidgetIds.length; i++) {
            // ID del widget actual
            int widgetId = appWidgetIds[i];

            editor.remove("msg_" + widgetId);
        }

        // Aceptamos los cambios
        editor.commit();

        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("org.inftel.tms.mobile.intent.action.ACTUALIZAR_WIDGET")) {

            // Obtenemos el ID del widget a actualizar
            int widgetId = intent.getIntExtra(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);

            // Obtenemos el widget manager de nuestro contexto
            AppWidgetManager widgetManager = AppWidgetManager
                    .getInstance(context);

            // Actualizamos el widget
            if (widgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
                actualizarWidget(context, widgetManager, widgetId);
            }
        }

        super.onReceive(context, intent);
    }

    public static void actualizarWidget(Context context,
            AppWidgetManager appWidgetManager, int widgetId) {

        // Obtenemos la lista de controles del widget actual
        RemoteViews controles = new RemoteViews(context.getPackageName(),
                R.layout.miwidget);

        Location location = getLastLocationFinder(context).getLastBestLocation(
                MAX_DISTANCE, MAX_TIME);
        Builder messageBuilder = buildUserAlarm().cause(
                "click: solicitada asistencia usando widget");

        if (location != null) {
            messageBuilder.location(location.getLatitude(), location.getLongitude());
        } else {
            Log.w("WidgetReceiver",
                    "No se ha podido obtener localizacion al enviar una alerta de usuario");
        }
        PasosMessage message = messageBuilder.build();

        // Configurar y Lanzar servicio de envio de mensajes PaSOS
        Intent sendService = new Intent(context, SendPasosMessageIntentService.class);
        sendService.putExtra(TmsConstants.EXTRA_KEY_MESSAGE_CONTENT, message.toString());

        PendingIntent pendingIntent = PendingIntent.getService(context, widgetId, sendService,
                PendingIntent.FLAG_UPDATE_CURRENT);

        controles.setOnClickPendingIntent(R.id.BtnActualizar, pendingIntent);

        appWidgetManager.updateAppWidget(widgetId, controles);
    }
}
