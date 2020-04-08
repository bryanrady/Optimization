package com.bryanrady.optimization.base.component;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import com.bryanrady.optimization.R;
import com.bryanrady.optimization.base.component.activity.FirstActivity;
import com.bryanrady.optimization.base.component.service.FirstService;
import com.bryanrady.optimization.base.component.service.ForegroundService;
import com.bryanrady.optimization.base.component.service.MyIntentService;
import com.bryanrady.optimization.leaked.FixLeakedUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

/**
 * @author: wangqingbin
 * @date: 2020/4/2 10:31
 */
public class ComponentActivity extends AppCompatActivity {

    private Intent mServiceIntent;
    private Intent mForegroundServiceIntent;
    private FirstService.FirstBinder mFirstBinder;
    private FirstService.IService mIService;

    public static final String ACTION_INTENT_SERVICE = "action_intent_service";
    private LocalBroadcastManager mLocalBroadcastManager;
    private MyBroadcastReceiver mBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_component);
        
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
        mBroadcastReceiver = new MyBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_INTENT_SERVICE);
        mLocalBroadcastManager.registerReceiver(mBroadcastReceiver, intentFilter);
        
    }

    public void activity(View view) {
        Intent intent = new Intent(this, FirstActivity.class);
        startActivity(intent);
    }

    private class MyBroadcastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            if (ACTION_INTENT_SERVICE.equals(intent.getAction())){
                String status = intent.getStringExtra("status");
                Log.d("wangqingbin","status=="+status);
            }
        }
    }

    private void testConn(){
        if (mFirstBinder != null){
            mFirstBinder.callMethod1();
            mFirstBinder.callMethod2();
        }
    }

    private void testConn2(){
        if (mIService != null){
            mIService.callMethod3();
            mIService.callMethod4();
        }
    }

    private ServiceConnection mConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d("wangqingbin","服务已连接");
            mFirstBinder = (FirstService.FirstBinder) iBinder;
            mIService = (FirstService.IService) iBinder;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d("wangqingbin","服务已断开");
        }
    };

    public void service(View view) {
        testConn();
        testConn2();

        mServiceIntent = new Intent(this, FirstService.class);
        mServiceIntent.putExtra("key","12345");
        startService(mServiceIntent);
        bindService(mServiceIntent, mConn, BIND_AUTO_CREATE);

        //启动前台服务
        mForegroundServiceIntent = new Intent(this, ForegroundService.class);
        mForegroundServiceIntent.putExtra("foreground","111111111111");
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O){
            startForegroundService(mForegroundServiceIntent);
        }else{
            startService(mForegroundServiceIntent);
        }

        //启动IntentService   不用手动停止服务
        Intent intent = new Intent(this, MyIntentService.class);
        startService(intent);
    }

    public void receiver(View view) {

    }

    public void provider(View view) {

    }

    public void fragment(View view) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //华为手机引发的泄漏
        FixLeakedUtils.fixInputMethodManagerLastSrvView(this);
        FixLeakedUtils.fixInputMethodManagerLeak(this);

        stopService(mServiceIntent);
    //    unbindService(mConn);
    //    stopService(mForegroundServiceIntent);

        mLocalBroadcastManager.unregisterReceiver(mBroadcastReceiver);
    }

}

