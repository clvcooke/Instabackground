package me.clvcooke.instabackground.Adapters;

import android.app.ActionBar;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import me.clvcooke.instabackground.BootReceiver;
import me.clvcooke.instabackground.FullScreenPhotoActivity;
import me.clvcooke.instabackground.R;
import me.clvcooke.instabackground.Utilities.UtilityMethods;

/**
 * Created by Colin on 2015-05-14.
 */
public class ImageGridAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflator;
    private List<ImageHolder> imageHolders;
    private String user;
    private int width;

    public ImageGridAdapter(Context c) {
        mContext = c;
        mInflator = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        width = Resources.getSystem().getDisplayMetrics().widthPixels / 3;
    }


    /**
     * @param urls
     * @param username
     */
    public void loadUrls(HashSet<Integer> downlaoded, ArrayList<String> urls, String username) {
        imageHolders = new ArrayList<>();
        for (int i = 0; i < urls.size(); i++) {
            imageHolders.add(new ImageHolder(urls.get(i), false, downlaoded.contains(i)));
        }
        user = username;

    }

    public void loadUrls(ArrayList<String> urls, String username, HashSet<String> selectedUrls) {
        imageHolders = new ArrayList<>();
        for (String url : urls) {
            imageHolders.add(new ImageHolder(url, selectedUrls != null ? selectedUrls.contains(url) : false, false));
        }
        user = username;
    }

    @Override
    public int getCount() {
        if (imageHolders != null) {
            return imageHolders.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (imageHolders == null || position >= imageHolders.size()) {
            return null;
        }
        return imageHolders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public String onItemClick(View view, int position) {
        imageHolders.get(position).toggle(view);

        return imageHolders.get(position).url;
    }

    public void onItemLongClick(int position) {
        Intent intent = new Intent(mContext, FullScreenPhotoActivity.class);
        intent.putExtra("url", imageHolders.get(position).url);
        mContext.startActivity(intent);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ImageView image;
        final FrameLayout layout;
        if (convertView == null) {
            layout = (FrameLayout) mInflator.inflate(R.layout.image_view_overlay, null);
            image = (ImageView) layout.findViewById(R.id.image);
            image.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            image.setPadding(3, 3, 3, 3);
        } else {
            layout = (FrameLayout) convertView;
        }

        return imageHolders.get(position).getView(layout);
    }

    public void saveSelected() {
        String directory = UtilityMethods.DIRECTORY_PREFIX + user;
        File direct = new File(Environment.getExternalStorageDirectory() + directory);
        if (!direct.exists()) {
            direct.mkdirs();
        }
        DownloadManager mgr = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        for (ImageHolder holder : imageHolders) {
            holder.save(mgr, directory);
        }

        notifyDataSetChanged();

    }

    public boolean minimumSelected() {
        for (ImageHolder holder : imageHolders) {
            if (holder.isToggled) {
                return true;
            }
        }
        return false;
    }

    public void removeSelected() {
        for (int i = 0; i < imageHolders.size(); i++) {
            if (imageHolders.get(i).isToggled) {
                imageHolders.remove(i);
                i--;
            }
        }
        notifyDataSetChanged();
    }

    public void deselectAll() {

        for (ImageHolder holder : imageHolders) {
            holder.isToggled = false;
        }
        notifyDataSetChanged();
    }

    public ArrayList<String> getUrls() {
        ArrayList<String> urls = new ArrayList<>();
        for (ImageHolder holder : imageHolders) {
            urls.add(holder.url);
        }
        return urls;
    }

    private class ImageHolder {
        String url;
        boolean isToggled;
        boolean isDownloaded;

        ImageHolder(String url, boolean isToggled, boolean isDownloaded) {
            this.url = url;
            this.isToggled = isToggled;
            this.isDownloaded = isDownloaded;
        }

        public View getView(FrameLayout layout) {
            layout.findViewById(R.id.overlay).setVisibility(isToggled ? View.VISIBLE : View.INVISIBLE);
            ImageView imageView = (ImageView) layout.findViewById(R.id.image);
            imageView.setMinimumHeight(width);
            imageView.setMinimumWidth(width);
            Picasso.with(mContext).load(url).resize(width, width).centerInside().into(imageView);
            if (isDownloaded) {
                imageView.setColorFilter(Color.argb(100, 191, 191, 191));
                layout.findViewById(R.id.savedIndicator).setVisibility(View.VISIBLE);
            }
            return layout;
        }

        public void toggle(View v) {
            if (!isDownloaded) {
                isToggled = !isToggled;
                ImageView overlay = (ImageView) v.findViewById(R.id.overlay);
                overlay.setVisibility(isToggled ? View.VISIBLE : View.INVISIBLE);
            }
        }

        public void save(DownloadManager mgr, String directory) {
            if (isToggled && !isDownloaded) {
                isDownloaded = true;
                    DownloadManager.Request request = new DownloadManager.Request(
                            Uri.parse(url));
                    request.setAllowedNetworkTypes(
                            DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                            .setAllowedOverRoaming(false).setTitle("Instabackground image download")
                            .setDescription("Downloading photos")
                                    //basic way to add the url to the filename. Making another assumption that the last
                                    //28 characters are unique enough
                            .setDestinationInExternalPublicDir(directory, url.substring(url.length() - 32));
                    mgr.enqueue(request);
                isToggled = false;

            }
        }
    }
}
