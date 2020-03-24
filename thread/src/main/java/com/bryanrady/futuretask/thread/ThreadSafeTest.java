package com.bryanrady.futuretask.thread;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 线程安全
 * @author: wangqingbin
 * @date: 2020/3/23 15:01
 */
public class ThreadSafeTest {

    public static void main(String[] args) {
        testSaleTicket();
    }

    public static void testSaleTicket(){
        //安全的售票任务
        Object mutex = new Object();
        SafeTicketRunnable safeTicketRunnable = new SafeTicketRunnable(mutex);
        //3个售票窗口进行售票
        Thread[] threads = new Thread[3];
        for (int i = 0; i < threads.length; i++){
            threads[i] = new Thread(safeTicketRunnable,"thread "+ (i+1));
        }
        for (Thread thread : threads){
            thread.start();
        }

    }

    /**
     *  线程之间变量有共享与不共享之分，共享理解为大家都使用同一份，不共享理解为每个单独持有一份。
     *
     *  count变量是共享的，不然都会打印5。因为我们用的是用一个Runnable，但是也发现了修改值的顺序有问题，
     *  有时候还会出现数字相同的情况，该现象是由于多线程对同一变量进行读写操作不同步产生的。
     *
     *  解决方案在访问变量方法中增加synchronized关键字进行同步
     *
     *      synchronized关键字，含有synchronized关键字的这个方法称为“互斥区” 或“临界区”，
     *      只有获得这个关键字对应的锁才能执行方法体，方法体执行完自动会释放锁。
     *
     */
    static class SafeTicketRunnable implements Runnable{

        private Object mutex;
        private int count = 1000;

        //锁
        private Lock l;


        public SafeTicketRunnable(Object mutex){
            this.mutex = mutex;
            this.l = new ReentrantLock();
        }

        @Override
        public void run() {
            while (true){

                //同步代码块
//                synchronized (this){
//                    if (count > 0){
//                        System.out.println(Thread.currentThread().getName() + ": current ticket is " + --count);
//                    }
//                }
                //同步代码块
//                synchronized (mutex){
//                    if (count > 0){
//                        System.out.println(Thread.currentThread().getName() + ": current ticket is " + --count);
//                    }
//                }

                //同步方法
//                saleTicket();

                //同步锁
                //上锁
                l.lock();
                try {
                    if (count > 0){
                        System.out.println(Thread.currentThread().getName() + ": current ticket is " + --count);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    //释放锁
                    l.unlock();
                }
            }
        }

    }

}
