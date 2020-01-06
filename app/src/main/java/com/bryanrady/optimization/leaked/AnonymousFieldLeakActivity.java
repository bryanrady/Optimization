package com.bryanrady.optimization.leaked;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bryanrady.optimization.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * https://blog.csdn.net/liu3364575/article/details/80108312
 * https://www.imooc.com/article/70889
 * https://www.jianshu.com/p/52ac1ded7618
 * https://www.jianshu.com/p/a6658b39e4ff
 * 匿名内部类造成的内存泄漏
 * @author: wangqingbin
 * @date: 2020/1/6 17:01
 */
public class AnonymousFieldLeakActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaked_model);
        TextView tv = findViewById(R.id.tv_prompt);
        tv.setText("匿名内部类造成的内存泄漏");


        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * https://blog.csdn.net/hellocsz/article/details/81974251
                 * 匿名内部类    使用匿名内部类还有个前提条件：必须继承一个父类或实现一个接口
                 *      并不会发生内存泄漏，所以匿名内部类导致Activity发生内存泄漏的原因和耗时无关，只是网上很多人都这样说而已
                 */
                new Animal(){
                    @Override
                    public void sleep() {
//                        Log.e("wangqingbin","动物开始睡觉了");
//                        try {
//                            Thread.sleep(5000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        Log.e("wangqingbin","动物已经睡着了");
                    }
                }.sleep();

                /**
                 * 匿名内部类
                 */
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("wangqingbin","匿名内部类Thread开启线程");
                        while (true){
                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();

                LeakThread leakThread = new LeakThread();
                leakThread.start();

                
                new Handler(){
                    @Override
                    public void handleMessage(@NonNull Message msg) {
                        super.handleMessage(msg);

                    }
                }.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                    }
                },3000);
            }
        });

    }

    private class LeakThread extends Thread{
        @Override
        public void run() {
            super.run();
            Log.e("wangqingbin","非静态内部类LeakThread开启线程");
//            while (isAlive() && !interrupted()){
//                try {
//                    Thread.sleep(5000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //华为手机引发的泄漏
        FixLeakedUtils.fixInputMethodManagerLastSrvView(this);
        FixLeakedUtils.fixInputMethodManagerLeak(this);
    }
}
