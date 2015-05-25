package me.clvcooke.instabackground.Adapters;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.clvcooke.instabackground.FullScreenPhotoActivity;
import me.clvcooke.instabackground.R;
import me.clvcooke.instabackground.UserPhotoGrid;
import me.clvcooke.instabackground.UtilityMethods;

/**
 * Created by Colin on 2015-05-24.
 */
public class GalleryGridAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> mUrls;
    private List<String> users;
    private DisplayImageOptions options;
    private LayoutInflater mInflator;
    private List<Boolean> isToggled;


    public GalleryGridAdapter(Context c) {
        mContext = c;
        mInflator = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        options = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.noimage)
                .showImageOnFail(R.drawable.noimage)
                .showImageOnLoading(R.drawable.noimage)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();


    }

    public void setUrls(List<String> urls, List<String> usernames) {
        mUrls = urls;
        users = usernames;
        isToggled = new ArrayList<>();
        for (int i = 0; i < mUrls.size(); i++) {
            isToggled.add(false);
        }
    }

    @Override
    public int getCount() {
        if (mUrls != null) {
            return mUrls.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (mUrls == null || position >= mUrls.size()) {
            return null;
        }
        return mUrls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void onItemClick(View view, int position) {
        Intent intent = new Intent(mContext, UserPhotoGrid.class);
        intent.putExtra("user",users.get(position));
        mContext.startActivity(intent);
    }

    public void onItemLongClick(int position){

        //TODO options menu
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ImageView image;
        final FrameLayout layout;
        if (convertView == null) {
            layout = (FrameLayout) mInflator.inflate(R.layout.gallary_image_view, null);
            image = (ImageView) layout.findViewById(R.id.image);
            image.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            image.setPadding(3, 3, 3, 3);
        } else {
            layout = (FrameLayout) convertView;
            image = (ImageView) layout.findViewById(R.id.image);
        }
        ((TextView)layout.findViewById(R.id.text)).setText(users.get(position));
        image.setAdjustViewBounds(true);
        ImageLoader.getInstance().displayImage((String) getItem(position), image, options);
        return layout;
    }


}


