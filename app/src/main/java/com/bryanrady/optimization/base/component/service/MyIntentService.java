package com.bryanrady.optimization.base.component.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.bryanrady.optimization.base.component.ComponentActivity;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

/**
 * @author: wangqingbin
 * @date: 2020/4/8 9:56
 */
public class MyIntentService extends IntentService {

    private LocalBroadcastManager mLocalBroadcastManager;

    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("wangqingbin","线程开始运行");
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d("wangqingbin","线程正在运行中");
        try {
            //模拟耗时操作
            Thread.sleep(5000);

            Intent broadcastIntent = new Intent(ComponentActivity.ACTION_INTENT_SERVICE);
            broadcastIntent.putExtra("status","线程执行完成");
            mLocalBroadcastManager.sendBroadcast(broadcastIntent);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("wangqingbin","线程结束运行");
    }
}
