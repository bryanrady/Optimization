package com.bryanrady.optimization.leaked;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bryanrady.optimization.R;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * https://blog.csdn.net/liu3364575/article/details/80108312
 * https://www.jianshu.com/p/a6658b39e4ff
 * 匿名内部类造成的内存泄漏
 * @author: wangqingbin
 * @date: 2020/1/6 17:01
 */
public class AnonymousInnerClassLeakActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTv;
    private Timer mTimer;
    private MyTimerTask mTimerTask;
    private AsyncTask<Void, Void, String> mAsyncTask;
    private MyAsyncTask myAsyncTask;
    private FixHandler mFixHandler;

    //匿名内部类
//    private Handler mHandler = new Handler(){
//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what){
//                case 1:
//                    Toast.makeText(AnonymousInnerClassLeakActivity.this, "收到了handler发送的消息 1",Toast.LENGTH_SHORT).show();
//                    break;
//            }
//        }
//    };

    //非静态成员内部类
    private class MyHandler extends Handler{
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
        }
    }

    /**
     *  Handler泄漏原因：不管是匿名内部类的写法，还是非静态内部类的写法,都可能会造成内存泄漏。
     *
     *  我们通过mHandler延迟10s发送消息，所以这个被延迟发送的消息会在被处理之前存在于主线程消息队列中10s,
     *  而一旦我们在10s内退出了activity，这时候activity就会发生内存泄漏。
     *
     *  原因：匿名内部类或者非静态成员内部类会默认持有外部类Activity的引用，而我们通过Handler发送消息，Message又会
     *        持有Handler的引用，Message被存放在消息队列中，MessageQueue又持有Message的引用，所以即使我们退出了activity,
     *        但是因为android主线程的消息队列中还有引用链到达我们的Activity，所以activity的内存不会被回收，从而导致了内存泄漏。
     *
     *  修改: 1.将Handler改为静态成员内部类形式
     *        2.将内部类内部的Activity引用置为弱引用形式。
     *        3.从消息队列中移除消息。
     */

    private static class FixHandler extends Handler{

        private WeakReference<AnonymousInnerClassLeakActivity> mActivity;

        public FixHandler(AnonymousInnerClassLeakActivity activity){
            this.mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            AnonymousInnerClassLeakActivity activity = mActivity.get();
            switch (msg.what){
                case 1:
                    if(activity != null && !activity.isFinishing()){
                        Toast.makeText(activity, "收到了handler发送的消息 1",Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaked_model);
        mTv = findViewById(R.id.tv_prompt);
        mTv.setOnClickListener(this);
        mTv.setText("匿名内部类内部有耗时任务造成的内存泄漏");

        //延迟10发送消息
    //    mHandler.sendEmptyMessageDelayed(1, 10000);

        mFixHandler = new FixHandler(this);
        mFixHandler.sendEmptyMessageDelayed(1,10000);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_prompt:
                //使用匿名内部类有个前提条件：使用的匿名内部类必须继承一个父类或实现一个接口

                /**
                 * 匿名内部类 new Thread开启线程
                 */
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Log.e("wangqingbin","匿名内部类Thread开启线程");
//                        try {
//                            Thread.sleep(15000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }).start();

                /**
                 *  泄漏原因
                 *      Thread现在是一个匿名内部类，它默认持有外部类Activity的强引用，Runnable也是一个匿名内部类，它也默认持有外部类
                 *      Activity的强引用而我们在线程内部又做了长达15s的耗时操作，所以当线程中的耗时操作没执行完的时候，我们直接退出
                 *      Activity，但是Thread还是会持有Activity的强引用，所以Activity的内存也不会被回收，从而造成了Activity内存泄漏
                 *
                 *  修改: 使用静态内部类Thread来执行耗时操作，这样Thread就不会持有Activiy的引用了。
                 *
                 */

                LeakThread leakThread = new LeakThread();
                leakThread.start();



                //定时器执行定时任务
//                new Timer().schedule(new TimerTask() {
//
//                    int i = 1;
//
//                    @Override
//                    public void run() {
//                        Log.e("wangqingbin","执行第"+i+"次定时任务");
//                        try {
//                            Thread.sleep(3000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        i++;
//                    }
//                },1000,1000);

                /**
                 *  泄漏原因
                 *      TimerTask是一个匿名内部类，它默认持有Activity的强引用，我们这里10s后才会通过定时器开启一个定时任务，
                 *      所以当我们退出界面的时候，定时任务还没有开启，TimerTask就不会被销毁，所以他持有的Activity引用也不会被销毁
                 *      还有一种情况就是如果在定时任务里面执行耗时操作，TimerTask也不会被销毁，所以导致了内存泄漏
                 *
                 *  修改：1.将TimerTask使用静态内部类来实现。
                 *        2.在适当的时候取消定时任务。
                 */

                mTimer = new Timer();
                mTimerTask = new MyTimerTask();
                //延迟1s执行，然后1s执行一次
                mTimer.schedule(mTimerTask,1000, 1000);


                //异步任务
//                mAsyncTask = new AsyncTask<Void, Void, String>() {
//
//                    @Override
//                    protected String doInBackground(Void... voids) {
//                        try {
//                            Thread.sleep(10000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        return "异步任务返回的结果!";
//                    }
//
//                    @Override
//                    protected void onCancelled() {
//                        super.onCancelled();
//                        Log.e("wangqingbin","取消异步任务");
//                    }
//
//                    @Override
//                    protected void onPostExecute(String s) {
//                        if (!TextUtils.isEmpty(s)) {
//                            mTv.setText(s);
//                        }
//                    }
//                };
//                mAsyncTask.execute();

                /**
                 *  泄漏原因
                 *      AsyncTask是一个匿名内部类，它默认持有Activity的强引用，AsyncTask用来做异步操作，里面的doInBackground方法我们
                 *      不知道什么时候会执行完成,所以当我们退出界面的时候，doInBackground方法可能还继续在工作，所以AsyncTask就不会被
                 *      回收，所以他持有的Activity引用也不会被销毁，所以导致了内存泄漏
                 *
                 *  修改：1.将AsyncTask使用静态内部类来实现。
                 *        2.在适当的时候取消异步任务，但是很多需求可能不需要我们取消异步任务。
                 */

                myAsyncTask = new MyAsyncTask(this);
                myAsyncTask.execute();

                break;
        }
    }

    /**
     * 这里一定要使用静态内部类，不使用static的话，LeakThread还会默认持有外部类Activity的引用
     */
    private static class LeakThread extends Thread{
        @Override
        public void run() {
            Log.e("wangqingbin","静态内部类LeakThread开启线程");
            try {
                Thread.sleep(15000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.e("wangqingbin","静态内部类LeakThread执行完成");
        }
    }

    private static class MyTimerTask extends TimerTask{
        int i = 1;

        @Override
        public void run() {
            Log.e("wangqingbin","执行第"+i+"次定时任务");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            i++;
        }
    }

    private static class MyAsyncTask extends AsyncTask<Void,Void,String>{

        //很多时候我们需要更新UI信息，如果直接使用全局变量和全局方法来实现，感觉有些不负责任，所以这里我们绑定下activity引用
        private WeakReference<AnonymousInnerClassLeakActivity> mActivity;

        public MyAsyncTask(AnonymousInnerClassLeakActivity activity){
            this.mActivity = new WeakReference<>(activity);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "经过异步任务后，更新UI";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (!TextUtils.isEmpty(s)){
                AnonymousInnerClassLeakActivity activity = mActivity.get();
                if(activity != null && !activity.isFinishing()){
                    activity.mTv.setText(s);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //清除当前任务里面的任务队列
        if(mTimerTask != null && !mTimerTask.cancel()){
            mTimerTask.cancel();
            mTimerTask = null;
        }
        //清除定时器里面的所有任务
        if (mTimer != null){
            mTimer.cancel();
        }
//        if(mAsyncTask != null && !mAsyncTask.isCancelled()){
//            mAsyncTask.cancel(true);
//            mAsyncTask = null;
//        }
        //这里即时不取消也不会发生内存泄漏了，我们就不取消吧，让它执行完
//        if(myAsyncTask != null && !myAsyncTask.isCancelled()){
//            myAsyncTask.cancel(true);
//            myAsyncTask = null;
//        }

        //将消息移除消息队列
        if(mFixHandler != null){
            mFixHandler.removeCallbacksAndMessages(null);
        }

        //华为手机引发的泄漏
        FixLeakedUtils.fixInputMethodManagerLastSrvView(this);
        FixLeakedUtils.fixInputMethodManagerLeak(this);
    }
}
