package me.clvcooke.instabackground;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

/**
 * Created by Michael on 15/05/2015.
 */
public class PhotoDisplayer extends ActionBarActivity {

    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photos_page);

        TextView textView = (TextView) findViewById(R.id.textView);
        GridView photoGrid = (GridView) findViewById(R.id.pictureGrid);
    };


}
