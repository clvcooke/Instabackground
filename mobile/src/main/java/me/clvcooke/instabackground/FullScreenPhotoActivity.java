package me.clvcooke.instabackground;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.process.BitmapProcessor;

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
        getWindowManager().getDefaultDisplay().getRectSize(window);
        ImageLoader.getInstance().displayImage(
                getIntent().getExtras().getString("url")
                , imageView, new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.noimage)
                        .showImageOnFail(R.drawable.noimage)
                        .showImageOnLoading(R.drawable.noimage)
                        .cacheInMemory(true)
                        .cacheOnDisk(true)
                        .postProcessor(new BitmapProcessor() {
                            @Override
                            public Bitmap process(Bitmap bitmap) {
                                return Bitmap.createScaledBitmap(bitmap,window.width(),window.width(),false);
                            }
                        })
                        .build());
    }
}
