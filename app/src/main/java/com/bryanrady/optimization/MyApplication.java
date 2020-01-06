package com.bryanrady.optimization;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * @author: wangqingbin
 * @date: 2020/1/3 10:57
 */
public class MyApplication extends Application {

    public static MyApplication mContext = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        initThirdSdk();
    }

    private void initThirdSdk() {
        if(LeakCanary.isInAnalyzerProcess(this)){
            return;
        }
        LeakCanary.install(this);
    }

    public static MyApplication getContext(){
        return mContext;
    }


}
