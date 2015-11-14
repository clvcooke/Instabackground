package me.clvcooke.instabackground;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.zip.Inflater;

import me.clvcooke.instabackground.Constants.Strings;
import me.clvcooke.instabackground.Utilities.TinyDB;
import me.clvcooke.instabackground.Utilities.UtilityMethods;

/**
 * Created by Colin on 2015-06-27.
 */
public class Settings extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.settings_page);

        final Context context = this;

        //TODO don't show these buttons if the service isn't running

     //   Button timeButton = (Button) findViewById(R.id.changeTime);
        final Button stopButton = (Button) findViewById(R.id.stopWallpaper);

        TinyDB tinyDB = new TinyDB(context);

        if (!tinyDB.getBoolean("isRunning",false)){
      //      timeButton.setEnabled(false);
            stopButton.setEnabled(false);
        }

/*
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setTitle("Navigate Away?")
                        .setMessage("This will take you to your device's file manager, do you want to continue?")
                        .setNeutralButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String directory = UtilityMethods.DIRECTORY_PREFIX;
                                File direct = new File(Environment.getExternalStorageDirectory() + directory);
                                if (!direct.exists()) {
                                    direct.mkdirs();
                                }
                                Intent fileMangerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                                fileMangerIntent.setType("file/");
                                startActivity(fileMangerIntent);
                            }
                        });

                builder.create().show();
            }
        });
*/
  /*      timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                LayoutInflater inflater = getLayoutInflater();


                View view = inflater.inflate(R.layout.change_timer_menu, null);

                final EditText text = (EditText) view.findViewById(R.id.hours);

                builder
                        .setView(inflater.inflate(R.layout.change_timer_menu, null))
                        .setPositiveButton("Set new rotation time", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(text.getText().toString().isEmpty()){
                                    Toast.makeText(context, "Please enter an amount of time", Toast.LENGTH_SHORT);
                                    return;
                                }
                                TinyDB tinyDB = new TinyDB(context);
                                AlarmReceiver alarmReceiver = new AlarmReceiver();
                                int seconds = (int) Double.parseDouble(text.getText().toString()) * 3600;
                                tinyDB.putInt(Strings.SECONDS_SHARED_PREF, seconds);
                                alarmReceiver.setAlarm(seconds, context);
                                dialog.dismiss();
                            }


                        })
                        .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builder.create().show();
            }
        });*/

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setMessage("Cancel background changing?")
                        .setNeutralButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                AlarmReceiver alarmReceiver = new AlarmReceiver();
                                alarmReceiver.cancelAlarm(context);
                                TinyDB tinyDB = new TinyDB(context);
                                tinyDB.putBoolean("isRunning",false);
                                stopButton.setEnabled(false);
                            }
                        });

                builder.create().show();
            }
        });


    }
}
