package com.bryanrady.futuretask.thread;

/**
 * Java多线程基础-使用多线程
 * https://www.jianshu.com/p/d901b25e0d4a
 * @author: wangqingbin
 * @date: 2020/3/23 9:30
 */
public class ThreadBaseTest {

    private static Object mutex = new Object();

    public static void main(String[] args) {
    //    testThreadExecuteOrder();
    //    testSleepFunction();
    //    testJoinFunction();
    //    testSharedVariable();
    //    testStopThread();
    //    testDaemonThread();
        testPriorityThread();
    }

    /**
     * 启动两个线程，和一个在主线程中执行的逻辑，由打印得知，线程运行结果与执行顺序无关
     *
     *  线程是交替执行的。JAVA采用抢占式线程调度，也就是每个线程由系统来分配时间，线程的切换并不由线程本身决定。
     */
    public static void testThreadExecuteOrder(){
        new CustomThread().start();
        new CustomThread().start();
        for (int i = 1; i <= 5; i++){
            System.out.println(Thread.currentThread().getName() + " : " + i);
        }
//        main : 1
//        Thread-1 : 1
//        Thread-1 : 2
//        Thread-1 : 3
//        main : 2
//        main : 3
//        main : 4
//        main : 5
//        Thread-0 : 1
//        Thread-0 : 2
//        Thread-0 : 3
//        Thread-0 : 4
//        Thread-0 : 5
//        Thread-1 : 4
//        Thread-1 : 5
    }

    static class CustomThread extends Thread{

        @Override
        public void run() {
            for (int i = 1; i <= 5; i++){
                System.out.println(Thread.currentThread().getName() + " : " + i);
            }
        }
    }

    /**
     * sleep是Thread的一个静态native方法，调用了底层的C库函数来实现的睡眠，有一个long类型的参数，表示睡眠多少毫秒。
     *
     * 注意该方法会抛出InterruptedException中断异常。
     *
     * sleep方法的含义就是，让当前正在执行任务的线程睡眠（临时地停止执行）指定的毫秒数，
     * 这个精度和准确性是用系统时钟和调度器保证的。但是，线程并不会释放它拥有的锁。
     *
     */
    public static void testSleepFunction(){
        SleepThread s1 = new SleepThread();
        s1.start();
        SleepThread s2 = new SleepThread();
        s2.start();

        //添加同步之前
//        Thread-0: start sleep
//        Thread-1: start sleep
//        Thread-0: end sleep
//        Thread-1: end sleep

        //添加同步之后
//        Thread-0: start sleep
//        Thread-0: end sleep
//        Thread-1: start sleep
//        Thread-1: end sleep

//        可以多运行几次，可能会有线程1在上面或和线程0在上面，但始终都是一个行程运行完了才会运行另一个线程，
//        中间不会插入进来一个线程运行。
    }

    static class SleepThread extends Thread{

        @Override
        public void run() {
        //    synchronized (mutex){
                System.out.println(Thread.currentThread().getName() + ": start sleep");
                try {
                    //使当前线程进入睡眠状态
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + ": end sleep");
        //    }
        }
    }

    /**
     * 如果想等sleepThread执行完后再执行joinThread
     *
     *      join()
     *      join(long millis)
     *      join(long millis,int nanoseconds)
     *
     *      无参数的join方法其实就是调用了join(0)，即永远等待下去。不过通过源码我们可以看到，在while循环中有一个条件判断，
     *      即isAlive()方法，意思是如果当前线程还活着，就会一直等待下去。
     *
     *      如果某个线程在另一个线程t上调用t.join()，此线程将被挂起，直到目标线程t结束才恢复（即t.isAlive()方法返回假）。
     */
    public static void testJoinFunction(){
        SleepThread sleepThread = new SleepThread();
        sleepThread.start();

        try {
            //让sleepThread线程处于挂起状态，直到sleepThread线程执行完成后，才能执行后面的逻辑
            sleepThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        JoinThread joinThread = new JoinThread();
        joinThread.start();

        //添加join之前
//        Thread-0: start sleep
//        Thread-1: test join
//        Thread-0: end sleep

        //添加join之后
//        Thread-0: start sleep
//        Thread-0: end sleep
//        Thread-1: test join
    }

    static class JoinThread extends Thread{

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + ": test join");
        }
    }


    /**
     * yield() 也是Thread的静态native方法 线程让步
     *
     *      让当前线程释放CPU资源，让其他线程抢占，线程的让步操作比不让步耗时长。
     *
     */

    /**
     * 终止正在运行的线程方法有三种：
     *
     *   1)使用退出标志,使线程正常的执行完run方法终止。
     *
     *   2)使用interrupt方法,使线程异常，线程进行捕获或抛异常，正常执行完run方法终止。
     *
     *   3)使用stop方法强制退出。
     */
    public static void testStopThread(){

        //1.使用自己设置的标志位进行打断
//        FlagThread flagThread = new FlagThread();
//        flagThread.start();
//        try {
//            //这里就睡眠1ms就行，免得打印太多的信息
//            flagThread.sleep(1);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        flagThread.customStop();


        //2.使用Thread内部的interrupt()方法进行打断
        ThreadFlagThread threadFlagThread = new ThreadFlagThread();
        threadFlagThread.start();
        try {
            threadFlagThread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //使用Thread内部方法interrupt()打断
        threadFlagThread.interrupt();
    }

    static class FlagThread extends Thread{

        private boolean isInterrupt = false;

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + ": thread start running");
            int i = 0;
            while (!isInterrupt){
                System.out.println(Thread.currentThread().getName() + ":" + (i++));
            }
            System.out.println(Thread.currentThread().getName() + ": thread is interrupt!");
        }

        public void customStop(){
            isInterrupt = true;
        }
    }

    static class ThreadFlagThread extends Thread{

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + ": start running");
            try {
                int i = 0;
                while (!isInterrupted()){
                    System.out.println(Thread.currentThread().getName() + ":" + (i++));
                }
                Thread.sleep(1);
            }catch (InterruptedException e){
                System.out.println(Thread.currentThread().getName() + ": thread is interrupt");
                e.printStackTrace();
                return;
            }
            System.out.println(Thread.currentThread().getName() + ": thread running completed!");

            //现在我们捕获了InterruptedException异常后，通过return作用，就不会执行后面的代码了，达到了打断的效果
//            Thread-0: start running
//            Thread-0: thread is interrupt
//            java.lang.InterruptedException: sleep interrupted
//            at java.lang.Thread.sleep(Native Method)
//            at com.bryanrady.futuretask.thread.ThreadBaseTest$ThreadFlagThread.run(ThreadBaseTest.java:264)
        }

    }

    public static void  testDaemonThread(){
        DaemonThread daemonThread = new DaemonThread();
        //将这个线程设置为守护线程
        daemonThread.setDaemon(true);
        daemonThread.start();
        System.out.println(Thread.currentThread().getName() + ": stop running!");

        //由打印可得知，主线程停止DaemonThread线程也相应的停止了，但不是立即停止。

//        main: stop running!
//        Thread-0: daemon thread is running!
//        Thread-0: daemon thread is running!
//        Thread-0: daemon thread is running!
    }

    static class DaemonThread extends Thread{
        @Override
        public void run() {
            while (true){
                System.out.println(Thread.currentThread().getName() + ": daemon thread is running!");
            }
        }
    }

    /**
     * 线程优先级
     *      MIN_PRIORITY = 1;
     *      NORM_PRIORITY = 5;
     *      MAX_PRIORITY = 10;
     *
     *      默认是NORM_PRIORITY
     */
    public static void testPriorityThread(){
        PriorityThread p1 = new PriorityThread();
        PriorityThread p2 = new PriorityThread();
        p1.setName("Thread 1");
        p2.setName("Thread 2");
        //设置线程优先级
        p1.setPriority(Thread.MIN_PRIORITY);
        p2.setPriority(Thread.MAX_PRIORITY);
        p1.start();
        p2.start();

        /**
         *      （1）线程运行顺序与代码执行顺序无关。
         *      （2）线程优先级具有随机性，不是优先级高的就一定先完成。
         */

//        Thread 1 : 1, Priority : 1
//        Thread 2 : 1, Priority : 10
//        Thread 2 : 2, Priority : 10
//        Thread 2 : 3, Priority : 10
//        Thread 2 : 4, Priority : 10
//        Thread 2 : 5, Priority : 10
//        Thread 2 : 6, Priority : 10
//        Thread 1 : 2, Priority : 1
//        Thread 2 : 7, Priority : 10
//        Thread 2 : 8, Priority : 10
//        Thread 2 : 9, Priority : 10
//        Thread 1 : 3, Priority : 1
//        Thread 1 : 4, Priority : 1
//        Thread 1 : 5, Priority : 1
//        Thread 1 : 6, Priority : 1
//        Thread 1 : 7, Priority : 1
//        Thread 1 : 8, Priority : 1
//        Thread 1 : 9, Priority : 1

    }

    static class PriorityThread extends Thread{
        @Override
        public void run() {
            int i = 1;
            while (i < 10){
                System.out.println(Thread.currentThread().getName() + " : "+ (i++) + ", Priority : " + Thread.currentThread().getPriority());
            }
        }
    }

}
