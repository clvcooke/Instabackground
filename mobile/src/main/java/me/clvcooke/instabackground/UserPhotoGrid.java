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
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import me.clvcooke.instabackground.Adapters.ImageGridAdapter;
import me.clvcooke.instabackground.Utilities.UtilityMethods;

/**
 * Created by Colin on 2015-05-24.
 */
public class UserPhotoGrid extends Activity {

    ImageGridAdapter imageGridAdapter;
    ArrayList<String> selectedURLS;
    Button setButton;
    Context context;
    Menu mMenu;
    boolean isSelecting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_photo_grid);
        Bundle extras = getIntent().getExtras();
        String username = extras.getString("user");
        context = this;
        setButton = (Button) findViewById(R.id.setButton);
        selectedURLS = extras.getStringArrayList("selected");
        if (!selectedURLS.isEmpty()) {
            changeSelectMode(true);
        }

        GridView gridView = (GridView) findViewById(R.id.userGridView);
        gridView.setNumColumns(3);
        ((TextView) findViewById(R.id.textView)).setText(username);

        imageGridAdapter = new ImageGridAdapter(this);

        File[] files = UtilityMethods.getSavedFiles(UtilityMethods.DIRECTORY_PREFIX + username);
        ArrayList<String> urls = new ArrayList<>();
        for (File file : files) {
            urls.add("file://" + file.getPath());
        }
        gridView.setAdapter(imageGridAdapter);
        imageGridAdapter.setUrls(urls, username, selectedURLS);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = imageGridAdapter.onItemClick(view, position);
                if (!selectedURLS.remove(url)) {
                    selectedURLS.add(url);
                }
                if(selectedURLS.isEmpty()){
                    changeSelectMode(false);
                    isSelecting = false;
                }else if(!isSelecting){
                    changeSelectMode(true);
                    isSelecting = true;
                }
            }
        });

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                imageGridAdapter.onItemLongClick(position);
                return true;
            }
        });

        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BackgroundSetter.class);
                intent.putStringArrayListExtra("urls", selectedURLS);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("urls", selectedURLS);
        setResult(RESULT_OK, returnIntent);
        finish();
        super.onBackPressed();
    }

    private void changeSelectMode(boolean isSelecting){
        getActionBar().setBackgroundDrawable(isSelecting ? new ColorDrawable((getResources().getColor(R.color.actionBarSet))) : getResources().getDrawable(R.color.primary_dark_material_dark));
        getActionBar().setTitle(isSelecting ? "Select Photos" : getString(R.string.app_name));
        setButton.setEnabled(isSelecting);
        mMenu.getItem(0).setVisible(isSelecting);
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
                imageGridAdapter.deselectAll();
                changeSelectMode(false);
                isSelecting = false;
                return true;
            case R.id.action_settings:
                startActivity(new Intent(this, Settings.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



}
