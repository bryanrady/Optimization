package com.bryanrady.optimization.battery.location;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.SystemClock;
import android.text.TextUtils;

import com.bryanrady.optimization.battery.JobManager;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;


public class LocationService extends Service {

    private PowerManager.WakeLock locationLock;
    private Intent alarmIntent;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate() {
        super.onCreate();
        JobManager.getInstance().init(this);
        LocationManager.getInstance().startLocation(this);
        //使用WeakLock
//        PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
        //判断是否支持
//        pm.isWakeLockLevelSupported(PowerManager.PARTIAL_WAKE_LOCK);
        //只唤醒cpu
//        locationLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
//                "location_lock");
//        locationLock.acquire();

        alarmKeep();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //释放
        LocationManager.getInstance().destoryLocation();
        //注销广播接收者
        unregisterReceiver(alarmReceiver);
//        if (null != locationLock) {
//            locationLock.release();
//        }
    }

    private void alarmKeep() {
        alarmIntent = new Intent();
        alarmIntent.setAction("LOCATION");
        //创建延迟意图
        PendingIntent broadcast = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
        //获得闹钟管理器
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        //动态注册广播接受者
        IntentFilter filter = new IntentFilter();
        filter.addAction("LOCATION");
        registerReceiver(alarmReceiver,filter);
        //设置一个 每隔 5s 发送一个广播
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,SystemClock.elapsedRealtime(),
                5_000,broadcast);
    }

    BroadcastReceiver alarmReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (TextUtils.equals(intent.getAction(),"LOCATION")){
                LocationManager.getInstance().startLocation(LocationService.this);
            }
        }
    };


}
