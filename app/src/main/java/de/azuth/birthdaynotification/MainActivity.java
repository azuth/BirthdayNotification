package de.azuth.birthdaynotification;


import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

public class MainActivity extends Activity {
    //public static final String NOTIFICATION_PREFERENCES = "NotifyPreferences";

    private AlarmSetActivity asa;

    public void onClickToast(View view) {
        String string = "Toast Ready!";
        Toast.makeText(this, string, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences settings = getSharedPreferences("PREFS_SAVED_DATA", MODE_PRIVATE);
        //settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


        if(!settings.contains("hour") || !settings.contains("minute")) {
            settings.edit().putInt("hour", 20);
            settings.edit().putInt("minute", 21);
            settings.edit().commit();
        }

        TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);
        timePicker.setCurrentHour(settings.getInt("hour", 12));
        timePicker.setCurrentMinute(settings.getInt("minute", 0));
        new AlarmSetActivity().setAlarm();


        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                SharedPreferences settings = getSharedPreferences("PREFS_SAVED_DATA", MODE_PRIVATE);
                settings.edit().putInt("hour", hourOfDay);
                settings.edit().putInt("minute", minute);
                settings.edit().commit();
                new AlarmSetActivity().setAlarm();
            }
        });
    }
}
