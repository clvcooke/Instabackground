package me.clvcooke.instabackground;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Colin on 2014-12-29.
 */
public class UtilityMethods {

    public static String getPageTitle(String url) throws IOException{
        Document document = Jsoup.connect(url).get();
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





}
