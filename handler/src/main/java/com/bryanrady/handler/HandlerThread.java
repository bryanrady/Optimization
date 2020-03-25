package com.bryanrady.handler;

/**
 * HandlerThread 其实就这么简单
 * @author: wangqingbin
 * @date: 2020/3/25 17:28
 */
public class HandlerThread extends Thread {

    private Handler mHandler;
    private Looper mLooper;

    public HandlerThread(String name){
        super(name);
    }

    protected void onLooperPrepared(){

    }

    @Override
    public void run() {
        super.run();
        //准备Looper
        Looper.prepare();
        synchronized (this){
            //获取当前线程的Looper
            mLooper = Looper.myLooper();
            //通知getLooper() Looper有值了
            notify();
        }
        //这相当于个钩子函数，已经准备好了looper, 子类可以重写
        onLooperPrepared();
        //开始从队列中取数据
        Looper.loop();
    }

    /**
     * 获取Looper的方法是一个阻塞
     * @return
     */
    public Looper getLooper(){
        if (!isAlive()){
            return null;
        }
        synchronized (this){
            if (isAlive() && mLooper == null){
                //如果线程起起来了，但是Looper还没有准备好，那就等待
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return mLooper;
    }

    /**
     * 其实就是创建当前线程的Handler
     * @return
     */
    public Handler getThreadHandler(){
        if (mHandler == null){
            mHandler = new Handler(getLooper());
        }
        return mHandler;
    }

    public boolean quit(){
        Looper looper = getLooper();
        if (looper != null){
            looper.quit();
            return true;
        }
        return false;
    }

}
