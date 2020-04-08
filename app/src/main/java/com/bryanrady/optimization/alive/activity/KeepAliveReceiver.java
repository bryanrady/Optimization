package com.bryanrady.optimization.alive.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

/**
 * @author: wangqingbin
 * @date: 2020/4/8 11:43
 */
public class KeepAliveReceiver extends BroadcastReceiver {

    private static final String TAG = "KeepAliveReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(TAG, "receive:" + intent.getAction());
        if (TextUtils.equals(Intent.ACTION_SCREEN_ON, intent.getAction())){
            KeepAliveManager.getInstance().finishKeepAliveActivity();
        }else if(TextUtils.equals(Intent.ACTION_SCREEN_OFF, intent.getAction())){
            KeepAliveManager.getInstance().startKeepAliveActivity(context);
        }
    }
}
