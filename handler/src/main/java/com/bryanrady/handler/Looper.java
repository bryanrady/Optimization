package com.bryanrady.handler;

/**
 * @author: wangqingbin
 * @date: 2020/3/25 10:59
 */
public class Looper {

    //一个线程只有一个Looper ThreadLocal用来管理各个线程的Looper
    static final ThreadLocal<Looper> sThreadLocal = new ThreadLocal<Looper>();

    //其实这里可以为每个线程都先初始化一个Looper，这样就不用给每个线程调用prepare()了，不知道谷歌工程师为什么不这样做？
    // 可能有别的原因吧？
//    static final ThreadLocal<Looper> sThreadLocal = new ThreadLocal<Looper>(){
//        @Override
//        protected Looper initialValue() {
//            return new Looper();
//        }
//    };

    //Looper里面有个消息队列
    MessageQueue mQueue;

    private Looper(){
        mQueue = new MessageQueue();
    }

    /**
     * 准备Looper
     */
    public static void prepare(){
        if (sThreadLocal.get() != null){
            throw new RuntimeException(Thread.currentThread().getName() + "already have a looper");
        }
        sThreadLocal.set(new Looper());
    }

    /**
     * 获取当前线程的Looper
     * @return
     */
    public static Looper myLooper(){
        return sThreadLocal.get();
    }

    /**
     * 不断的从MessageQueue中获取Message
     */
    public static void loop(){
        //获取当前线程的Looper
        Looper looper = myLooper();
        //拿到消息队列
        MessageQueue queue = looper.mQueue;
        for (;;){
            //从消息队列中取出消息
            Message next = queue.next();
            //如果是空，说明没有消息了
            if (next == null){
                break;
            }
            //使用对应的handler分发消息
            next.target.dispatchMessage(next);
        }
    }

    public void quit(){
        mQueue.quit();
    }

}
