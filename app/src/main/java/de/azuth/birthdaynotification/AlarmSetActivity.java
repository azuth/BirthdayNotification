package de.azuth.birthdaynotification;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class AlarmSetActivity extends Activity{
    private static final int NOTIFICATION_REQUES_CODE = 123;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setAlarm(){
        SharedPreferences settings =  getSharedPreferences("PREFS_SAVED_DATA", MODE_PRIVATE);
        //SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, settings.getInt("hours",12));
        startTime.set(Calendar.MINUTE, settings.getInt("minutes",0));
        startTime.set(Calendar.SECOND, 0);

        // Adjust calendar day if time of day has already occurred today:
        if (startTime.getTimeInMillis() < System.currentTimeMillis())
            startTime.add(Calendar.DAY_OF_MONTH, 1);

        Intent serviceIntent = new Intent(this, NotifyService.class);
        PendingIntent pi = PendingIntent.getService(this, NOTIFICATION_REQUES_CODE, serviceIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        cancelAlarmIfExists(this,serviceIntent);

        AlarmManager alarmManager  = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, startTime.getTimeInMillis(), TimeUnit.MINUTES.toMillis(1), pi); //AlarmManager.INTERVAL_DAY
    }

    private void cancelAlarmIfExists(Context context,Intent intent) {
        try {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, NOTIFICATION_REQUES_CODE, intent, 0);
            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            am.cancel(pendingIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
