package com.bryanrady.optimization.handler;

import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue;
import android.util.Log;

import java.lang.reflect.Field;

import androidx.annotation.NonNull;

//问：什么是 IdleHandler？有什么用？怎么用？
//
//答：IdleHandler 可以用来提升性能，主要用在我们希望能够在当前线程消息队列空闲时做些事情（譬如 UI 线程在显示完成后，
//        如果线程空闲我们就可以提前准备其他内容）的情况下，不过最好不要做耗时操作。

//   IdleHandler 说白了，就是 Handler 机制提供的一种，可以在 Looper 事件循环的过程中，当出现空闲的时候，允许我们执行任务的一种机制。

//既然 IdleHandler 主要是在 MessageQueue 出现空闲的时候被执行，那么何时出现空闲？
//
//        MessageQueue 是一个基于消息触发时间的优先级队列，所以队列出现空闲存在两种场景。
//
//        MessageQueue 为空，没有消息；
//        MessageQueue 中最近需要处理的消息，是一个延迟消息（when>currentTime），需要滞后执行；



/**
 * MessageQueue中有一个static的接口IdleHandler，这个接口用于在MessageQueue中没有可处理的Message的时候回调，
 * 这样就可以在UI线程中处理完所有的view事务之后，回调一些额外的操作而不会block UI线程。
 * @author: wangqingbin
 * @date: 2020/3/26 9:28
 */
public class DataModel2 implements MessageQueue.IdleHandler {

    private MessageQueue mQueue;
    private Handler mHandler;
    private OnDataChangeListener mListener;

    public DataModel2(){
        HandlerThread handlerThread = new HandlerThread("data_model2");
        handlerThread.start();

        //拿到当前线程的looper getLooper()是个阻塞的方法，必须有了looper后才能返回成功
        Looper looper = handlerThread.getLooper();

        //拿到looper中的消息队列
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            mQueue = looper.getQueue();
        }else{
            //通过反射拿到mQueue
            try {
                Field mQueueField = Looper.class.getDeclaredField("mQueue");
                mQueueField.setAccessible(true);
                mQueue = (MessageQueue) mQueueField.get(looper);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        //注册IdleHandler
        if (mQueue != null){
            mQueue.addIdleHandler(this);
        }

        //根据子线程的Looper创建一个Handler
        mHandler = new Handler(looper){
            @Override
            public void handleMessage(@NonNull Message msg) {
                Log.e("wangqingbin","开始同步数据");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

    }

    @Override
    public boolean queueIdle() {
        //返回true  进入闲置就会执行
        //返回false 只会执行一次
        if (mListener != null){
            mListener.onDataChange2();
        }
        return true;
    }

    public void release(){
        if (mQueue != null){
            mQueue.removeIdleHandler(this);
        }
    }

    public void syncData(){
        mHandler.sendEmptyMessage(0);
    }

    public void setOnDataChangeListener(OnDataChangeListener listener){
        this.mListener = listener;
    }

    public interface OnDataChangeListener{
        void onDataChange2();
    }

 //   https://www.cnblogs.com/plokmju/p/handler_idleHandler.html

//    Q：IdleHandler 有什么用？
//
//    IdleHandler 是 Handler 提供的一种在消息队列空闲时，执行任务的时机；
//    当 MessageQueue 当前没有立即需要处理的消息时，会执行 IdleHandler；

//    Q：MessageQueue 提供了 add/remove IdleHandler 的方法，是否需要成对使用？
//
//    不是必须；IdleHandler.queueIdle() 的返回值，可以移除加入 MessageQueue 的 IdleHandler；

//    Q：当 mIdleHanders 一直不为空时，为什么不会进入死循环？
//
//    只有在 pendingIdleHandlerCount 为 -1 时，才会尝试执行 mIdleHander；
//    pendingIdlehanderCount 在 next() 中初始时为 -1，执行一遍后被置为 0，所以不会重复执行；

//    Q：是否可以将一些不重要的启动服务，搬移到 IdleHandler 中去处理？
//
//    不建议；
//    IdleHandler 的处理时机不可控，如果 MessageQueue 一直有待处理的消息，那么 IdleHander 的执行时机会很靠后；

//    Q：IdleHandler 的 queueIdle() 运行在那个线程？
//
//    陷进问题，queueIdle() 运行的线程，只和当前 MessageQueue 的 Looper 所在的线程有关；
//    子线程一样可以构造 Looper，并添加 IdleHandler；

}
