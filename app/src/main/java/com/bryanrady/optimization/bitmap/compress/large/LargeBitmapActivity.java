package com.bryanrady.optimization.bitmap.compress.large;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.bryanrady.optimization.R;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @author: wangqingbin
 * @date: 2020/3/12 16:28
 */
public class LargeBitmapActivity extends AppCompatActivity {

    private Bitmap mSrcBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bitmap_file_compress);

        mSrcBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.lance);

    }

}
