package me.clvcooke.instabackground;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.IOException;

/**
 * Created by Colin on 2014-12-29.
 */
public class UtilityMethods {

    public static String getPageTitle(String url) throws IOException{
        Document document = Jsoup.connect(url).get();
        return document.title();
    }

}
