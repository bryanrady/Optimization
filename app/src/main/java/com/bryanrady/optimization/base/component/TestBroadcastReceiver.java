package com.bryanrady.optimization.base.component;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * @author: wangqingbin
 * @date: 2020/4/8 15:41
 */
public class TestBroadcastReceiver extends BroadcastReceiver {

    public static final String TEST_ACTION = "test_action";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.e("wangqingbin","receive action: " + action);
        if (TEST_ACTION.equals(action)){
            String test = intent.getStringExtra("test");
            Log.e("wangqingbin","receive test: " + test);
        }
    }

}
