package com.example.savior.movieapp2;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by savior on 7/23/2017.
 */

public class Debugging extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);

    }
}
