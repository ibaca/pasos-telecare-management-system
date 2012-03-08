
package org.inftel.tms.mobile.util;

import static android.app.PendingIntent.FLAG_CANCEL_CURRENT;
import static android.app.PendingIntent.getActivity;
import static android.content.Context.NOTIFICATION_SERVICE;

import org.inftel.tms.mobile.TmsConstants;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.SharedPreferences;

public class Notifications {

    /** Crea una notificacion con el mensaje pasado. */
    public static int generateNotification(Context context, String message) {
        int icon = android.R.drawable.stat_sys_upload;

        SharedPreferences settings = context.getSharedPreferences(
                TmsConstants.SHARED_PREFERENCE_FILE, Context.MODE_PRIVATE);
        int notificatonID = settings.getInt("notificationID", 0);

        generateNotification(icon, context, message, notificatonID);

        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("notificationID", (notificatonID + 1) % 32);
        editor.commit();

        return notificatonID;
    }

    /** Actualiza una notificacion existente. */
    public static void generateNotification(Context context, String message, int id) {
        int icon = android.R.drawable.stat_sys_upload_done;
        generateNotification(icon, context, message, id);
    }

    private static void generateNotification(int icon, Context context, String message, int id) {
        long when = System.currentTimeMillis();

        PendingIntent cancelCurrent = getActivity(context, 0, null, FLAG_CANCEL_CURRENT);
        Notification notification = new Notification(icon, message, when);
        notification.setLatestEventInfo(context, "TMS", message, cancelCurrent);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        NotificationManager nm;
        nm = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        nm.notify(id, notification);
    }

}
