package com.bryanrady.futuretask;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * https://www.cnblogs.com/rinack/p/9888717.html 深入源码分析Java线程池的实现原理
 * @author: wangqingbin
 * @date: 2020/3/20 14:54
 */
public class ThreadPoolExecutorTest {

    //任务队列
    private static final BlockingQueue workQueue = new LinkedBlockingDeque(5);

    private static final ThreadPoolExecutor THREAD_POOL_EXECUTOR;

    static {
        //自己配置线程池

//        corePoolSize: 核心线程数量，可以类比正式员工数量，常驻线程数量。
//        maximumPoolSize: 最大的线程数量，公司最多雇佣员工数量。常驻+临时线程数量。
//        workQueue：多余任务等待队列，再多的人都处理不过来了，需要等着，在这个地方等。
//        keepAliveTime：非核心线程空闲时间，就是外包人员等了多久，如果还没有活干，解雇了。
//        threadFactory: 创建线程的工厂，在这个地方可以统一处理创建的线程的属性。每个公司对员工的要求不一样，在这里设置员工的属性。
//        handler：线程池拒绝策略，什么意思呢?就是当任务实在是太多，人也不够，需求池也排满了，还有任务咋办?
//                  默认是不处理，抛出异常告诉任务提交者，我这忙不过来了。

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                10,
                20,
                30,
                TimeUnit.SECONDS,
                workQueue);

        THREAD_POOL_EXECUTOR = threadPoolExecutor;

    }


    public static void main(String[] args) {

        THREAD_POOL_EXECUTOR.setRejectedExecutionHandler(new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                //自定义被拒绝之后的策略
                System.out.println("reject=====");

                //给到线程池一个监听器，监听当前线程池的 任务队列数
                // 如果任务队列允许 加入新的任务
                //接到回调 在把runnable 加入 线程池
            }
        });

        for (int i = 0; i < 30; i++){
            THREAD_POOL_EXECUTOR.execute(new Runnable() {
                @Override
                public void run() {
                    //这里这样写是为了让线程中的任务一直无法完成，
                    while (true){

                    }
                }
            });
        }

        //最后关闭
        THREAD_POOL_EXECUTOR.shutdown();
    }

    /**
     * 上面创建的线程池核心线程数10个，最大线程数20个，任务队列5个。
     *
     *      现在我们启动30个线程执行任务，线程池中线程数30大于最大线程数20个，然后先将剩余的加入任务队列，任务队列5个也满了之后，
     *
     *      就报了下面的错误，针对这种错误我们可以通过  RejectedExecutionHandler 来进行处理
     *
     *      经过RejectedExecutionHandler处理之后，任务队列中的5个满了之后，剩余的5个没有被添加到就会被拒绝
     *
     *      拒绝会打印5次信息
     *
     */


//            Exception in thread "main" java.util.concurrent.RejectedExecutionException: Task com.bryanrady.futuretask.
//                    ThreadPoolExecutorTest$1@5e2de80c rejected from java.util.concurrent.ThreadPoolExecutor@1d44bcfa
//                [Running, pool size = 20, active threads = 20, queued tasks = 5, completed tasks = 0]


//    ExecutorService接口----ThreadPoolExecutor
//
//    1、避免每次new Thread新建对象
//	  2、线程统一管理，重用存在的线程，减少对象创建、消亡的开销。
//    3、可有效控制最大并发线程数，提高系统资源的使用率，同时避免过多资源竞争，避免堵塞。
//

//    通过Executors静态工厂构建

//    newCachedThreadPool
//          创建一个可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程。

//    newFixedThreadPool
//          创建一个定长线程池，可控制线程最大并发数，超出的任务会在队列中等待。

//    newScheduledThreadPool
//          创建一个定长线程池，支持定时及周期性任务执行。

//    newSingleThreadExecutor
//          创建一个单线程化的线程池，它只会用唯一的工作线程来执行任务，保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行。
//

//    public void execute(Runnable command) {
//        if (command == null)
//            throw new NullPointerException();
//        int c = ctl.get();
//        if (workerCountOf(c) < corePoolSize) {
//            if (addWorker(command, true))
//                return;
//            c = ctl.get();
//        }
//        if (isRunning(c) && workQueue.offer(command)) {
//            int recheck = ctl.get();
//            if (! isRunning(recheck) && remove(command))
//                reject(command);
//            else if (workerCountOf(recheck) == 0)
//                addWorker(null, false);
//        }
//        else if (!addWorker(command, false))
//            reject(command);
//    }

//    1 ：workerCountOf方法根据ctl的低29位，得到线程池的当前线程数，如果线程数小于corePoolSize，则执行addWorker方法创建新
//        的线程执行任务;

//    2 ：判断线程池是否在运行，如果在，任务队列是否允许插入，插入成功再次验证线程池是否运行，如果不在运行，移除插入的任务，
//        然后抛出拒绝策略。如果在运行，没有线程了，就启用一个线程。

//    3 ：如果添加非核心线程失败，就直接拒绝了。


//    如果当前线程池中运行的线程数量小于coresize, 创建线程执行任务。
//
//    如果当前线程池中运行的线程数量大于coresize，线程池中运行的线程数量小于maxsize，创建线程执行任务。
//
//    如果当前线程池中运行的线程数量大于coresize，线程池中运行的线程数量大于等于maxsize，任务队列没满，加入任务队列。
//
////    如果当前线程池中运行的线程数量大于coresize，线程池中运行的线程数量大于等于maxsize，任务队列满了，交给错误RejectedExecutionHandler处理
//
//    添加worker线程
//
//    从方法execute的实现可以看出：addWorker主要负责创建新的线程并执行任务，代码如下(这里代码有点长，没关系，
//     也是分块的，总共有5个关键的代码块)：


//    private boolean addWorker(Runnable firstTask, boolean core) {
//        boolean workerStarted = false;
//        boolean workerAdded = false;
//        Worker w = null;
//        try {
//            w = new Worker(firstTask);
//            final Thread t = w.thread;
//            if (t != null) {
//                final ReentrantLock mainLock = this.mainLock;
//                mainLock.lock();
//                try {
//                    // Recheck while holding lock.
//                    // Back out on ThreadFactory failure or if
//                    // shut down before lock acquired.
//                    int rs = runStateOf(ctl.get());
//
//                    if (rs < SHUTDOWN ||
//                            (rs == SHUTDOWN && firstTask == null)) {
//                        if (t.isAlive()) // precheck that t is startable
//                            throw new IllegalThreadStateException();
//                        workers.add(w);
//                        int s = workers.size();
//                        if (s > largestPoolSize)
//                            largestPoolSize = s;
//                        workerAdded = true;
//                    }
//                } finally {
//                    mainLock.unlock();
//                }
//                if (workerAdded) {
//                    t.start();
//                    workerStarted = true;
//                }
//            }
//        } finally {
//            if (! workerStarted)
//                addWorkerFailed(w);
//        }
//        return workerStarted;
//    }


//    1. 获取线程池主锁。线程池的工作线程通过Woker类实现，通过ReentrantLock锁保证线程安全。
//
//    2. 添加线程到workers中(线程池中)。workers是一个hashSet。所以，线程池底层的存储结构其实就是一个HashSet。
//
//    3. 启动新建的线程

//    Worker线程处理队列任务

//    public void run() {
//        runWorker(this);
//    }

//    final void runWorker(Worker w) {
//        Thread wt = Thread.currentThread();
//        Runnable task = w.firstTask;
//        w.firstTask = null;
//        w.unlock(); // allow interrupts
//        boolean completedAbruptly = true;
//        try {
//            while (task != null || (task = getTask()) != null) {
//                w.lock();
//                // If pool is stopping, ensure thread is interrupted;
//                // if not, ensure thread is not interrupted.  This
//                // requires a recheck in second case to deal with
//                // shutdownNow race while clearing interrupt
//                if ((runStateAtLeast(ctl.get(), STOP) ||
//                        (Thread.interrupted() &&
//                                runStateAtLeast(ctl.get(), STOP))) &&
//                        !wt.isInterrupted())
//                    wt.interrupt();
//                try {
//                    beforeExecute(wt, task);
//                    Throwable thrown = null;
//                    try {
//                        task.run();
//                    } catch (RuntimeException x) {
//                        thrown = x; throw x;
//                    } catch (Error x) {
//                        thrown = x; throw x;
//                    } catch (Throwable x) {
//                        thrown = x; throw new Error(x);
//                    } finally {
//                        afterExecute(task, thrown);
//                    }
//                } finally {
//                    task = null;
//                    w.completedTasks++;
//                    w.unlock();
//                }
//            }
//            completedAbruptly = false;
//        } finally {
//            processWorkerExit(w, completedAbruptly);
//        }
//    }

//    1：是否是第一次执行任务，或者从队列中可以获取到任务。
//    2：获取到任务后，执行任务开始前操作钩子。
//    3：执行任务。
//    4：执行任务后钩子。

//    这两个钩子(beforeExecute，afterExecute)允许我们自己继承线程池，做任务执行前后处理。

//    总结
//
//    所谓线程池本质是一个hashSet。多余的任务会放在阻塞队列中。
//
//    只有当阻塞队列满了后，才会触发非核心线程的创建。所以非核心线程只是临时过来打杂的。直到空闲了，然后自己关闭了。
//
//    线程池提供了两个钩子(beforeExecute，afterExecute)给我们，我们继承线程池，在执行任务前后做一些事情。
//
//    线程池原理关键技术：锁(lock,cas)、阻塞队列、hashSet(资源池)

}
