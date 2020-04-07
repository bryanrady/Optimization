package com.bryanrady.optimization.base.component;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
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
import com.bryanrady.optimization.leaked.FixLeakedUtils;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @author: wangqingbin
 * @date: 2020/4/2 10:31
 */
public class ComponentActivity extends AppCompatActivity {

    private Intent mServiceIntent;
    private Intent mForegroundServiceIntent;
    private FirstService.FirstBinder mFirstBinder;
    private FirstService.IService mIService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_component);
    }

    public void activity(View view) {
        Intent intent = new Intent(this, FirstActivity.class);
        startActivity(intent);
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
    }

    public void receiver(View view) {
        testConn();
        testConn2();
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
    }

}

