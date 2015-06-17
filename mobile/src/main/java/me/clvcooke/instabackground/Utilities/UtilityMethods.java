package me.clvcooke.instabackground.Utilities;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

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
    public static final String IMAGE_SEARCH_STRING = "display_src";
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
    public static ArrayList<String> getURLS(String pageURL,int amount) throws IOException {
        ArrayList<String> urls = new ArrayList<String>();
        String megaString  = Jsoup.connect(pageURL).timeout(3000).userAgent("Mozilla/17.0").get().toString();
        int prev = 0;
        prev = megaString.indexOf("nodes\":[{\"");

        //TODO this depends heavily on instagrams page format, lets just hope it doesn't break. Fuck it broke
        for(int i = 0; i < amount; i++){
            prev = megaString.indexOf(IMAGE_SEARCH_STRING,prev) + IMAGE_SEARCH_STRING.length()+3;
            urls.add(megaString.substring(prev, megaString.indexOf(",",prev) -2).replace(String.valueOf('\\'), ""));
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

    public static File getSavedFile(String filePath){
        return new File(Environment.getExternalStorageDirectory() + filePath);
    }

    public static void saveFiles(List<String> urls){
        for(String url: urls){

        }
    }




}
