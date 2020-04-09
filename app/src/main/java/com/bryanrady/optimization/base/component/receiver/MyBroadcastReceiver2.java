package com.bryanrady.optimization.base.component.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * @author: wangqingbin
 * @date: 2020/4/9 10:26
 */
public class MyBroadcastReceiver2 extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("MyBroadcastReceiver2", "receive data : " + intent.getStringExtra("data"));
        //截断广播
        abortBroadcast();

        /**
         *
         * 不管是动态注册还是静态注册,都是一样的结果。
         *
         * MyBroadcastReceiver2优先级为999，MyBroadcastReceiver优先级500，所以MyBroadcastReceiver2会先收到广播。
         *
         *      如果是发送无序广播，两个广播接收者都会有消息，但是会出现错误报告！告诉你无序广播不能截断。
         *              BroadcastReceiver trying to return result during a non-ordered broadcast
         *
         *      如果是发送有序广播，那么低优先级的广播不能接收广播数据。因为这里使用了拦截的方法！
         *
         *  无序广播不能截断，有序广播能被高优先级的广播接收者截断。不管是对静态的广播接收者还是对动态的广播接收者都是一样的。
         *
         */
    }

}
