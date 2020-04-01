package com.bryanrady.futuretask.thread;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author: wangqingbin
 * @date: 2020/3/31 16:07
 */
public class CopyOnWriteArrayListTest {

    private static List<String> mList = Collections.synchronizedList(new ArrayList<String>());
    //private static List<String> mList = new CopyOnWriteArrayList<>();

    public static void main(String[] args) {
        test1();
    //    test2();
    }

    private static void test1(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (mList){
                    mList.add("1");
                    mList.add("2");
                    mList.add("3");
                    mList.add("4");
                    mList.add("5");
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (mList){
                    for (String s : mList){
                        if ("2".equals(s)){
                            mList.remove(s);
                        }
                    }
                    for (String s : mList){
                        System.out.println("Thread2 running : " + s);
                    }
                }
            }
        }).start();
    }

    private static void test3(){
        mList.add("1");
        mList.add("2");
        mList.add("3");
        mList.add("4");
        mList.add("5");
        for (String s : mList){
            if ("2".equals(s)){
                mList.remove(s);
            }
        }
        for (String s : mList){
            System.out.println("mList : " + s);
        }
    }


}
