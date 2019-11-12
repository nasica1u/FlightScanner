package com.christophenasica.flyscanner.core;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;

public class ApplicationManager extends Application {

    private static Context mAppContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppContext = getApplicationContext();
    }

    public static Context getAppContext() {
        return mAppContext;
    }

    public static Resources getAppResources() {
        return mAppContext.getResources();
    }

    public static AssetManager getAppAssets() {
        return mAppContext.getAssets();
    }
}
