package com.bryanrady.optimization.alive.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.lang.ref.PhantomReference;
import java.lang.ref.WeakReference;

/**
 * @author: wangqingbin
 * @date: 2020/4/8 11:39
 */
public class KeepAliveManager {

    private static final KeepAliveManager ourInstance = new KeepAliveManager();

    public static KeepAliveManager getInstance() {
        return ourInstance;
    }

    private KeepAliveManager() {
    }

    private WeakReference<Activity> mKeepAliveAct;
    private KeepAliveReceiver mKeepAliveReceiver;

    /**
     * 注册开屏、息屏广播
     * @param context
     */
    public void registerKeepAliveReceiver(Context context){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        mKeepAliveReceiver = new KeepAliveReceiver();
        context.registerReceiver(mKeepAliveReceiver,intentFilter);
    }

    public void unregisterKeepAliveReceiver(Context context){
        if(mKeepAliveReceiver != null){
            context.unregisterReceiver(mKeepAliveReceiver);
        }
    }

    public void setKeepAlive(KeepAliveActivity activity){
        mKeepAliveAct = new WeakReference<Activity>(activity);
    }

    public void startKeepAliveActivity(Context context){
        Intent intent = new Intent(context, KeepAliveActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void finishKeepAliveActivity(){
        if (mKeepAliveAct != null){
            Activity activity = mKeepAliveAct.get();
            if(activity != null){
                activity.finish();
            }
            mKeepAliveAct = null;
        }
    }

}
