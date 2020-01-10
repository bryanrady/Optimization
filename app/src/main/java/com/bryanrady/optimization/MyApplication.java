package com.bryanrady.optimization;

import android.app.Application;
import android.content.Context;

import com.bryanrady.optimization.advertisement.SharePreferenceManager;
import com.danikula.videocache.HttpProxyCacheServer;
import com.squareup.leakcanary.LeakCanary;

/**
 * @author: wangqingbin
 * @date: 2020/1/3 10:57
 */
public class MyApplication extends Application {

    public static MyApplication mContext = null;
    private HttpProxyCacheServer proxy;

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

        SharePreferenceManager.init(this,"refreshTime");
    }

    public static MyApplication getContext(){
        return mContext;
    }

    private HttpProxyCacheServer newProxy() {
        return new HttpProxyCacheServer.Builder(this)
                .maxCacheSize(1024 * 1024 * 1024)       // 1 Gb for cache
                .maxCacheFilesCount(20)
                .build();
    }

    public static HttpProxyCacheServer getProxy(Context context) {
        MyApplication app = (MyApplication) context.getApplicationContext();
        return app.proxy == null ? (app.proxy = app.newProxy()) : app.proxy;
    }


}
