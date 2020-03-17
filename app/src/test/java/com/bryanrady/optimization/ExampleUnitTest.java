package com.bryanrady.optimization;


import org.junit.Test;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testProducerConsumer(){
        new Producer().start();
        new Consumer().start();
    }

    static class Product{
        static String value = null;
    }

    static class Producer extends Thread{
        @Override
        public void run() {
            int i = 0;
            while (true){
                if (Product.value == null){
                    Product.value = "No " + i++;
                    System.out.println("生产产品：" + Product.value);
                }
            }
        }
    }

    static class Consumer extends Thread{
        @Override
        public void run() {
            while (true){
                if (Product.value != null){
                    System.out.println("消费产品：" + Product.value);
                    Product.value = null;
                }
            }
        }
    }

//    生产产品：No 1584437525449
//    消费产品：No 1584437525449
//    生产产品：No 1584437525449
//    消费产品：No 1584437525449
//    生产产品：No 1584437525449
//    消费产品：No 1584437525449
//    生产产品：No 1584437525449
//    消费产品：No 1584437525449
//    生产产品：No 1584437525449
//    消费产品：No 1584437525449
//    生产产品：No 1584437525449
//    消费产品：No 1584437525449
//    生产产品：No 1584437525449
//    消费产品：No 1584437525449
//    生产产品：No 1584437525449
//    消费产品：No 1584437525449
//    消费产品：No 1584437525450
//    生产产品：No 1584437525450
//    消费产品：No 1584437525450
//    生产产品：No 1584437525450
    /**
     *  上面的打印只是截取一部分，而且这个打印不会一直循环打印下去，由此看来这个打印没有满足生产消费模型的需要，明显是错的
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
     */

    @Test
    public void testReference() throws InterruptedException {
        //引用队列
        ReferenceQueue<Object> referenceQueue = new ReferenceQueue<>();

        //软引用
        Object obj2 = new Object();
        SoftReference<Object> softObj = new SoftReference<>(obj2, referenceQueue);

        System.out.println("gc之前 soft ：" + softObj.get());
        System.out.println("gc之前 soft queue：" + referenceQueue.poll());

        obj2 = null;
        System.gc();
        Thread.sleep(1000);

        System.out.println("gc之后 soft：" + softObj.get());
        System.out.println("gc之后 soft queue：" + referenceQueue.poll());

        //弱引用
        Object obj3 = new Object();
        WeakReference<Object> weakObj = new WeakReference<>(obj3, referenceQueue);

        System.out.println("gc之前 weak：" + weakObj.get());
        System.out.println("gc之前 weak queue：" + referenceQueue.poll());

        obj3 = null;
        System.gc();
        Thread.sleep(1000);

        System.out.println("gc之后 weak：" + weakObj.get());
        System.out.println("gc之后 weak queue：" + referenceQueue.poll());

        //虚引用
        Object obj4 = new Object();
        PhantomReference<Object> plantomObj = new PhantomReference<>(obj4, referenceQueue);

        System.out.println("gc之前 plantom：" + plantomObj.get());
        System.out.println("gc之前 plantom queue：" + referenceQueue.poll());

        obj4 = null;
        System.gc();
        Thread.sleep(1000);

        System.out.println("gc之后 plantom：" + plantomObj.get());
        System.out.println("gc之后 plantom queue：" + referenceQueue.poll());

//        gc之前 soft ：java.lang.Object@759ebb3d
//        gc之前 soft queue：null
//        gc之后 soft：java.lang.Object@759ebb3d
//        gc之后 soft queue：null
//        gc之前 weak：java.lang.Object@484b61fc
//        gc之前 weak queue：null
//        gc之后 weak：null
//        gc之后 weak queue：java.lang.ref.WeakReference@45fe3ee3
//        gc之前 plantom：null
//        gc之前 plantom queue：null
//        gc之后 plantom：null
//        gc之后 plantom queue：java.lang.ref.PhantomReference@4cdf35a9

    }
}