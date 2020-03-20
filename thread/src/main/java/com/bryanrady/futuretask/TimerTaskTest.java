package com.bryanrady.futuretask;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 定时任务测试 任务执行 串行
 * @author: wangqingbin
 * @date: 2020/3/20 10:52
 */
public class TimerTaskTest {

    private static long start = 0;

    public static void main(String[] args) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("first task");
                start = System.currentTimeMillis();
                try {
                    Thread.sleep(4_000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },0);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //因为前一个 task还没执行完成，需要等到前一个task 执行完成之后才能执行
                System.out.println("second task:" + (System.currentTimeMillis() - start));
            }
        },0);

    }

}
