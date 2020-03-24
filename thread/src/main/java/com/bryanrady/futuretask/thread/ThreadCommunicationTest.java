package com.bryanrady.futuretask.thread;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

/**
 * 线程间通讯
 * @author: wangqingbin
 * @date: 2020/3/24 15:49
 */
public class ThreadCommunicationTest {

    public static void main(String[] args) {
    //    testWaitNotify();

    //    testJoin();

    //    testPipeStream();

        testThreadLocal();
    }

    private static void testThreadLocal() {
        final Tools tools = new Tools();
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + " : " + tools.getValue());
                tools.setValue("wangqingbin");
                System.out.println(Thread.currentThread().getName() + " : " + tools.getValue());
                tools.setValue("bryanrady");
                System.out.println(Thread.currentThread().getName() + " : " + tools.getValue());
            }
        },"thread1");
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                tools.setValue("bryanrady");
                System.out.println(Thread.currentThread().getName() + " : " + tools.getValue());
            }
        },"thread2");

        thread2.start();
        thread1.start();

//        thread2 : bryanrady
//        thread1 : init value
//        thread1 : wangqingbin
//        thread1 : bryanrady

        /**
         *
         * https://www.jianshu.com/p/98b68c97df9b
         * https://www.jianshu.com/p/3c5d7f09dfbd
         * https://www.jianshu.com/p/e200e96a41a0
         *
         *  tHREAD
         *  （1）每个Thread线程内部都有一个Map。
         *  （2）Map里面存储线程本地对象（key）和线程的变量副本（value），
         *  （3）但是Thread内部的Map是由ThreadLocal维护的，由ThreadLocal负责向map获取和设置线程的变量值。
         *      所以对于不同的线程，每次获取副本值时，别的线程并不能获取到当前线程的副本值，形成了副本的隔离，互不干扰。
         *
         *
         *  ThreadLocal特性
         *
         *      ThreadLocal和Synchronized都是为了解决多线程中相同变量的访问冲突问题，不同的点是
         *
         *          Synchronized是通过线程等待，牺牲时间来解决访问冲突
         *
         *          ThreadLocal是通过每个线程单独一份存储空间，牺牲空间来解决冲突，并且相比于Synchronized，ThreadLocal具有线程
         *          隔离的效果，只有在线程内才能获取到对应的值，线程外则不能访问到想要的值。
         *
         *      正因为ThreadLocal的线程隔离特性，使他的应用场景相对来说更为特殊一些。在android中Looper、ActivityThread以及AMS中
         *      都用到了ThreadLocal。当某些数据是以线程为作用域并且不同线程具有不同的数据副本的时候，就可以考虑采用ThreadLocal。
         *
         */
    }

    static class Tools{

        private static ThreadLocal<String> threadLocal = new ThreadLocal<String>(){

            /**
             * 这个方法返回一个默认值,也可以不实现这个方法
             * @return
             */
            @Override
            protected String initialValue() {
                return "init value";
            }
        };

        public void setValue(String value){
            threadLocal.set(value);
        }

        public String getValue(){
            return threadLocal.get();
        }

    }

    private static void testPipeStream() {
        PipedInputStream inputStream = new PipedInputStream();
        PipedOutputStream outputStream = new PipedOutputStream();
        try {
            outputStream.connect(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        WriteThread write = new WriteThread("write_thread", outputStream);
        ReadThread read = new ReadThread("read_thread", inputStream);
        write.start();
        read.start();

//        write_thread: start write data
//        write_thread: write data completed
//        read_thread: start read data
//        read_thread: read data :18798085

    }

    static class WriteThread extends Thread{

        private PipedOutputStream outputStream;

        public WriteThread(String name,PipedOutputStream outputStream){
            super(name);
            this.outputStream = outputStream;
        }

        @Override
        public void run() {
            if (outputStream == null){
                return;
            }
            try {
                System.out.println(Thread.currentThread().getName() + ": start write data");
                String data = "18798085";
                outputStream.write(data.getBytes(),0,data.getBytes().length);
                outputStream.flush();
                System.out.println(Thread.currentThread().getName() + ": write data completed");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (outputStream != null){
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    static class ReadThread extends Thread{

        private PipedInputStream inputStream;

        public ReadThread(String name,PipedInputStream inputStream){
            super(name);
            this.inputStream = inputStream;
        }

        @Override
        public void run() {
            if (inputStream == null){
                return;
            }
            byte[] buffer = new byte[1024];
            System.out.println(Thread.currentThread().getName() + ": start read data");
            String data = "";
            try {
                int length = inputStream.read(buffer, 0, buffer.length);
                while (length != -1) {
                    String str = new String(buffer, 0, length);
                    data += str;
                    length = inputStream.read(buffer, 0, buffer.length);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (inputStream != null){
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            System.out.println(Thread.currentThread().getName() + ": read data :" + data);
        }
    }

    private static void testJoin() {
        JoinThread joinThread = new JoinThread();
        joinThread.start();
        try {
            joinThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(Thread.currentThread().getName() + ":  value == " + joinThread.value);

        //通过等待的方式获取线程的结果
//        Thread-0: start running
//        Thread-0: end running
//        main:  value == 5
    }

    static class JoinThread extends Thread{

        private int value;

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + ": start running");
            try {
                Thread.sleep(3000);
                value = 5;
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + ": end running");
        }
    }

    private static void testWaitNotify() {
//        thread 1 : enter wait(), RUNNABLE
//        thread 1,state:WAITING
//        thread 2 : enter notify(), RUNNABLE
//        thread 2 : exit notify(), RUNNABLE
//        thread 1 : exit wait(), RUNNABLE
//        thread 2,state:TERMINATED

//        了wait与notify的简单使用，使用这两个方法时必须有同种锁的同步代码块,调用wait方法前，首先获得wait对象的锁。执行wait方法后，
//        当前线程释放锁，并且状态变更为Waiting状态，当前任务加入至“预处理队列”中；调用notify方法前，首先获得notify对象的锁。
//        执行完notify synchronized同步代码块后，当前线程释放锁，如果该线程没有任务则该线程状态变更为Terminated状态。

        final WaitNotifyService waitNotifyService = new WaitNotifyService();
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                waitNotifyService.invokeWait();
            }
        },"thread 1");
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                waitNotifyService.invokeNotify();
            }
        },"thread 2");

        thread1.start();
        thread2.start();

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(thread1.getName() + ",state:" + thread1.getState());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(thread2.getName() + ",state:" + thread2.getState());
    }

    static class WaitNotifyService{

        private void invokeWait(){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (this){
                System.out.println(Thread.currentThread().getName() + " : enter wait(), " + Thread.currentThread().getState());
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + " : exit wait(), " + Thread.currentThread().getState());
            }
        }

        private void invokeNotify(){
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (this){
                System.out.println(Thread.currentThread().getName() + " : enter notify(), " + Thread.currentThread().getState());
                notify();
                System.out.println(Thread.currentThread().getName() + " : exit notify(), " + Thread.currentThread().getState());
            }
        }
    }


}
