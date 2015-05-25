package me.clvcooke.instabackground;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.clvcooke.instabackground.Adapters.GalleryGridAdapter;

/**
 * Created by Colin on 15/05/2015.
 */
public class PhotoDisplayer extends Activity {

    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photos_page);
        final GridView gridView = (GridView) findViewById(R.id.gridView);
        final Button setButton = (Button) findViewById(R.id.background_button);
        final ColorDrawable backgroundColor = new ColorDrawable(Color.parseColor("#002e3e"));
        boolean isToggled = false;

        GalleryGridAdapter adapter = new GalleryGridAdapter(this);
        gridView.setAdapter(adapter);
        List<String> users = new ArrayList<>();
        List<String> urls = new ArrayList<>();
        File[] files = UtilityMethods.getSavedFiles(UtilityMethods.DIRECTORY_PREFIX);
        if (files != null) {
            for (File file : files) {
                File[] pictures = file.listFiles();
                if (pictures != null && pictures.length != 0) {
                    urls.add("file://" + file.listFiles()[0].getPath());
                    users.add(file.getName());
                }
            }
        }
        adapter.setUrls(urls, users);
        adapter.notifyDataSetChanged();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((GalleryGridAdapter) gridView.getAdapter()).onItemClick(view, position);
            }
        });

        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button button = (Button) v;
                ActionBar actionBar = getActionBar();
                String text = button.getText().toString();
                if(text.equals("Set Background")){
                    actionBar.setBackgroundDrawable(backgroundColor);
                    actionBar.setTitle("Select Photos");
                    button.setText("Save Selection");
                }else{
                    actionBar.setBackgroundDrawable(getResources().getDrawable(R.color.primary_dark_material_dark));
                    actionBar.setTitle(getResources().getString(R.string.app_name));
                    button.setText("Set Background");
                }
                //Go to background selector mode
            }
        });




    }



}
