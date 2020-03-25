package com.bryanrady.handler;

/**
 * @author: wangqingbin
 * @date: 2020/3/25 10:59
 */
public class Message {

    int what;
    Object obj;

    Message next;

    //使用这个handler发送的消息 则需要将消息交给这个handler处理
    Handler target;

    private static Message obtain(){
        //这里实际上还有一个消息池的存在，从消息池中获取Message实例，目的就是为了减少内存开销，避免重新为Message分配内存
        //这里就不写了我们直接new出来 看源码即可
        return new Message();
    }

    public static Message obtain(Handler h){
        Message msg = obtain();
        msg.target = h;
        return msg;
    }

    public static Message obtain(Handler h, int what){
        Message msg = obtain();
        msg.target = h;
        msg.what = what;
        return msg;
    }

    public static Message obtain(Handler h, int what, Object obj){
        Message msg = obtain();
        msg.target = h;
        msg.what = what;
        msg.obj = obj;
        return msg;
    }


    public void recycle(){
        what = 0;
        obj = null;
        next = null;
        target = null;
    }

}
