package com.bryanrady.optimization.base.component.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

/**
 * @author: wangqingbin
 * @date: 2020/4/2 10:28
 */
public class FirstService extends Service {

    public class FirstBinder extends Binder implements IService{

        public void callMethod1() {
            method1();
        }

        public void callMethod2() {
            method2();
        }

        @Override
        public void callMethod3() {
            method3();
        }

        @Override
        public void callMethod4() {
            method4();
        }
    }

    private void method1(){
        Log.d("wangqingbin","Service method1 执行........");
    }

    private void method2(){
        Log.d("wangqingbin","Service method2 执行........");
    }

    private void method3(){
        Log.d("wangqingbin","Service method3 执行........");
    }

    private void method4(){
        Log.d("wangqingbin","Service method4 执行........");
    }

    public interface IService{
        void callMethod3();
        void callMethod4();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("wangqingbin","Service onCreate........");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("wangqingbin","Service onStartCommand........");
        String key = intent.getStringExtra("key");
        Log.d("wangqingbin","key == " + key);
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("wangqingbin","Service onBind........");
        String key = intent.getStringExtra("key");
        Log.d("wangqingbin","key222 == " + key);
        return new FirstBinder();
    }

    /**
     * 只有onUnbind()返回true的情况下，onRebind()才会执行，否则不会执行。
     * @param intent
     */
    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        Log.d("wangqingbin","Service onRebind........");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d("wangqingbin","Service onUnbind........");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    //    stopSelf();
        Log.d("wangqingbin","Service onDestroy........");
    }

    //startService()方式启动
//    2020-04-02 10:48:14.144 32095-32095/com.bryanrady.optimization D/wangqingbin: Service onCreate........
//    2020-04-02 10:48:14.145 32095-32095/com.bryanrady.optimization D/wangqingbin: Service onStartCommand........
    //stopService()销毁
//    2020-04-02 10:48:16.784 32095-32095/com.bryanrady.optimization D/wangqingbin: Service onDestroy........

    //bindService()绑定
//    2020-04-02 11:00:43.906 5320-5320/com.bryanrady.optimization D/wangqingbin: Service onCreate........
//    2020-04-02 11:00:43.906 5320-5320/com.bryanrady.optimization D/wangqingbin: Service onBind........
    //unbindService()解绑或者直接退出调用者Activity
//    2020-04-02 11:00:50.905 5320-5320/com.bryanrady.optimization D/wangqingbin: Service onUnbind........
//    2020-04-02 11:00:50.905 5320-5320/com.bryanrady.optimization D/wangqingbin: Service onDestroy........
}
