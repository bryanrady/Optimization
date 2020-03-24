package com.bryanrady.futuretask.thread;

/**
 * 锁
 * @author: wangqingbin
 * @date: 2020/3/24 14:41
 */
public class ThreadSyncLockTest {

    private Object mutex = new Object();

    public static void main(String[] args) {
        testDeadLock();
    }

    //synchronized修饰普通方法，此时用的锁是LockTest对象(内置锁)
    public synchronized void test(){

    }

    public void test1(){
        //synchronized修饰普通方法的代码块，此时用的锁是LockTest对象(内置锁)--->this
        synchronized (this){

        }
    }

    public void test2(){
        //synchronized修饰普通方法的代码块，此时用的锁是mutex对象(内置锁),每一个Java对象都有一个对应的锁标记
        synchronized (mutex){

        }
    }

    //synchronized修饰静态方法，静态方法属于类方法，所以获取到的锁属于类的锁(类的字节码文件对象)-->ThreadSyncLockTest.class
    public synchronized static void test3(){

    }

    //修饰静态方法里的代码块，静态方法属于类方法，它属于这个类，获取到的锁也是属于类的锁(类的字节码文件对象)-->ThreadSyncLockTest.class
    public static void test4(){
        synchronized (ThreadSyncLockTest.class){

        }
    }

    public static void testDeadLock(){
        final DeadLock deadLock = new DeadLock();
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                deadLock.method1();
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                deadLock.method2();
            }
        });
        thread1.start();
        thread2.start();

    }

    static class DeadLock{

        private Object mutex1 = new Object();
        private Object mutex2 = new Object();

        public void method1(){
            System.out.println(Thread.currentThread().getName() + ":  wait mutex1");
            synchronized (mutex1){
                System.out.println(Thread.currentThread().getName() + ":  hold mutex1");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + ":  wait mutex2");
                synchronized (mutex2){
                    System.out.println(Thread.currentThread().getName() + ":  hold mutex2");
                }
                System.out.println(Thread.currentThread().getName() + ":  release mutex2");
            }
            System.out.println(Thread.currentThread().getName() + ":  release mutex1");
        }

        public void method2(){
            System.out.println(Thread.currentThread().getName() + ":  wait mutex2");
            synchronized (mutex2){
                System.out.println(Thread.currentThread().getName() + ":  hold mutex2");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + ":  wait mutex1");
                synchronized (mutex1){
                    System.out.println(Thread.currentThread().getName() + ":  hold mutex1");
                }
                System.out.println(Thread.currentThread().getName() + ":  release mutex1");
            }
            System.out.println(Thread.currentThread().getName() + ":  release mutex2");
        }

//        Thread-0:  wait mutex1
//        Thread-0:  hold mutex1
//        Thread-1:  wait mutex2
//        Thread-1:  hold mutex2
//        Thread-0:  wait mutex2
//        Thread-1:  wait mutex1

        /**
         *
         *  从运行结果来看，进程一直处于运行状态，thread_0等待获取thread_1所持有的mutex2，thread_1等待获取
         *  thread_0所持有的mutex1，这样进程在不借助外力的情况下，将处于永久等待状态。
         *
         *  出现死锁的情形：两个或两个以上线程处于永久等待状态，每个线程都在等待其他线程释放所持有的资源（锁）。
         *
         */

    }

}
