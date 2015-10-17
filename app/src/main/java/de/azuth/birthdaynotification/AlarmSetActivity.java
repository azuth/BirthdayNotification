package de.azuth.birthdaynotification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class AlarmSetActivity {
    private static final int NOTIFICATION_REQUES_CODE = 123;

    private Context context;

    public AlarmSetActivity(Context context){
        this.context = context;
    }

    public void setAlarm(){
        //SharedPreferences settings =  context.getApplicationContext().getSharedPreferences("PREFS_SAVED_DATA", context.getApplicationContext().MODE_PRIVATE);
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        //SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());

        Log.d("am", settings.toString());

        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, settings.getInt("hour",12));
        startTime.set(Calendar.MINUTE, settings.getInt("minute",1));
        startTime.set(Calendar.SECOND, 0);
        Toast.makeText(context, "set to "+startTime.getTime(), Toast.LENGTH_LONG).show();

        // Adjust calendar day if time of day has already occurred today:
        if (startTime.getTimeInMillis() < System.currentTimeMillis())
            startTime.add(Calendar.DAY_OF_MONTH, 1);

        Intent serviceIntent = new Intent(context, NotifyService.class);
        PendingIntent pi = PendingIntent.getService(context, NOTIFICATION_REQUES_CODE, serviceIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        cancelAlarmIfExists(context,serviceIntent);

        AlarmManager alarmManager  = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
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
