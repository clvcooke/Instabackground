package me.clvcooke.instabackground;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

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
        Button backgroundButton = (Button) findViewById(R.id.setterButton);
        final EditText numberText = (EditText) findViewById(R.id.time);
        GridView gridView = (GridView) findViewById(R.id.backgroundPhotos);
        final Context context = this;

        final ImageGridAdapter imageGridAdapter = new ImageGridAdapter(this);
        gridView.setAdapter(imageGridAdapter);
        imageGridAdapter.setUrls(urls, "");

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
                removeButton.setEnabled(imageGridAdapter.getSelected().size() > 0);
            }

        });


        backgroundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numberText.getText() != null)
                {
                    TinyDB tinyDB = new TinyDB(context);
                    tinyDB.putListString(Strings.FILES_SHARED_PREF, imageGridAdapter.getUrls());
                    AlarmReceiver alarmReceiver = new AlarmReceiver();

                    int seconds = (int) (Double.parseDouble(numberText.getText().toString()) * 3600);
                    tinyDB.putInt(Strings.SECONDS_SHARED_PREF, seconds);
                    alarmReceiver.setAlarm(seconds, context);
                } else {
                    Toast.makeText(context, "Enter an hour amount", Toast.LENGTH_SHORT);
                }
            }
        });


        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageGridAdapter.removeSelected();
            }
        });


    }
}
