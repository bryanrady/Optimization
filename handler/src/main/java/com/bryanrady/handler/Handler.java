package com.bryanrady.handler;

/**
 * @author: wangqingbin
 * @date: 2020/3/25 10:59
 */
public class Handler {

    MessageQueue mQueue;
    Callback mCallback;

    public Handler(){
        this(null,null);
    }

    public Handler(Looper looper){
        this(looper,null);
    }

    public Handler(Callback callback){
        this(null, callback);
    }

    public Handler(Looper looper, Callback callback){
        //获得当前线程的Looper和消息队列
        if (looper == null){
            looper = Looper.myLooper();
        }
        mQueue = looper.mQueue;
        this.mCallback = callback;
    }

    public final Message obtainMessage(){
        return Message.obtain(this);
    }

    public final Message obtainMessage(int what){
        return Message.obtain(this, what);
    }

    public final Message obtainMessage(int what, Object obj){
        return Message.obtain(this, what, obj);
    }

    public void sendMessage(Message msg){
        enqueueMessage(msg);
    }

    public void enqueueMessage(Message msg){
        msg.target = this;
        mQueue.enqueueMessage(msg);
    }

    public void dispatchMessage(Message msg){
        if (mCallback != null){
            mCallback.handleMessage(msg);
        }else{
            handleMessage(msg);
        }
    }

    public void handleMessage(Message msg){

    }

    public interface Callback{

        boolean handleMessage(Message msg);
    }

}
