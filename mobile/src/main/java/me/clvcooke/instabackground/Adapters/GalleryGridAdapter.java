package me.clvcooke.instabackground.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.DimenRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import me.clvcooke.instabackground.R;

/**
 * Created by Colin on 2015-05-24.
 */
public class GalleryGridAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> mUrls;
    private List<String> users;
    private LayoutInflater mInflator;
    private List<Boolean> isToggled;
    private int width;

    public GalleryGridAdapter(Context c) {
        mContext = c;
        mInflator = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        width = Resources.getSystem().getDisplayMetrics().widthPixels;
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

    public String getUser(int position){
        return users.get(position);
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
        Picasso.with(mContext).load(mUrls.get(position)).resize(width,width).into(image);
        return layout;
    }


}


