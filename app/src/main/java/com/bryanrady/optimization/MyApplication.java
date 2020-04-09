package com.bryanrady.optimization;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Process;
import android.text.TextUtils;

import com.bryanrady.optimization.advertisement.SharePreferenceManager;
import com.bryanrady.optimization.battery.location.LocationService;
import com.danikula.videocache.HttpProxyCacheServer;
import com.squareup.leakcanary.LeakCanary;

import java.util.List;

/**
 * @author: wangqingbin
 * @date: 2020/1/3 10:57
 */
public class MyApplication extends Application {

    public static MyApplication mContext = null;
    private HttpProxyCacheServer proxy;

    private Intent location;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        initThirdSdk();

//        if (!TextUtils.equals(BuildConfig.APPLICATION_ID + ":location", getProcessName(Process.myPid()))) {
//            location = new Intent(this, LocationService.class);
//            startService(location);
//        }
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

    public Intent getLocation() {
        return location;
    }

    String getProcessName(int pid) {
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
            if (procInfo.pid == pid) {
                return procInfo.processName;
            }
        }
        return null;
    }

}
