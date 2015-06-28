package me.clvcooke.instabackground;

import android.app.ActionBar;
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
import java.util.List;

import me.clvcooke.instabackground.Adapters.GalleryGridAdapter;
import me.clvcooke.instabackground.Constants.Integers;
import me.clvcooke.instabackground.Utilities.UtilityMethods;

/**
 * Created by Colin on 15/05/2015.
 */
public class PhotoDisplayer extends Activity {

    private ArrayList<String> selectedURLS = new ArrayList<>();
    private Button setButton;
    private Menu mMenu;

    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photos_page);
        final GridView gridView = (GridView) findViewById(R.id.gridView);

        final ColorDrawable backgroundColor = new ColorDrawable((getResources().getColor(R.color.actionBarSet)));
        final Context context = this;

        setButton = (Button) findViewById(R.id.background_button);

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
                intent.putStringArrayListExtra("selected", selectedURLS);
                startActivityForResult(intent, Integers.PICK_PHOTO_REQUEST);
            }
        });

        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BackgroundSetter.class);
                intent.putStringArrayListExtra("urls", selectedURLS);
                disableSelectingMode();
                startActivity(intent);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Integers.PICK_PHOTO_REQUEST) {
            if (resultCode == RESULT_OK) {
                selectedURLS = data.getExtras().getStringArrayList("urls");
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
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_picker, menu);
        mMenu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.cancel:
                selectedURLS = new ArrayList<String>();
                disableSelectingMode();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void disableSelectingMode(){

        getActionBar().setBackgroundDrawable(getResources().getDrawable(R.color.primary_dark_material_dark));
        setButton.setEnabled(false);
        selectedURLS = null;
    }
}


