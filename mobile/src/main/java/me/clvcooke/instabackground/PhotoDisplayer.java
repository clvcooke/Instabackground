package me.clvcooke.instabackground;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.clvcooke.instabackground.Adapters.GalleryGridAdapter;
import me.clvcooke.instabackground.Constants.Integers;
import me.clvcooke.instabackground.Utilities.TinyDB;
import me.clvcooke.instabackground.Utilities.UtilityMethods;

/**
 * Created by Colin on 15/05/2015.
 */
public class PhotoDisplayer extends Activity {

    private ArrayList<String> backgroundURLS = new ArrayList<>();

    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photos_page);
        final GridView gridView = (GridView) findViewById(R.id.gridView);
        final Button setButton = (Button) findViewById(R.id.background_button);
        final ColorDrawable backgroundColor = new ColorDrawable(Color.parseColor("#002e3e"));
        boolean isToggled = false;
        final Context context = this;

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
                String username = ((GalleryGridAdapter) gridView.getAdapter()).getUser(position);
                Intent intent = new Intent(context, UserPhotoGrid.class);
                intent.putExtra("user", username);
                if(!setButton.getText().equals("Set Background")){
                    intent.putStringArrayListExtra("selected", backgroundURLS);
                }
                startActivityForResult(intent, Integers.PICK_PHOTO_REQUEST);
            }
        });

        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button button = (Button) v;
                ActionBar actionBar = getActionBar();
                String text = button.getText().toString();
                if (text.equals("Set Background")) {
                    actionBar.setBackgroundDrawable(backgroundColor);
                    actionBar.setTitle("Select Photos");
                    button.setText("Save Selection");
                    TinyDB tinyDB = new TinyDB(context);
                    tinyDB.putListString("files", backgroundURLS);
                } else {
                    actionBar.setBackgroundDrawable(getResources().getDrawable(R.color.primary_dark_material_dark));
                    actionBar.setTitle(getResources().getString(R.string.app_name));
                    button.setText("Set Background");
                    //Go to background selector mode
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Integers.PICK_PHOTO_REQUEST){
            if(resultCode == RESULT_OK){
                List<String> urls = data.getExtras().getStringArrayList("urls");
                backgroundURLS.removeAll(urls);
                backgroundURLS.addAll(urls);         }
        }
    }

}
