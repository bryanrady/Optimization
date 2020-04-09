package com.bryanrady.optimization.base.component.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * @author: wangqingbin
 * @date: 2020/4/9 11:18
 */
public class NonPermissionBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if ("custom_action".equals(intent.getAction())){
            Log.e("NonPermission","接收到了 custom_action 广播 : " + intent.getStringExtra("data"));
        }
    }
}
