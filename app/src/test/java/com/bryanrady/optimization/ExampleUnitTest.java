package com.bryanrady.optimization;

import org.junit.Test;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
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
    public void test() throws InterruptedException {
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