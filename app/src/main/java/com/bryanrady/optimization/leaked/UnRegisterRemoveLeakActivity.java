package com.bryanrady.optimization.leaked;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraManager;
import android.hardware.display.DisplayManager;
import android.media.AudioDeviceCallback;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.telephony.TelephonyManager;
import android.widget.TextView;
import android.widget.Toast;

import com.bryanrady.optimization.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * 广播、系统服务回调接口、观察者未取消注册监听或回调的造成的内存泄漏
 * @author: wangqingbin
 * @date: 2020/1/6 17:01
 */
public class UnRegisterRemoveLeakActivity extends AppCompatActivity implements
        ClipboardManager.OnPrimaryClipChangedListener, SensorEventListener {

    private static final String ACTION_BD = "com.bryanrady";
    private MyBroadcastReceiver myBroadcastReceiver;

    private ClipboardManager clipboardManager;
    private SensorManager sensorManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaked_model);

        TextView tv = findViewById(R.id.tv_prompt);
        tv.setText("广播、系统服务回调接口、观察者未取消注册监听或回调的造成的内存泄漏");

        myBroadcastReceiver = new MyBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_BD);
        this.registerReceiver(myBroadcastReceiver, intentFilter);


//        Activity中动态注册的BroadcastReceiver与Activity的引用持有关系
//
//        对于Android中的广播机制，可以先参考文章：《Android总结篇系列：Android广播机制》
//
//        Activity中动态注册的广播接收器，一般性写法都是此Activity中持有创建的广播接收器的对象引用，并指明广播接收器对应的接收广播类型（IntentFilter）。
//
//        Activity中调用registerReceiver(mBroadcastReceiver, intentFilter)方法进行广播接收器的注册。此时，通过Binder机制向AMS(Activity Manager Service)进行注册。
//
//        AMS会对应的记录Activity上下文、广播接收器以及对应的IntentFilter等内容，并形成类似于消息的发布-订阅存储模式与结构。
//
//        当对应的广播发出时，在定义的广播接收器的onReceive(context, intent)方法回调中，对于Activity中动态注册的广播接收器，
//
//        onReceive方法回调中的context指的是Activity Context！也就是说，Activity与mBroadcastReceiver此时实际上是通过AMS相互持有强引用的。
//
//        因此，对于Activity中动态注册的广播接收器，一定要在对应的声明周期回调方法中去unregisterReceiver，以斩断此关联。
//
//        否则，就会出现当前Activity的内存泄露。

        //发送广播
        Intent intent = new Intent(ACTION_BD);
        sendBroadcast(intent);


        /**
         * https://blog.csdn.net/xiabing082/article/details/53993298    Android内存泄漏：谨慎使用getSystemService
         *
         * 通过context获取系统服务出现的内存泄漏问题
         *
         *      原理：当使用context.getSystemService(int name)方法，获取系统服务时候，这个服务处于系统进程中一直存在，
         *            如果传入Activity引用，会导致也会导致内存泄漏问题。
         */

        //剪贴板服务
    //    clipboardManager = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
        //ClipboardManager持有Activity的引用造成Activity无法回收，造成内存泄漏
        clipboardManager = (ClipboardManager) getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.addPrimaryClipChangedListener(this);

        //传感器服务
        sensorManager = (SensorManager) getApplicationContext().getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ALL);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    public void onPrimaryClipChanged() {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private static class MyBroadcastReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(ACTION_BD.equals(action)){
                Toast.makeText(context,"收到了广播",Toast.LENGTH_SHORT).show();
            }
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //华为手机引发的泄漏
        FixLeakedUtils.fixInputMethodManagerLastSrvView(this);
        FixLeakedUtils.fixInputMethodManagerLeak(this);

        //反注册
        this.unregisterReceiver(myBroadcastReceiver);
        clipboardManager.removePrimaryClipChangedListener(this);
        sensorManager.unregisterListener(this);
    }
}
