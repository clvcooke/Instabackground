package me.clvcooke.instabackground;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael on 15/05/2015.
 */
public class PhotoDisplayer extends Activity {

    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photos_page);
/*
        TextView textView = (TextView) findViewById(R.id.textView);
        GridView photoGrid = (GridView) findViewById(R.id.pictureGrid);

        ImageGridAdapter imageGridAdapter = new ImageGridAdapter(this);
        photoGrid.setAdapter(imageGridAdapter);

        List<String> list = new ArrayList<>();
        list.add("drawable://" + R.drawable.instabottom);

        imageGridAdapter.setUrls(list, "");*/
    };


}
