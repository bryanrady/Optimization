package com.bryanrady.tinker;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

public class TinkerApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        MultiDex.install(base);
        TinkerManager.fix(base);
        super.attachBaseContext(base);
    }
}
