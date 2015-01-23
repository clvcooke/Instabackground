package me.clvcooke.instabackground;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Random;

/**
 * Created by Colin on 2015-01-06.
 */
public class AlarmReceiver extends BroadcastReceiver {

    private PendingIntent alarmIntent;

    public void setAlarm(int timeInSeconds, Context context, int count){

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        //Define intent
        Intent intent = new Intent(context, AlarmReceiver.class);
       // intent.putExtra(UtilityMethods.COUNT_STRING, count);


        alarmIntent = PendingIntent.getBroadcast(context,0,intent,0);


        SharedPreferences sharedPreferences = context.getSharedPreferences(UtilityMethods.COUNT_STRING, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(UtilityMethods.COUNT_STRING, count);

        //TODO
        int i = 0;
        i++;

        editor.commit();


        //TODO change ELAPSED_REALTIME_WAKEUP
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(),timeInSeconds * 1000, alarmIntent);


    }


    @Override
    public void onReceive(Context context, Intent intent) {



        SharedPreferences sharedPreferences = context.getSharedPreferences(UtilityMethods.COUNT_STRING, Context.MODE_PRIVATE);
        int amount = sharedPreferences.getInt(UtilityMethods.COUNT_STRING,1);

      //  Toast.makeText(context,Integer.toString(amount), Toast.LENGTH_SHORT).show();
        //just for now do a random picture out of the amount stored
        //TODO not that


        Random rand = new Random();
        int randomNumber = rand.nextInt(amount + 1);


        ContextWrapper contextWrapper = new ContextWrapper(context);


        File directory = contextWrapper.getDir(UtilityMethods.IMAGE_DIR, Context.MODE_PRIVATE);

        File path = new File(directory, UtilityMethods.IMAGE_FILE_NAME_PREFIX + Integer.toString(randomNumber+1));


        if(path.exists()){
            Bitmap bitmap = BitmapFactory.decodeFile(path.getAbsolutePath());
            UtilityMethods.setWallpaper(context, bitmap);
        }
    }
}
