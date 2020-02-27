package com.bryanrady.optimization;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bryanrady.optimization.advertisement.PlayerActivity;
import com.bryanrady.optimization.bitmap.compress.CompressActivity;
import com.bryanrady.optimization.leaked.FixLeakedUtils;
import com.bryanrady.optimization.leaked.LeakedActivity;
import com.bryanrady.optimization.shake.ShakeActivity;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void leaked(View view) {
        Intent intent = new Intent(this, LeakedActivity.class);
        startActivity(intent);
    }

    public void shake(View view) {
        Intent intent = new Intent(this, ShakeActivity.class);
        startActivity(intent);
    }

    public void wheel_play(View view) {
        Intent intent = new Intent(this, PlayerActivity.class);
        startActivity(intent);
    }

    public void bitmap_compress(View view) {
        Intent intent = new Intent(this, CompressActivity.class);
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
