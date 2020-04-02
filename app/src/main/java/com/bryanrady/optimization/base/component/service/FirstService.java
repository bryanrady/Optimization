package com.bryanrady.optimization.base.component.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

/**
 * @author: wangqingbin
 * @date: 2020/4/2 10:28
 */
public class FirstService extends Service {

    public class FirstBinder extends Binder{

        public void callService() {
            Log.d("wangqingbin","Service callService........");
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("wangqingbin","Service onCreate........");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("wangqingbin","Service onStartCommand........");
        String key = intent.getStringExtra("key");
        Log.d("wangqingbin","key == " + key);
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("wangqingbin","Service onBind........");
        String key = intent.getStringExtra("key");
        Log.d("wangqingbin","key222 == " + key);
        return new FirstBinder();
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        Log.d("wangqingbin","Service onRebind........");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d("wangqingbin","Service onUnbind........");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("wangqingbin","Service onDestroy........");
    }

    //startService()方式启动
//    2020-04-02 10:48:14.144 32095-32095/com.bryanrady.optimization D/wangqingbin: Service onCreate........
//    2020-04-02 10:48:14.145 32095-32095/com.bryanrady.optimization D/wangqingbin: Service onStartCommand........
    //stopService()销毁
//    2020-04-02 10:48:16.784 32095-32095/com.bryanrady.optimization D/wangqingbin: Service onDestroy........

    //bindService()绑定
//    2020-04-02 11:00:43.906 5320-5320/com.bryanrady.optimization D/wangqingbin: Service onCreate........
//    2020-04-02 11:00:43.906 5320-5320/com.bryanrady.optimization D/wangqingbin: Service onBind........
    //unbindService()解绑或者直接退出调用者Activity
//    2020-04-02 11:00:50.905 5320-5320/com.bryanrady.optimization D/wangqingbin: Service onUnbind........
//    2020-04-02 11:00:50.905 5320-5320/com.bryanrady.optimization D/wangqingbin: Service onDestroy........
}
