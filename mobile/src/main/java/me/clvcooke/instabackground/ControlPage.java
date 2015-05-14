package me.clvcooke.instabackground;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Colin on 2015-05-13.
 */
public class ControlPage extends ActionBarActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.control_page);




        final Context context = this;
        Button searchButton = (Button) findViewById(R.id.search_intro_button);
        Button photosButton = (Button) findViewById(R.id.pictures_intro_button);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UserFinder.class);
                startActivity(intent);
            }
        });

        photosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UserFinder.class);
                startActivity(intent);
            }
        });


    }

}
