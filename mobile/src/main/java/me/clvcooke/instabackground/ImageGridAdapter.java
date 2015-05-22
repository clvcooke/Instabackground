package me.clvcooke.instabackground;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Colin on 2015-05-14.
 */
public class ImageGridAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> mUrls;
    private DisplayImageOptions options;
    private LayoutInflater mInflator;
    private List<Boolean> isToggled;
    private String user;

    public ImageGridAdapter(Context c) {
        mContext = c;
        mInflator = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        options = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.noimage)
                .showImageOnFail(R.drawable.noimage)
                .showImageOnLoading(R.drawable.noimage)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();


    }

    public void setUrls(List<String> urls, String username) {
        mUrls = urls;
        isToggled = new ArrayList<>();
        for (int i = 0; i < mUrls.size(); i++) {
            isToggled.add(false);
        }
        user = username;
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
        boolean visible = isToggled.get(position);
        ImageView overlay = (ImageView) view.findViewById(R.id.overlay);
        overlay.setVisibility(visible ? View.INVISIBLE : View.VISIBLE);
        isToggled.set(position, overlay.getVisibility() == View.VISIBLE);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ImageView image;
        final FrameLayout layout;
        if (convertView == null) {
            layout = (FrameLayout) mInflator.inflate(R.layout.image_view_overlay, null);
            image = (ImageView) layout.findViewById(R.id.image);
            image.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            image.setPadding(2, 2, 2, 2);
        } else {
            layout = (FrameLayout) convertView;
            image = (ImageView) layout.findViewById(R.id.image);
        }

        layout.findViewById(R.id.overlay).setVisibility(isToggled.get(position) ? View.VISIBLE : View.INVISIBLE);
        image.setAdjustViewBounds(true);
        ImageLoader.getInstance().displayImage((String) getItem(position), image, options);
        return layout;
    }

    public void saveSelected() {

        String date = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss").format(new Date());
        String directory = "/instabackground/"  + user;
        for (int i = 0; i < mUrls.size(); i++) {
            if (isToggled.get(i)) {
                String url = mUrls.get(i);
                File direct = new File(Environment.getExternalStorageDirectory() + directory);

                if (!direct.exists()) {
                    direct.mkdirs();
                }

                DownloadManager mgr = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
                Uri downloadUri = Uri.parse(url);
                DownloadManager.Request request = new DownloadManager.Request(
                        downloadUri);

                request.setAllowedNetworkTypes(
                        DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                        .setAllowedOverRoaming(false).setTitle("Instabackground image download")
                        .setDescription("Downloading photos")
                        .setDestinationInExternalPublicDir(directory, date + Integer.toString(i) + ".jpg");

                mgr.enqueue(request);
            }
        }

    }
}
