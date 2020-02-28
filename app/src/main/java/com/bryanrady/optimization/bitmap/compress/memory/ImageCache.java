package com.bryanrady.optimization.bitmap.compress.memory;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.LruCache;

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
    private LruCache<String, Bitmap> mMemoryCache;
    private BitmapFactory.Options mOptions;

    public void init(Context context, String dir){
        this.mContext = context.getApplicationContext();
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
                 * 当bitmap从Lru缓存中移除时回调
                 * @param evicted
                 * @param key
                 * @param oldValue
                 * @param newValue
                 */
                @Override
                protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
                    super.entryRemoved(evicted, key, oldValue, newValue);
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


}
