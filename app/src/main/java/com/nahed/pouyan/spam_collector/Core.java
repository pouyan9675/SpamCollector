package com.nahed.pouyan.spam_collector;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Environment;

import java.io.File;


/**
 * Created by pouyan on 3/15/18.
 */

public class Core extends Application {

    private static Core instance;
    public static Core getInstance() {
        return instance;
    }

    private Typeface applicationFont;
    private Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        mContext = getApplicationContext();
        applicationFont = Typeface.createFromAsset(getAssets(), "font.ttf");

    }

    public Typeface getApplicationFont(){
        return applicationFont;
    }

    public Context getContext() {
        return mContext;
    }
}
