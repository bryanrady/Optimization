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
public class SingleTaskActivity extends AppCompatActivity {

    private String mKey;

    private void initData(){
        mKey = getIntent().getStringExtra("key");
        Log.d("wangqingbin","SingleTask  key === " + mKey);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode);
        Log.d("wangqingbin","SingleTask  onCreate........");
        initData();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("wangqingbin","SingleTask  onRestart........");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("wangqingbin","SingleTask  onStart........");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("wangqingbin","SingleTask  onResume........");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("wangqingbin","SingleTask  onPause........");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("wangqingbin","SingleTask  onStop........");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("wangqingbin","SingleTask  onDestroy........");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d("wangqingbin","SingleTask  onNewIntent........");
        setIntent(intent);
        initData();
    }

    public void jump(View view) {
        Intent intent = new Intent(this, ThirdActivity.class);
        startActivity(intent);
    }

    /**
     * 这个Activity是 SingleTask 启动模式
     */
                //启动A的过程
//            04-01 21:18:52.756 29273-29273/com.bryanrady.optimization D/wangqingbin: A  onCreate........
//            04-01 21:18:52.756 29273-29273/com.bryanrady.optimization D/wangqingbin: A  onStart........
//            04-01 21:18:52.756 29273-29273/com.bryanrady.optimization D/wangqingbin: A  onResume........

                //A携带参数new key 跳转到SingleTask的过程
//            04-01 21:18:54.986 29273-29273/com.bryanrady.optimization D/wangqingbin: A  onPause........
//            04-01 21:18:55.026 29273-29273/com.bryanrady.optimization D/wangqingbin: SingleTask  onCreate........
//            04-01 21:18:55.026 29273-29273/com.bryanrady.optimization D/wangqingbin: SingleTask  key === new key
//            04-01 21:18:55.026 29273-29273/com.bryanrady.optimization D/wangqingbin: SingleTask  onStart........
//            04-01 21:18:55.026 29273-29273/com.bryanrady.optimization D/wangqingbin: SingleTask  onResume........
//            04-01 21:18:55.506 29273-29273/com.bryanrady.optimization D/wangqingbin: A  onStop........

                //SingleTask跳转到C的过程
//            04-01 21:18:55.846 29273-29273/com.bryanrady.optimization D/wangqingbin: SingleTask  onPause........
//            04-01 21:18:55.886 29273-29273/com.bryanrady.optimization D/wangqingbin: C  onCreate........
//            04-01 21:18:55.886 29273-29273/com.bryanrady.optimization D/wangqingbin: C  onStart........
//            04-01 21:18:55.886 29273-29273/com.bryanrady.optimization D/wangqingbin: C  onResume........
//            04-01 21:18:56.356 29273-29273/com.bryanrady.optimization D/wangqingbin: SingleTask  onStop........

                //C携带参数 update key 跳转到SingleTask的过程
//            04-01 21:18:57.306 29273-29273/com.bryanrady.optimization D/wangqingbin: C  onPause........
//            04-01 21:18:57.336 29273-29273/com.bryanrady.optimization D/wangqingbin: SingleTask  onNewIntent........
//            04-01 21:18:57.336 29273-29273/com.bryanrady.optimization D/wangqingbin: SingleTask  key === update key
//            04-01 21:18:57.336 29273-29273/com.bryanrady.optimization D/wangqingbin: SingleTask  onRestart........
//            04-01 21:18:57.336 29273-29273/com.bryanrady.optimization D/wangqingbin: SingleTask  onStart........
//            04-01 21:18:57.336 29273-29273/com.bryanrady.optimization D/wangqingbin: SingleTask  onResume........
//            04-01 21:18:57.796 29273-29273/com.bryanrady.optimization D/wangqingbin: C  onStop........
//            04-01 21:18:57.796 29273-29273/com.bryanrady.optimization D/wangqingbin: C  onDestroy........
}
