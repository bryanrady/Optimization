package com.bryanrady.optimization.handler;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import com.bryanrady.optimization.R;
import com.bryanrady.optimization.leaked.FixLeakedUtils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class HandlerThreadActivity extends AppCompatActivity {

    private HandlerThread mHandlerThread;
    private Handler mWorkHandler;
    private TextView mUpdateTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_textview);

        mUpdateTv = findViewById(R.id.tv);

        /**
         *  HandlerThread 主要用来主线程通知子线程的
         */

        //创建线程并启动
        mHandlerThread = new HandlerThread("thread1");
        mHandlerThread.start();
        //获取的thread1线程的Looper
        Looper threadLooper = mHandlerThread.getLooper();
        mWorkHandler = new Handler(threadLooper){

            @Override
            public void handleMessage(@NonNull Message msg) {
                Log.d("wangqingbin","thread name: " + Thread.currentThread().getName());
                final String str = (String) msg.obj;
                Log.d("wangqingbin","str == " + str);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mUpdateTv.setText(str);
                    }
                });

            }
        };

        Message msg = Message.obtain();
        msg.obj = "I am from main thread!";
        mWorkHandler.sendMessageDelayed(msg,1000);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //华为手机引发的泄漏
        FixLeakedUtils.fixInputMethodManagerLastSrvView(this);
        FixLeakedUtils.fixInputMethodManagerLeak(this);

        boolean quit = mHandlerThread.quit();
        Log.e("wangqingbin","退出是否成功 " + quit);
    }

}
