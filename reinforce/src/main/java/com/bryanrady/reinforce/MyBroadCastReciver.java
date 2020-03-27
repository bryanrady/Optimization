package com.bryanrady.reinforce;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * @author Lance
 * @date 2017/12/28
 */

public class MyBroadCastReciver extends BroadcastReceiver {

    public static final String TAG = "MyBroadCastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(TAG, "reciver:" + context);
        Log.e(TAG, "reciver:" + context.getApplicationContext());
        Log.e(TAG, "reciver:" + context.getApplicationInfo().className);

        /**
         *
         * 这里面是不能同过onReceive(Context context, Intent intent) 里面的context参数来做一些操作的。
         * 因为这个contxt是经过封装了的是ReceiverRestrictedContext，所以说在onReceive()方法里面通过context来
         * 调用registerReceiver（）  bindService （） 这两个方法都会报错的。
         */

    }
}
