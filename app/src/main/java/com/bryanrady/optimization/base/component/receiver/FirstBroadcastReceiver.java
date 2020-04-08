package com.bryanrady.optimization.base.component.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * @author: wangqingbin
 * @date: 2020/4/8 15:41
 */
public class FirstBroadcastReceiver extends BroadcastReceiver {

    public static final String FIRST_ACTION = "first_action";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.e("wangqingbin","receive action: " + action);
        if (FIRST_ACTION.equals(action)){
            String data = intent.getStringExtra("data");
            Log.e("wangqingbin","receive data: " + data);
        }
    }

}
