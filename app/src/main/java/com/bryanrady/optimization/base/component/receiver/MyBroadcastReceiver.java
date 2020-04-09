package com.bryanrady.optimization.base.component.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * @author: wangqingbin
 * @date: 2020/4/9 10:26
 */
public class MyBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("MyBroadcastReceiver", "receive data : " + intent.getStringExtra("data"));
    }

}
