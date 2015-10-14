package de.azuth.birthdaynotification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AutoStartNotifyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        new AlarmSetActivity().setAlarm();
    }
}
