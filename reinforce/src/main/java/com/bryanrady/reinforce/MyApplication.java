package com.bryanrady.reinforce;

import android.app.Application;
import android.util.Log;

/**
 * @author: wangqingbin
 * @date: 2020/3/26 16:37
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("wangqingbin","MyApplication onCreate...........");
    }
}
