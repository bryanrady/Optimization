package com.bryanrady.optimization.bitmap.compress.memory;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * 图片缩放
 * @author: wangqingbin
 * @date: 2020/2/28 11:52
 */
public class ImageResize {

    /**
     * 对图片进行缩放(压缩)
     * @param context
     * @param resourceId
     * @param needWidth
     * @param needHeight
     * @param hasAlpha
     * @param reusable
     * @return
     */
    public static Bitmap resizeBitmap(Context context, int resourceId, int needWidth, int needHeight, boolean hasAlpha, Bitmap reusable){
        //用于控制图片解码工具
        BitmapFactory.Options options = new BitmapFactory.Options();
        //将这个设置为true表示只解码图片的边界信息 即解码出outXXX参数
        options.inJustDecodeBounds = true;
        //加载图片，这时候图片并没有真正加载到内存当中，我们只是解析出图片的原始信息而已
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);
        //这里每次都是打印的null
//        if (bitmap != null){
//            Log.e("wangqingbin","bitmap=="+bitmap.getByteCount());
//        }else{
//            Log.e("wangqingbin","bitmap null");
//        }
        //获取到原图的宽高
        int width = options.outWidth;
        int height = options.outHeight;
        //采样率(缩放系数)
        options.inSampleSize = calculateInSampleSize(width, height, needWidth, needHeight);
        options.inJustDecodeBounds = false;
        //如果不需要alpha通道
        if (!hasAlpha){
            options.inPreferredConfig = Bitmap.Config.RGB_565;
        }

        /**
         * 图片复用
         */
        //设置可变
        options.inMutable = true;
        options.inBitmap = reusable;
        Log.d("wangqingbin","服务器加载使用复用内存:" + reusable);

        //返回缩放后的图片
        return BitmapFactory.decodeResource(context.getResources(), resourceId, options);
    }

    /**
     * 计算采样率(也就是缩放系数)
     *      我们根据图片的宽高和要求缩放的宽高来进行计算
     * @param width
     * @param height
     * @param needWidth
     * @param needHeight
     * @return
     */
    private static int calculateInSampleSize(int width, int height, int needWidth, int needHeight){
        int inSampleSize = 1;
        //如果原图宽高大于需要图片的宽高 才进行缩放 否则就不进行缩放
        if (width > needWidth && height > needHeight){
            //缩放2倍
            inSampleSize = 2;
            //如果缩放了之后 原图宽高还是大于需要图片的宽高  那我们就继续进行缩放
            while (width/inSampleSize > needWidth && height/inSampleSize > needHeight){
                //在原来的基础上再继续缩放2倍
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }


}
