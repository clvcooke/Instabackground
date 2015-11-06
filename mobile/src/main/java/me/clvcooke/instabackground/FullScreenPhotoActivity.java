package me.clvcooke.instabackground;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by Colin on 2015-05-23.
 */
public class FullScreenPhotoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fullscreen_image_view);
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        final Rect window = new Rect();

        int width =
                Resources.getSystem().getDisplayMetrics().widthPixels;
        Picasso.with(this).load(
                getIntent().getExtras().getString("url")
        ).resize(
                width, width).into(imageView);
    }
}
