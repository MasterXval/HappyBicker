package com.happybiker;

import android.app.Application;

/**
 * Created by rsaury on 28/09/2014.
 */
public class HappyBickerApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Help.setAppCtx(getApplicationContext());
    }
}
