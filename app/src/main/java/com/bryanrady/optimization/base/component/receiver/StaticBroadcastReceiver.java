package com.bryanrady.optimization.base.component.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * @author: wangqingbin
 * @date: 2020/4/8 15:41
 */
public class StaticBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.e("StaticBroadcastReceiver","receive action: " + action);
        if (ReceiverActivity.STATIC_ACTION.equals(action)){
            String data = intent.getStringExtra("data");
            Log.e("StaticBroadcastReceiver","receive data: " + data);
        }
    }

}
