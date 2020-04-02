package com.bryanrady.optimization.base.component;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import com.bryanrady.optimization.R;
import com.bryanrady.optimization.advertisement.PlayerActivity;
import com.bryanrady.optimization.base.component.activity.FirstActivity;
import com.bryanrady.optimization.base.component.service.FirstService;
import com.bryanrady.optimization.battery.BatteryActivity;
import com.bryanrady.optimization.bitmap.BitmapActivity;
import com.bryanrady.optimization.handler.HandlerActivity;
import com.bryanrady.optimization.leaked.FixLeakedUtils;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @author: wangqingbin
 * @date: 2020/4/2 10:31
 */
public class ComponentActivity extends AppCompatActivity {

    private Intent mServiceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_component);
    }

    public void activity(View view) {
        Intent intent = new Intent(this, FirstActivity.class);
        startActivity(intent);
    }

    private ServiceConnection mConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d("wangqingbin","服务已连接");
            FirstService.FirstBinder binder = (FirstService.FirstBinder) iBinder;
            binder.callService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d("wangqingbin","服务已断开");
        }
    };

    public void service(View view) {
        mServiceIntent = new Intent(this, FirstService.class);
        mServiceIntent.putExtra("key","12345");
    //    startService(mServiceIntent);
        bindService(mServiceIntent, mConn, BIND_AUTO_CREATE);
    }

    public void receiver(View view) {
        mServiceIntent = new Intent(this, FirstService.class);
        mServiceIntent.putExtra("key","234");
        //    startService(mServiceIntent);
        bindService(mServiceIntent, mConn, BIND_AUTO_CREATE);
    }

    public void provider(View view) {
        Intent intent = new Intent(this, BatteryActivity.class);
        startActivity(intent);
    }

    public void fragment(View view) {
        Intent intent = new Intent(this, HandlerActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //华为手机引发的泄漏
        FixLeakedUtils.fixInputMethodManagerLastSrvView(this);
        FixLeakedUtils.fixInputMethodManagerLeak(this);

    //    stopService(mServiceIntent);
    //    unbindService(mConn);
    }

}

