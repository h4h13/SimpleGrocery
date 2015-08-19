package com.sharebuttons.grocery.receiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.sharebuttons.grocery.R;
import com.sharebuttons.grocery.ui.DetailsActivity;

/**
 * Created by Monkey D Luffy on 7/18/2015.
 */
public class AlertReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        createNotification(context, "Grocery List", "Let's got out for buying!", "Grocery Items are ready!");
    }

    private void createNotification(Context context, String msg, String msgText, String msgAlert) {
        PendingIntent notificationIntent = PendingIntent.getActivity(context, 0, new Intent(context, DetailsActivity.class), 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_add_white_48dp)
                .setContentTitle(msg)
                .setTicker(msgAlert)
                .setContentText(msgText);

        builder.setContentIntent(notificationIntent);
        builder.setDefaults(NotificationCompat.DEFAULT_LIGHTS);
        builder.setDefaults(NotificationCompat.DEFAULT_SOUND);
        builder.setDefaults(NotificationCompat.DEFAULT_VIBRATE);

        builder.setAutoCancel(true);
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1, builder.build());
    }
}





