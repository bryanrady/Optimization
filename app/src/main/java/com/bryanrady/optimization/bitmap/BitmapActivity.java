package com.bryanrady.optimization.bitmap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bryanrady.optimization.R;
import com.bryanrady.optimization.advertisement.PlayerActivity;
import com.bryanrady.optimization.leaked.FixLeakedUtils;
import com.bryanrady.optimization.leaked.LeakedActivity;
import com.bryanrady.optimization.shake.ShakeActivity;

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

    public void compress(View view) {
        Intent intent = new Intent(this, LeakedActivity.class);
        startActivity(intent);
    }

    public void memory(View view) {
        Intent intent = new Intent(this, ShakeActivity.class);
        startActivity(intent);
    }

    public void long_bitmap(View view) {
        Intent intent = new Intent(this, ShakeActivity.class);
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
