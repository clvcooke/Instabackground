package me.clvcooke.instabackground;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import me.clvcooke.instabackground.Adapters.AlbumGridAdapter;
import me.clvcooke.instabackground.Constants.Integers;
import me.clvcooke.instabackground.Utilities.UtilityMethods;

/**
 * Created by Colin on 15/05/2015.
 */
public class Albums extends Activity {

    private HashSet<String> selectedURLS = new HashSet<>();
    private Button setButton;
    private Menu mMenu;

    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photos_page);
        final GridView gridView = (GridView) findViewById(R.id.gridView);

        final Context context = this;

        setButton = (Button) findViewById(R.id.background_button);

        AlbumGridAdapter adapter = new AlbumGridAdapter(this);
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
                String username = ((AlbumGridAdapter) gridView.getAdapter()).getUser(position);
                Intent intent = new Intent(context, SavedPhotos.class);
                intent.putExtra("user", username);
                intent.putExtra("selected", selectedURLS);
                startActivityForResult(intent, Integers.PICK_PHOTO_REQUEST);
            }
        });

        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BackgroundSetter.class);
                intent.putExtra("urls", selectedURLS);
                changeSelectMode(false);
                startActivity(intent);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Integers.PICK_PHOTO_REQUEST) {
            if (resultCode == RESULT_OK) {
                selectedURLS = (HashSet<String>) data.getExtras().get("urls");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setButton.setEnabled(!selectedURLS.isEmpty());
                        mMenu.getItem(0).setVisible(!selectedURLS.isEmpty());
                        getActionBar().setBackgroundDrawable(!selectedURLS.isEmpty() ? new ColorDrawable((getResources().getColor(R.color.actionBarSet))) : getResources().getDrawable(R.color.primary_dark_material_dark));
                    }
                });
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMenu = menu;
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.cancel:
                selectedURLS = new HashSet<>();
                changeSelectMode(false);
                return true;
            case R.id.action_settings:
                startActivity(new Intent(this, Settings.class));
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void changeSelectMode(boolean isSelecting){
        getActionBar().setBackgroundDrawable(isSelecting ? new ColorDrawable((getResources().getColor(R.color.actionBarSet))) : getResources().getDrawable(R.color.primary_dark_material_dark));
        getActionBar().setTitle(isSelecting ? "Select Photos" : getString(R.string.app_name));
        setButton.setEnabled(isSelecting);
        mMenu.getItem(0).setVisible(isSelecting);
    }
}


