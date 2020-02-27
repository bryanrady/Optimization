package com.bryanrady.optimization.bitmap.compress;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import com.bryanrady.optimization.R;
import com.bryanrady.optimization.leaked.FixLeakedUtils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.BitmapCompat;

/**
 * @author: wangqingbin
 * @date: 2020/1/20 16:54
 */
public class FileCompressActivity extends AppCompatActivity {

    static {
        System.loadLibrary("bitmap-compress");
    }

    private Bitmap mSrcBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bitmap_compress);

        mSrcBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.lance);

    }

    /**
     * 尺寸压缩
     * @param bitmap
     * @param width
     * @param height
     */
    private void dimensionCompress(Bitmap bitmap, int width, int height){
        //filter参数 true 图片滤波处理 色彩更丰富
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
        if (scaledBitmap != null){
            compress(mSrcBitmap, Bitmap.CompressFormat.PNG, 100,Environment.getExternalStorageState()+"dimension_png.png");
        }
    }

    /**
     * 质量(或者格式)压缩
     * @param bitmap    原图
     * @param compressFormat    压缩格式
     * @param quality
     * @param outPath   压缩后的图片输出目录
     */
    private void compress(Bitmap bitmap, Bitmap.CompressFormat compressFormat, int quality, String outPath){
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(outPath);
            //压缩
            bitmap.compress(compressFormat, quality, fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //华为手机引发的泄漏
        FixLeakedUtils.fixInputMethodManagerLastSrvView(this);
        FixLeakedUtils.fixInputMethodManagerLeak(this);
    }

    public void quality(View view) {
        if (mSrcBitmap != null){
            //质量压缩 质量减半
            compress(mSrcBitmap, Bitmap.CompressFormat.PNG, 50, Environment.getExternalStorageState()+"quality_half.png");
        }
    }

    public void dimensions(View view) {
        if (mSrcBitmap != null){
            //尺寸压缩
            dimensionCompress(mSrcBitmap, 300,300);
        }
    }

    public void format(View view) {
        //像素格式压缩为PNG
        compress(mSrcBitmap, Bitmap.CompressFormat.PNG, 100,Environment.getExternalStorageState()+"format_png.png");
        //像素格式压缩为JPEG
        compress(mSrcBitmap, Bitmap.CompressFormat.JPEG, 100,Environment.getExternalStorageState()+"format_jpeg.jpeg");
        //像素格式压缩为WEBP
        compress(mSrcBitmap, Bitmap.CompressFormat.WEBP, 100,Environment.getExternalStorageState()+"format_webp.webp");
    }

    public void libjpeg(View view) {
        if(mSrcBitmap != null){
            nativeCompress(mSrcBitmap, 100,Environment.getExternalStorageState()+"native_libjpeg.jpeg");
        }
    }

    native void nativeCompress(Bitmap bitmap, int quality, String path);

}
