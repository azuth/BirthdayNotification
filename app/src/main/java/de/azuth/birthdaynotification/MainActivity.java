package de.azuth.birthdaynotification;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

public class MainActivity extends Activity {

    private SharedPreferences settings;
    protected AlarmSetActivity asa;

    public void onClickToast(View view) {
        String string = "Toast Ready!";
        Toast.makeText(this, string, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        //settings = getApplicationContext().getSharedPreferences("PREFS_SAVED_DATA", MODE_PRIVATE);


        if(!settings.contains("hour") || !settings.contains("minute")) {
            settings.edit().putInt("hour", 18);
            settings.edit().putInt("minute", 33);
            settings.edit().commit();
        }
        Log.d("main", settings.toString());
        //asa = new AlarmSetActivity(getApplicationContext());
        //asa.setAlarm();

        TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);
        timePicker.setCurrentHour(settings.getInt("hour", 12));
        timePicker.setCurrentMinute(settings.getInt("minute", 0));

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            public void onTimeChanged(TimePicker view, int hour, int minute) {
                settings.edit().putInt("hour", hour);
                settings.edit().putInt("minute", minute);
                settings.edit().commit();
                //asa.setAlarm();

                Intent intent = new Intent();
                intent.setAction("de.azuth.birthdaynotification.notify");
                sendBroadcast(intent);

            }
        });
    }
}
