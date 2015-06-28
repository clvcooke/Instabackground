package me.clvcooke.instabackground;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import me.clvcooke.instabackground.Constants.Strings;
import me.clvcooke.instabackground.Utilities.TinyDB;

/**
 * Created by Colin on 2015-06-28.
 */
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        TinyDB tinyDB = new TinyDB(context);
        if(tinyDB.getBoolean("isRunning",false)){
            AlarmReceiver alarmReceiver = new AlarmReceiver();
            alarmReceiver.setAlarm(tinyDB.getInt(Strings.SECONDS_SHARED_PREF, 3600),context);
        }
    }
}
