package com.bryanrady.optimization.bitmap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bryanrady.optimization.R;
import com.bryanrady.optimization.bitmap.compress.file.FileCompressActivity;
import com.bryanrady.optimization.bitmap.large.LargeBitmapActivity;
import com.bryanrady.optimization.bitmap.compress.memory.MemoryCompressActivity;
import com.bryanrady.optimization.leaked.FixLeakedUtils;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @author: wangqingbin
 * @date: 2020/1/20 16:39
 */
public class BitmapActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bitmap);
    }

    public void bitmap_compress(View view) {
        Intent intent = new Intent(this, FileCompressActivity.class);
        startActivity(intent);
    }

    public void bitmap_memory(View view) {
        Intent intent = new Intent(this, MemoryCompressActivity.class);
        startActivity(intent);
    }

    public void bitmap_large(View view) {
        Intent intent = new Intent(this, LargeBitmapActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //华为手机引发的泄漏
        FixLeakedUtils.fixInputMethodManagerLastSrvView(this);
        FixLeakedUtils.fixInputMethodManagerLeak(this);
    }

}
