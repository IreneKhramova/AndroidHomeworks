package com.example.irene.khramovahomework7;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class Receiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "Channel id";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Receiver", "onReceive");
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        String message = context.getString(R.string.notification_text_divorce,
                intent.getStringExtra(InfoActivity.EXTRA_BRIDGE_NAME),
                intent.getStringExtra(InfoActivity.EXTRA_TIME_BEFORE_DIVORCE));

        //TODO: multiline text
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_alarm)
                .setContentTitle(context.getString(R.string.notification_title_divorce))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentText(message);

        notificationManager.notify(intent.getIntExtra(InfoActivity.EXTRA_ID, 12), builder.build());
    }
}
