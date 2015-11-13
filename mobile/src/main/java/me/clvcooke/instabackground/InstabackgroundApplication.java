package me.clvcooke.instabackground;

import android.app.Application;

import com.taplytics.sdk.Taplytics;

/**
 * Created by Colin on 11/12/2015.
 */
public class InstabackgroundApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Taplytics.startTaplytics(this, "API KEY");
    }


}
