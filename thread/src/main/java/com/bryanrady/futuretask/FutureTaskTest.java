package com.bryanrady.futuretask;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * https://www.cnblogs.com/linghu-java/p/8991824.html   FutureTask源码解析
 */
public class FutureTaskTest {

    public static void main(String[] args) {
        WorkerTask workerTask = new WorkerTask();
        FutureTask<String> futureTask = new FutureTask<String>(workerTask){

            /**
             * 耗时操作执行完成的回调
             */
            @Override
            protected void done() {
                super.done();
                try {
                    //通过get()获得结果
                    String result = get();
                    System.out.println("result:" + result);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        };

        //线程池
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(futureTask);

//        try {
//            //如果直接通过futureTask来获取返回的结果，那么这个是个阻塞的 直到获得运行结果
//            futureTask.get();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }

    }

    static class WorkerTask implements Callable<String> {

        @Override
        public String call() throws Exception {
            Thread.sleep(3000);
            return "task ok";
        }
    }

}
