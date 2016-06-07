package com.fanbeat.sdk.android;

import android.app.Application;

/**
 * Created by tony on 6/7/16.
 */
public class FanBeatApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        FanBeat.getInstance(this);
    }
}