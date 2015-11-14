package me.clvcooke.instabackground.Utilities;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
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
            int next = megaString.indexOf(",",prev) -2;

            String url;
            if(prev > 0 && next >prev){
                url = megaString.substring(prev, next).replace(String.valueOf('\\'), "");
            }else{
                return urls;
            }

            //This is a fuzzy scraper, its too much effort to fine tune to get exact URLs so there
            //is a bit of self correction


            boolean add = true;
            //first lets check if it ends with the right extension
            if(url.endsWith("jp")){
                url = url.concat("g");
            }else if(url.endsWith("j")){
                url = url.concat("pg");
            }else if(url.endsWith(".")){
                url = url.concat("jpg");
            }else if(!url.endsWith(".jpg")){
                //so it doesn't have the right extension, last guess is that
                //it has some gunk at the end, get rid of that
                if(url.contains(".jpg")){
                    int end = url.indexOf(".jpg") + 4;
                    url = url.substring(0, end);
                }else{
                    //this thing is beyond saving
                    add = false;
                }
            }

            if(add && !urls.contains(url)){
                urls.add(url);
            }

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
