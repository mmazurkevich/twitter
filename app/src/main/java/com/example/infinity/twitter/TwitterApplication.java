package com.example.infinity.twitter;

import android.app.Application;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import io.fabric.sdk.android.Fabric;

/**
 * Created by infinity on 24.09.15.
 */
public class TwitterApplication extends Application {

    private static final String CONSUMER_KEY = "hbKjRjrmy28xNKEi3XmSnk9Dx";
    private static final String CONSUMER_SECRET = "SHHUkx6KFApgivtfzIS4HaOYKbmt74UBGLVi293HEOOOKqNOH8";

    @Override
    public void onCreate() {
        super.onCreate();
        TwitterAuthConfig authConfig =  new TwitterAuthConfig(CONSUMER_KEY, CONSUMER_SECRET);
        Fabric.with(this, new Twitter(authConfig));

    }
}
