package com.aa.myapplication;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class notificationReciver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Notification notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.generate_button_nobackground)
                .setContentTitle(intent.getStringExtra("title"))
                .setContentText(intent.getStringExtra("content"))
                .build();

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(42, notification);
    }
}
