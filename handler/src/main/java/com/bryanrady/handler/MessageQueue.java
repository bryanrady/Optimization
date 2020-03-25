package com.bryanrady.handler;

/**
 * 基于单链表实现
 * @author: wangqingbin
 * @date: 2020/3/25 10:59
 */
public class MessageQueue {

    //代表链表 头部 消息
    Message mMessages;

    //判断程序是否退出
    private boolean mQuitting;

    /**
     * 将消息添加到消息队列中
     * @param msg
     */
    void enqueueMessage(Message msg){
        if (msg.target == null){
            throw new IllegalArgumentException("Message must have a target.");
        }
        synchronized (this){
            if (mQuitting){
                msg.recycle();
                return;
            }
            //先获取队列链表头部Message
            Message p = mMessages;
            //如果当前链表头部没有消息，那就说明是添加第一条消息
            if (p == null){
                mMessages = msg;
            }else{  //如果不是第一条消息，就找到链表尾部添加这条消息
                /**
                 * 先找到单链表尾巴，然后将新加入的 msg 加入 链表尾端
                 */
                //prev代表链表最后一条消息
                Message prev;
                for (;;){
                    prev = p;
                    //将p指向下一条消息
                    p = p.next;
                    //如果下一条消息是null，则代表prev就是链表的最后一条消息
                    if(p == null){
                        break;
                    }
                }
                //找到了最后一条消息后，就将新的消息添加到链表尾部
                prev.next = msg;
            }

            //通知 获取Message的方法 已经有消息添加进队列中了
            notify();
        }
    }

    /**
     * 从消息队列中获取消息 链表头部的消息
     * @return
     */
    Message next(){
        synchronized (this){
            Message p;
            for (;;){
                if (mQuitting){
                    return null;
                }
                //获取头部消息
                p = this.mMessages;
                if (p != null){
                    break;
                }
                try {
                    //如果没有获取到，那就等待，直到队列中有消息
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //取出数据后，要将链表头部的消息设置为原来链表头部消息指向的下一个消息
            mMessages = mMessages.next;
            return p;
        }
    }

    /**
     * 退出 把队列中的消息清空
     */
    public void quit(){
        synchronized (this){
            mQuitting = true;
            //循环清空链表
            Message messages = this.mMessages;
            while (messages != null){
                //这样写的话是不对的
//                messages.recycle();
//                messages = messages.next;

                Message p = messages;
                messages.recycle();
                messages = p.next;

            }

            //退出后 也要通知取出数据的方法不要继续等待了
            notify();
        }
    }

}
