package me.clvcooke.instabackground;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.clvcooke.instabackground.Adapters.ImageGridAdapter;

/**
 * Created by Colin on 2015-05-24.
 */
public class UserPhotoGrid extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_photo_grid);


        String username = getIntent().getExtras().getString("user");

        GridView gridView = (GridView) findViewById(R.id.userGridView);
        gridView.setNumColumns(3);


        ((TextView) findViewById(R.id.textView)).setText(username);

        final ImageGridAdapter imageGridAdapter = new ImageGridAdapter(this);

        File[] files = UtilityMethods.getSavedFiles(UtilityMethods.DIRECTORY_PREFIX + username);
        List<String> urls = new ArrayList<>();
        for (File file : files) {
            urls.add("file://" + file.getPath());
        }
        gridView.setAdapter(imageGridAdapter);
        //TODO use overloaded function
        imageGridAdapter.setUrls(urls, username);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                imageGridAdapter.onItemClick(view, position);
            }
        });

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                imageGridAdapter.onItemLongClick(position);
                return true;
            }
        });
    }
}
