package de.azuth.birthdaynotification;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TimePicker;

public class MainActivity extends Activity {

    private SharedPreferences settings;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (!settings.contains("hour") || !settings.contains("minute")) {
            settings.edit().putInt("hour", 12).commit();
            settings.edit().putInt("minute", 35).commit();
        }

        TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);
        timePicker.setCurrentHour(settings.getInt("hour", 12));
        timePicker.setCurrentMinute(settings.getInt("minute", 0));
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            public void onTimeChanged(TimePicker view, int hour, int minute) {
                settings.edit().putInt("hour", hour).commit();
                settings.edit().putInt("minute", minute).commit();

                Intent intent = new Intent();
                intent.setAction("de.azuth.birthdaynotification.notify");
                sendBroadcast(intent);

            }
        });
    }
}
