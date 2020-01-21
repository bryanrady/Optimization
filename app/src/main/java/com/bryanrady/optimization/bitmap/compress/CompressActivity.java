package com.bryanrady.optimization.bitmap.compress;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import com.bryanrady.optimization.R;
import com.bryanrady.optimization.leaked.FixLeakedUtils;
import com.bryanrady.optimization.leaked.LeakedActivity;
import com.bryanrady.optimization.shake.ShakeActivity;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @author: wangqingbin
 * @date: 2020/1/20 16:54
 */
public class CompressActivity extends AppCompatActivity {

    private Bitmap mSrcBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compress);

        mSrcBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.lance);

        compress(mSrcBitmap, Bitmap.CompressFormat.PNG, 50, Environment.getExternalStorageState()+"quality_.png");

    }

    /**
     * 压缩
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


}
