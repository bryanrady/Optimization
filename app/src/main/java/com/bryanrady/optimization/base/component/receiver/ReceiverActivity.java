package com.bryanrady.optimization.base.component.receiver;

import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.bryanrady.optimization.R;
import com.bryanrady.optimization.leaked.FixLeakedUtils;

import androidx.appcompat.app.AppCompatActivity;

public class ReceiverActivity extends AppCompatActivity {

    public static final String STATIC_ACTION = "static_action";
    public static final String DYNAMIC_ACTION = "dynamic_action";

    private DynamicBroadcastReceiver mDynamicReceiver;
    private MyBroadcastReceiver mMyReceiver;
    private MyBroadcastReceiver2 mMyReceiver2;
    private PermissionBroadcastReceiver mPermissionReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver);

        mDynamicReceiver = new DynamicBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(DYNAMIC_ACTION);
        registerReceiver(mDynamicReceiver, filter);

        mMyReceiver = new MyBroadcastReceiver();
        IntentFilter filter2 = new IntentFilter();
        filter2.addAction(DYNAMIC_ACTION);
        filter2.setPriority(500);
        registerReceiver(mMyReceiver, filter2);

        mMyReceiver2 = new MyBroadcastReceiver2();
        IntentFilter filter3 = new IntentFilter();
        filter3.addAction(DYNAMIC_ACTION);
        filter3.setPriority(999);
        registerReceiver(mMyReceiver2, filter3);

        mPermissionReceiver = new PermissionBroadcastReceiver();
        IntentFilter filter4 = new IntentFilter();
        filter4.addAction("custom_action");
        registerReceiver(mPermissionReceiver, filter4,
                "com.bryanrady.custom.BroadcastReceiver", null);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //华为手机引发的泄漏
        FixLeakedUtils.fixInputMethodManagerLastSrvView(this);
        FixLeakedUtils.fixInputMethodManagerLeak(this);

        if (mDynamicReceiver != null){
            unregisterReceiver(mDynamicReceiver);
        }
        if (mMyReceiver != null){
            unregisterReceiver(mMyReceiver);
        }
        if (mMyReceiver2 != null){
            unregisterReceiver(mMyReceiver2);
        }
        if (mPermissionReceiver != null){
            unregisterReceiver(mPermissionReceiver);
        }
    }

    public void static_register(View view) {
        //https://www.jianshu.com/p/5283ebc225d5?utm_source=oschina-app
        //在Android8.0上突破隐式广播的限制
        Intent intent = new Intent();
        intent.setAction(STATIC_ACTION);
        intent.putExtra("data","这是ReceiverActivity页面发送的静态注册的无序广播");
        //W/BroadcastQueue: Background execution not allowed: receiving Intent { act=static_action flg=0x80010 (has extras) } to com.bryanrady.optimization/.base.component.receiver.StaticBroadcastReceiver
        //下面这3个方案都可以
//        intent.setComponent(new ComponentName("com.bryanrady.optimization",
//                "com.bryanrady.optimization.base.component.receiver.StaticBroadcastReceiver"));
        //这个是Intent里面的一个常量，带@hide注解的，所以不能直接引用这个常量
        //public static final int FLAG_RECEIVER_INCLUDE_BACKGROUND = 0x01000000;
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            intent.addFlags(0x01000000);
//        }
        intent.setPackage(getPackageName());
        sendBroadcast(intent);

//https://blog.csdn.net/fengyulinde/article/details/80757986    Android O行为变更--隐式广播限制
//        解决方式：
//        除了例外情况外，可通过以下方式解决：
//        1.改为带签名权限的广播。因为这些广播只会发送到使用相同证书签名的应用，而不是发送到设备上的所有应用。
//              https://blog.csdn.net/u010410408/article/details/42082977     BroadcastReceiver与自定义权限

//        2.动态注册而不是在清单文件中注册。

    }

    public void dynamic_register(View view) {
        Intent intent = new Intent();
        intent.setAction(DYNAMIC_ACTION);
        intent.putExtra("data","这是ReceiverActivity页面发送的动态注册的无序广播");
        sendBroadcast(intent);
    }

    public void static_order_register(View view) {
        Intent intent = new Intent();
        intent.setAction(STATIC_ACTION);
        intent.putExtra("data", "这是ReceiverActivity页面发送的静态注册的有序广播");
        intent.setPackage(getPackageName());
        sendOrderedBroadcast(intent, null);
    }

    public void dynamic_order_register(View view) {
        Intent intent = new Intent();
        intent.setAction(DYNAMIC_ACTION);
        intent.putExtra("data", "这是ReceiverActivity页面发送的动态注册的有序广播");
        sendOrderedBroadcast(intent, null);
    }

    public void send_permission(View view) {
        Intent intent = new Intent();
        intent.setAction("custom_action");
        intent.putExtra("data", "这是ReceiverActivity页面发送的动态注册的带有权限的无序广播");
        sendBroadcast(intent, "com.bryanrady.custom.BroadcastReceiver");
    }
}
