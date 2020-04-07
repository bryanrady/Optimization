package com.bryanrady.handler;


import javax.print.attribute.HashAttributeSet;

/**
 * 简易版的Handle
 * @author: wangqingbin
 * @date: 2020/3/25 10:58
 */
public class HandlerTest {

    public static void main(String[] args) {
//        Looper.prepare();
//        testHandler();
//        Looper.loop();

        testHandlerThread();
    }

    private static void testHandler(){
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                System.out.println(Thread.currentThread().getName() + " : receive message : " + msg.obj);
            }
        };

        final Handler handler2 = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                System.out.println(Thread.currentThread().getName() + " : receive message : " + msg.obj);
                return false;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.what = 1;
                msg .obj = "hello";
                handler.sendMessage(msg);
                System.out.println(Thread.currentThread().getName() + " : send message :" + msg .obj);
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = handler.obtainMessage();
                msg.what = 2;
                msg .obj = "world";
                handler2.sendMessage(msg);
                System.out.println(Thread.currentThread().getName() + " : send message :" + msg .obj);
            }
        }).start();


        //拿到主线程的looper
        final Looper looper = Looper.myLooper();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                looper.quit();
            }
        }).start();

    }


    public static void testHandlerThread(){

        HandlerThread handlerThread = new HandlerThread("handleThread");
        handlerThread.start();

        final Handler handler = new Handler(handlerThread.getLooper()){
            @Override
            public void handleMessage(Message msg) {
                System.out.println(Thread.currentThread().getName() + " : receive message : " + msg.obj);
            }
        };

        Message msg = handler.obtainMessage();
        msg .obj = "i am from main thread";
        handler.sendMessage(msg);
        System.out.println(Thread.currentThread().getName() + " : send message :" + msg .obj);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = handler.obtainMessage();
                msg .obj = "i am from thread11";
                handler.sendMessage(msg);
                System.out.println(Thread.currentThread().getName() + " : send message :" + msg .obj);
            }
        }).start();

        //拿到HandlerThread的Looper 然后退出
        final Looper looper = handlerThread.getLooper();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                looper.quit();
            }
        }).start();

    }



}
