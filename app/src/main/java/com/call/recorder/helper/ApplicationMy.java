package com.call.recorder.helper;

import android.app.Application;

import com.google.android.gms.ads.MobileAds;

/**
 * Created by Mohammad
 * on 10/4/2018 3:54 PM.
 */
public class ApplicationMy extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MobileAds.initialize(this, "ca-app-pub-8315069469881577~6351703167");

    }
}
