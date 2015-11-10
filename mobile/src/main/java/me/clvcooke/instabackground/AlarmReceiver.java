package me.clvcooke.instabackground;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.SystemClock;
import android.util.Log;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

import me.clvcooke.instabackground.Constants.Strings;
import me.clvcooke.instabackground.Utilities.TinyDB;
import me.clvcooke.instabackground.Utilities.UtilityMethods;

/**
 * Created by Colin on 2015-01-06.
 */
public class AlarmReceiver extends BroadcastReceiver {

    private PendingIntent alarmIntent;

    public void setAlarm(int timeInSeconds, Context context) {

        Intent receiverIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 123456789, receiverIntent, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), timeInSeconds * 1000, sender);
    }

    public void cancelAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmIntent == null) {
            Intent intent = new Intent(context, AlarmReceiver.class);
            alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        }
        alarmManager.cancel(alarmIntent);
    }


    @Override
    public void onReceive(Context context, Intent intent) {

        try {

            TinyDB tinyDB = new TinyDB(context);
            List<String> files = tinyDB.getListString(Strings.FILES_SHARED_PREF);
            if (files != null) {
                while (files.remove("?")) ;
                Random rand = new Random();
                String currentFile = tinyDB.getString(Strings.CURRENT_BACKGROUND_IMAGE_URL);
                files.remove(currentFile);
                if (files.size() > 0) {
                    String file = files.get(rand.nextInt(files.size()));
                    tinyDB.putString(Strings.CURRENT_BACKGROUND_IMAGE_URL, file);
                    UtilityMethods.setWallpaper(context, BitmapFactory.decodeFile(file));
                }
            }
        } catch (Exception e) {
            //never crash
        }
    }
}
