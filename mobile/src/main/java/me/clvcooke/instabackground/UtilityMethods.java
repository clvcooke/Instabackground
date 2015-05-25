package me.clvcooke.instabackground;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Colin on 2014-12-29.
 */
public class UtilityMethods {

    public static final String IMAGE_FILE_NAME_PREFIX = "instagram-photo-";
    public static final String COUNT_STRING = "ImageCount";
    public static final String IMAGE_DIR = "imageDir";
    public static final String IMAGE_SEARCH_STRING = "standard_resolution";
    public static final String DIRECTORY_PREFIX = "/instabackground/";

    public static String getPageTitle(String url) throws IOException{
        Document document = Jsoup.connect(url).timeout(3000).userAgent("Mozilla/17.0").get();
        return document.title();
    }

    public static void setWallpaper(Context context, Bitmap bitmap){
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
        try{
            //TODO add resized bitmap here (maybe, look it up)
            wallpaperManager.setBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //TODO add javadocs
    public static List<String> getURLS(String pageURL,int amount) throws IOException {
        List<String> urls = new ArrayList<String>();
        String megaString  = Jsoup.connect(pageURL).timeout(3000).userAgent("Mozilla/17.0").get().toString();
        int prev = 0;
        //TODO this depends heavily on instagrams page format, lets just hope it doesn't break
        for(int i = 0; i < amount; i++){
            prev = megaString.indexOf(IMAGE_SEARCH_STRING,prev) + IMAGE_SEARCH_STRING.length() + 10;
            urls.add(megaString.substring(prev, megaString.indexOf(",",prev) -1).replace(String.valueOf('\\'), ""));
        }
        return urls;
    }

    //TODO add javadocs
    public static File[] getSavedFiles(String relativePath){
        File[] files = null;
        File direct = new File(Environment.getExternalStorageDirectory() + relativePath);
        if(!direct.exists()){
            return files;
        }
        files = direct.listFiles();
        return files;
    }




}
