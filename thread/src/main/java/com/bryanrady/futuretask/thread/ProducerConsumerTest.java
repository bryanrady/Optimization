package com.bryanrady.futuretask.thread;

/**
 * 生产消费模式
 * @author: wangqingbin
 * @date: 2020/3/19 14:51
 */
public class ProducerConsumerTest {

    public static void main(String[] args) {
//        new Producer().start();
//        new Consumer().start();

        Object mutex = new Object();
        new Producer2(mutex).start();
        new Consumer2(mutex).start();
    }

    static class Product{

    //    static String value = null;

        static volatile String value = null;

    }

    static class Producer extends Thread{

        @Override
        public void run() {
            while (true){
                if (Product.value == null){
                    Product.value = "No " + System.currentTimeMillis();
                    System.out.println("Producer: " + Product.value);
                }
            }
        }
    }

    static class Consumer extends Thread{

        @Override
        public void run() {
            while (true){
                if (Product.value != null){
                    System.out.println("Consumer: " + Product.value);
                    Product.value = null;
                }
            }
        }
    }

    /**
     *  这个打印不会一直循环打印下去，就是程序不会一直运行下去，由此看来这个打印没有满足生产消费模型的需要，明显是错的
     *
     *  出现这种情况的原因：
     *
     *      线程机制为了提高运行效率，当一个线程在不断的访问一个变量的时候，线程会使用一个线程私有空间来存储这个变量。
     *      Producer线程和Consumer线程都在不断的访问修改value变量，所以Producer线程和Consumer线程都有一个线程私有空间
     *      来存放这个value，所以当Producer线程在修改value变量时，它实际上只修改自己私有空间的value,它对Consumer线程的
     *      value变量是不可见的，所以导致了Producer线程在一直修改value变量，但实际上Consumer线程看见的value变量还是null，
     *      所以程序就停住了
     *
     *      如何解决？
     *
     *      使用volatile关键字   易变变量
     *
     *      专门修饰被不同线程访问和修改的变量，让线程访问这个变量的时候 每次都从变量的原地址读取
     *
     *      加了volatile关键字之后打印会一直持续下去，程序会一直运行下去，但是如果这样来实现生产消费模式会有点问题：
     *
     *         程序一直在while(true)循环，不说别的，程序会一直运行，cpu一直运行。
     *
     *         修改为阻塞模式，使用锁
     *
     */

    static class Producer2 extends Thread{

        Object mutex;

        public Producer2(Object mutex){
            this.mutex = mutex;
        }

        @Override
        public void run() {
            while (true){
                synchronized (mutex){
                    if (Product.value != null){
                        //如果产品有的话，证明没有被消费，那就等待消费者进行消费
                        try {
                            mutex.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    //没有产品的话就进行生产
                    Product.value = "No " + System.currentTimeMillis();
                    System.out.println("Producer: " + Product.value);

                    //生产完成之后，通知消费者进行消费
                    mutex.notify();

                }

            }
        }
    }

    static class Consumer2 extends Thread{

        Object mutex;

        public Consumer2(Object mutex){
            this.mutex = mutex;
        }

        @Override
        public void run() {
            while (true){
                synchronized (mutex){
                    //如果没有产品,就证明生产者还没有生产出产品，那消费者就要进行等待
                    if (Product.value == null){
                        try {
                            mutex.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    //有产品的话就消费
                    System.out.println("Consumer: " + Product.value);
                    Product.value = null;

                    //这里加上睡眠是为了好看日志而已
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    //消费完成之后通知生产者进行生产
                    mutex.notify();
                }
            }
        }
    }

}
