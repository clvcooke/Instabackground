package me.clvcooke.instabackground;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.SystemClock;
import android.util.Log;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
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

    public void setAlarm(int timeInSeconds, Context context){

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(context,0,intent,0);
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(),timeInSeconds*1000, alarmIntent);
    }


    @Override
    public void onReceive(Context context, Intent intent) {


        TinyDB tinyDB = new TinyDB(context);

        List<String> files = tinyDB.getListString(Strings.FILES_SHARED_PREF);
        int seconds = tinyDB.getInt(Strings.SECONDS_SHARED_PREF, 14400);


        Log.d("INSTA","changing photo");

        if(files != null) {
            while(files.remove("?"));

            Random rand = new Random();
            String file = files.get(rand.nextInt(files.size())).substring(7);
            String currentFile = tinyDB.getString(Strings.CURRENT_BACKGROUND_IMAGE_URL);

            if(currentFile != file){
                tinyDB.putString(Strings.CURRENT_BACKGROUND_IMAGE_URL,file);
                UtilityMethods.setWallpaper(context, BitmapFactory.decodeFile(file));
            }
        }
    }
}
