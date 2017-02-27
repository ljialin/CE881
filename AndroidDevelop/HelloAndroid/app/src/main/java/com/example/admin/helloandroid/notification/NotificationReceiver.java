package com.example.admin.helloandroid.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

import static android.app.AlarmManager.*;

/**
 * Created by Jialin Liu on 20/10/2016.
 * CSEE, University of Essex
 * jialin.liu@essex.ac.uk
 */

public class NotificationReceiver extends WakefulBroadcastReceiver {
    private static final String TAG = "NotificationReceiver";
    private static final String ACTION_START_NOTIFICATION_SERVICE = "ACTION_START_NOTIFICATION_SERVICE";
    private static final String ACTION_DELETE_NOTIFICATION = "ACTION_DELETE_NOTIFICATION";

    public static int NOTIFICATIONS_INTERVAL_IN_FIFTEEN_MINUTES = 1;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Intent serviceIntent = null;
        if (ACTION_START_NOTIFICATION_SERVICE.equals(action)) {
            Log.i(getClass().getSimpleName(), "starting notification service");
            serviceIntent = NotificationIntentService.createIntentStartNotificationService(context);
            Log.d(TAG, "Notification: " + "starting notification service");
        } else if (ACTION_DELETE_NOTIFICATION.equals(action)) {
            Log.i(getClass().getSimpleName(), "delete notification service");
            serviceIntent = NotificationIntentService.createIntentDeleteNotification(context);
            Log.d(TAG, "Notification: " + "delete notification service");
        }

        if (serviceIntent != null) {
            startWakefulService(context, serviceIntent);
        }
    }

    public static void setupAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent alarmIntent = getStartPendingIntent(context);
        alarmManager.setRepeating(RTC_WAKEUP,
                getTriggerAt(new Date()),
                NOTIFICATIONS_INTERVAL_IN_FIFTEEN_MINUTES * INTERVAL_FIFTEEN_MINUTES,
                alarmIntent);
        Log.d(TAG, "Notification: " + "setup notification every " + (NOTIFICATIONS_INTERVAL_IN_FIFTEEN_MINUTES*15)+ " minutes");
    }

    private static long getTriggerAt(Date now) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        //calendar.add(Calendar.HOUR, NOTIFICATIONS_INTERVAL_IN_HOURS);
        return calendar.getTimeInMillis();
    }

    private static PendingIntent getStartPendingIntent(Context context) {
        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.setAction(ACTION_START_NOTIFICATION_SERVICE);
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static PendingIntent getDeleteIntent(Context context) {
        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.setAction(ACTION_DELETE_NOTIFICATION);
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
