package com.bryanrady.optimization.bitmap.compress.memory;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Process;
import android.util.LruCache;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * 图片缓存（内存缓存+磁盘缓存）
 * @author: wangqingbin
 * @date: 2020/2/28 15:33
 */
public class ImageCache {

    private static ImageCache instance = null;

    private ImageCache(){

    }

    public static ImageCache getInstance(){
        if (instance == null){
            synchronized (ImageCache.class){
                if (instance == null){
                    instance = new ImageCache();
                }
            }
        }
        return instance;
    }

    private Context mContext;
    //图片解码工具
    private BitmapFactory.Options mOptions;
    //内存缓存 Lru算法实现
    private LruCache<String, Bitmap> mMemoryCache;
    //使用Bitmap复用池来保存可被复用的图片
    private Set<WeakReference<Bitmap>> mReusablePool;
    //引用队列  当弱引用被回收的时候，bitmap会自动被添加到引用队列
    private ReferenceQueue<Bitmap> mReferenceQueue;
    //线程    目的是为了从引用队列中取出数据然后进行释放
    Thread mClearReferenceQueueThread;

    /**
     * 做一个全局的获取
     * @return
     */
    private ReferenceQueue<Bitmap> getReferenceQueue(){
        if (mReferenceQueue == null){
            mReferenceQueue = new ReferenceQueue<>();
            mClearReferenceQueueThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    //设置线程优先级
                    Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                    //释放bitmap
                    while (true){
                        //这里为什么不调用poll()而是用remove()? 因为remove()是一个阻塞的操作，队列中没有的话就不会执行
                        try {
                            Reference<? extends Bitmap> reference = mReferenceQueue.remove();
                            Bitmap bitmap = reference.get();
                            if (bitmap != null && !bitmap.isRecycled()){
                                bitmap.recycle();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            mClearReferenceQueueThread.start();
        }
        return mReferenceQueue;
    }

    public void init(Context context, String dir){
        this.mContext = context.getApplicationContext();
        mOptions = new BitmapFactory.Options();
        //创建一个线程安全的复用池
        mReusablePool = Collections.synchronizedSet(new HashSet<WeakReference<Bitmap>>());

        //通过系统的Application获取系统服务，原因不多说了，内存泄漏隐患
        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        if (am != null){
            //获取程序可用的最大内存
            int largeMemoryClass = am.getLargeMemoryClass();
            mMemoryCache = new LruCache<String, Bitmap>(largeMemoryClass / 8 * 1204 * 1024) {

                /**
                 * 返回value所占用的内存大小
                 * @param key
                 * @param value
                 * @return
                 */
                @Override
                protected int sizeOf(String key, Bitmap value) {
                    return value.getByteCount();
                }

                /**
                 * 当bitmap从内存缓存中移除时回调
                 * @param evicted
                 * @param key
                 * @param oldValue
                 * @param newValue
                 */
                @Override
                protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
                    /**
                     *  3.0以下       Bitmap 内存在 native
                     *  3.0及3.0以上  Bitmap 内存在 java
                     *  8.0           Bitmap 内存在 native
                     */
                    //如果图片是异变的，我们把图片添加到复用池中
                    if (oldValue.isMutable()){
                        putBitmap2ReusablePool(oldValue);
                    }else{
                        if (!oldValue.isRecycled()){
                            oldValue.recycle();
                        }
                    }
                }
            };
        }
    }

    public void putBitmap2MemoryCache(String key, Bitmap bitmap){
        mMemoryCache.put(key,bitmap);
    }

    public Bitmap getBitmapFromMemoryCache(String key){
        if (key != null){
            return mMemoryCache.get(key);
        }
        return null;
    }

    public void clearMemoryCache(){
        mMemoryCache.evictAll();
    }

    public void putBitmap2ReusablePool(Bitmap bitmap){
        mReusablePool.add(new WeakReference<>(bitmap, getReferenceQueue()));
    }

    /**
     * 从复用池中查找符合复用条件的Bitmap
     * @param width
     * @param height
     * @param inSampleSize
     * @return
     */
    public Bitmap getBitmapFromReusablePool(int width, int height, int inSampleSize){
        //3.0以下直接不管
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB){
            return null;
        }
        Iterator<WeakReference<Bitmap>> iterator = mReusablePool.iterator();
        Bitmap reusable = null;
        //迭代查找符合复用条件的Bitmap
        while (iterator.hasNext()){
            Bitmap bitmap = iterator.next().get();
            if (bitmap != null){
                //可以被复用
                if (checkInBitmap(bitmap, width, height, inSampleSize)){
                    reusable = bitmap;
                    //移出复用池
                    iterator.remove();
                    break;
                }
            }else{
                iterator.remove();
            }
        }
        return reusable;
    }


    /**
     *  可被复用的Bitmap必须具备以下条件才可被复用:
     *
     *      1. 可被复用的Bitmap必须设置inMutable为true
     *
     *      2. 4.4(API 19)之前
     *
     *          只有格式为jpg、png，并且待分配内存的Bitmap的宽高与被复用的Bitmap(从复用池中取出来的)的宽高必须一致，
     *          并且inSampleSize为1的Bitmap才可被复用。
     *
     *          并且被复用的Bitmap的inPreferredConfig会覆盖待分配内存的Bitmap设置的inPreferredConfig；
     *
     *         4.4(API 19)之后
     *
     *          被复用的Bitmap的内存必须大于等于待分配内存的Bitmap的内存；
     *
     */

    /**
     * 检查图片是否可以被复用
     * @param bitmap
     * @param width
     * @param height
     * @param inSampleSize
     * @return
     */
    private boolean checkInBitmap(Bitmap bitmap, int width, int height, int inSampleSize){
        //4.4以前
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT){
            return bitmap.getWidth() == width && bitmap.getHeight() == height && inSampleSize == 1;
        }else{
            //如果缩放系数大于1 获得缩放后的宽与高
            if (inSampleSize > 1){
                width /= inSampleSize;
                height /= inSampleSize;
            }
            int byteCount = width * height * getPixelsCount(bitmap.getConfig());
            return byteCount <= bitmap.getAllocationByteCount();
        }
    }

    private int getPixelsCount(Bitmap.Config config){
        if (config == Bitmap.Config.ARGB_8888){
            return 4;
        }
        return 2;
    }

}
