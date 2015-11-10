package me.clvcooke.instabackground;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.HashSet;

import me.clvcooke.instabackground.Adapters.ImageGridAdapter;
import me.clvcooke.instabackground.Constants.Strings;
import me.clvcooke.instabackground.Utilities.TinyDB;

/**
 * Created by Colin on 2015-05-27.
 */
public class BackgroundSetter extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayList<String> urls = getIntent().getExtras().getStringArrayList("urls");
        setContentView(R.layout.background_setter);
        final Button removeButton = (Button) findViewById(R.id.removePhotos);
        final Button setRotationtimeButton = (Button) findViewById(R.id.setterButton);
        setRotationtimeButton.setEnabled(false);
        final EditText numberText = (EditText) findViewById(R.id.time);

        GridView gridView = (GridView) findViewById(R.id.backgroundPhotos);
        final Context context = this;

        final ImageGridAdapter imageGridAdapter = new ImageGridAdapter(this);
        gridView.setAdapter(imageGridAdapter);
        imageGridAdapter.loadUrls(new HashSet<Integer>(), urls, "");

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                imageGridAdapter.onItemLongClick(position);
                return true;
            }
        });

        gridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                imageGridAdapter.onItemClick(view, position);
                removeButton.setEnabled(imageGridAdapter.minimumSelected());
            }

        });


        setRotationtimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TinyDB tinyDB = new TinyDB(context);
                tinyDB.putBoolean("isRunning", true);
                tinyDB.putListString(Strings.FILES_SHARED_PREF, imageGridAdapter.getUrls());
                AlarmReceiver alarmReceiver = new AlarmReceiver();
                int seconds = (int) (Double.parseDouble(numberText.getText().toString()) * 3600);
                tinyDB.putInt(Strings.SECONDS_SHARED_PREF, seconds);
                alarmReceiver.setAlarm(seconds, context);

                //transition to control page
                Intent intent = new Intent(context, ControlPage.class);
                intent.putExtra("backgroundset", true);
                startActivity(intent);
            }
        });


        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageGridAdapter.removeSelected();
                removeButton.setEnabled(false);
            }
        });

        numberText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                try {
                    if (numberText.getText() == null || numberText.getText().length() == 0) {
                        setRotationtimeButton.setEnabled(false);
                    } else if (Double.parseDouble(numberText.getText().toString()) > 0) {
                        setRotationtimeButton.setEnabled(true);
                    }
                }catch (NumberFormatException e){
                    //do nothing, its all good
                }
                return false;
            }
        });


    }
}
