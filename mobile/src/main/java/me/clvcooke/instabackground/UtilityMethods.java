package me.clvcooke.instabackground;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.SystemClock;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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

    public static String getPageTitle(String url) throws IOException{
        Document document = Jsoup.connect(url).timeout(3000).userAgent("Mozilla/17.0").get();
        return document.title();
    }


    //for now this just fetches the first x images in the feed
    public static List<String> getImageURLSFromRSS(String url, int amount) throws IOException{

        List<String> urls = new ArrayList<String>();
        Document document = Jsoup.connect(url).get();
        Element channel = document.select("rss").first().select("channel").first();

        int pictureMax = channel.select("item").size();
        for(int i = 0; i < amount&&i < pictureMax; i++){
            try{
                urls.add(channel.select("item").get(i).select("url").first().text());
            }catch(Exception e){
                e.printStackTrace();
                break;
            }
        }

        return urls;
    }


    public static void setWallpaper(Context context, Bitmap bitmap){

        WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);

        try{
            wallpaperManager.setBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    public static List<String> getURLS(String pageURL,int amount) throws IOException {

        List<String> urls = new ArrayList<String>();

        Document document = Jsoup.connect(pageURL).timeout(3000).userAgent("Mozilla/17.0").get();

        String megaString = document.toString();

        int prev = 0;

        //TODO more efficient?
        for(int i = 0; i < amount; i++){
            prev = megaString.indexOf("alt_media_url",prev+1);
            int start = megaString.indexOf("http",prev);
            int end = megaString.indexOf(",",start) -1;

            String minorLink = megaString.substring(start,end).replace(String.valueOf('\\'),"");

            Document imagePage = Jsoup.connect(minorLink).timeout(3000).userAgent("Mozilla/17.0").get();
            String megaString2 = imagePage.toString();


            start = megaString2.indexOf("og:image");
            start = megaString2.indexOf("=", start) +2;
            end = megaString2.indexOf(".jpg",start)+4;
            urls.add(megaString2.substring(start,end));

        }

        System.out.println(Integer.toString(urls.size()));

        return urls;
    }





}
