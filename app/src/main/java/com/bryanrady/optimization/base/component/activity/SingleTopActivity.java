package com.bryanrady.optimization.base.component.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bryanrady.optimization.R;

/**
 * @author: wangqingbin
 * @date: 2020/4/1 15:47
 */
public class SingleTopActivity extends AppCompatActivity {

    private String mKey;

    private void initData(){
        mKey = getIntent().getStringExtra("key");
        Log.d("wangqingbin","SingleTop  key === " + mKey);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode);
        Log.d("wangqingbin","SingleTop  onCreate........");
        initData();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("wangqingbin","SingleTop  onRestart........");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("wangqingbin","SingleTop  onStart........");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("wangqingbin","SingleTop  onResume........");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("wangqingbin","SingleTop  onPause........");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("wangqingbin","SingleTop  onStop........");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("wangqingbin","SingleTop  onDestroy........");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d("wangqingbin","SingleTop  onNewIntent........");
        setIntent(intent);
        initData();
    }

    public void jump(View view) {
        Intent intent = new Intent(this, SingleTopActivity.class);
        intent.putExtra("key","update key");
        startActivity(intent);
    }

    /**
     * 这个Activity是 SingleTop 启动模式
     */
                //启动A的过程
//            04-01 21:19:37.916 29273-29273/com.bryanrady.optimization D/wangqingbin: A  onCreate........
//            04-01 21:19:37.916 29273-29273/com.bryanrady.optimization D/wangqingbin: A  onStart........
//            04-01 21:19:37.916 29273-29273/com.bryanrady.optimization D/wangqingbin: A  onResume........

                //A 携带参数 new key 跳转到SingleTop 的过程
//            04-01 21:19:41.186 29273-29273/com.bryanrady.optimization D/wangqingbin: A  onPause........
//            04-01 21:19:41.246 29273-29273/com.bryanrady.optimization D/wangqingbin: SingleTop  onCreate........
//            04-01 21:19:41.246 29273-29273/com.bryanrady.optimization D/wangqingbin: SingleTop  key === new key
//            04-01 21:19:41.246 29273-29273/com.bryanrady.optimization D/wangqingbin: SingleTop  onStart........
//            04-01 21:19:41.246 29273-29273/com.bryanrady.optimization D/wangqingbin: SingleTop  onResume........
//            04-01 21:19:41.726 29273-29273/com.bryanrady.optimization D/wangqingbin: A  onStop........

                //SingleTop携带参数update key 跳转到SingleTop 的过程
//            04-01 21:19:42.326 29273-29273/com.bryanrady.optimization D/wangqingbin: SingleTop  onPause........
//            04-01 21:19:42.326 29273-29273/com.bryanrady.optimization D/wangqingbin: SingleTop  onNewIntent........
//            04-01 21:19:42.326 29273-29273/com.bryanrady.optimization D/wangqingbin: SingleTop  key === update key
//            04-01 21:19:42.326 29273-29273/com.bryanrady.optimization D/wangqingbin: SingleTop  onResume........

    /**
     * 如果不重写onNewIntent并设置新的intent进去的话 是拿不到最新的intent携带参数的
     */

}
