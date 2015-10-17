package de.azuth.birthdaynotification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Calendar;

public class SetAlarmReceiver extends BroadcastReceiver {
    private static final int NOTIFICATION_REQUES_CODE = 123;

    @Override
    public void onReceive(Context context, Intent intent) {
        setAlarm(context);
    }

    public void setAlarm(Context context){
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());

        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, settings.getInt("hour",12));
        startTime.set(Calendar.MINUTE, settings.getInt("minute",1));
        startTime.set(Calendar.SECOND, 0);

        Intent serviceIntent = new Intent(context, NotifyService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, NOTIFICATION_REQUES_CODE, serviceIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        cancelAlarmIfExists(context,serviceIntent);

        AlarmManager alarmManager  = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, startTime.getTimeInMillis() , AlarmManager.INTERVAL_DAY , pendingIntent);
    }

    private void cancelAlarmIfExists(Context context,Intent intent) {
        try {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, NOTIFICATION_REQUES_CODE, intent, 0);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(pendingIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
