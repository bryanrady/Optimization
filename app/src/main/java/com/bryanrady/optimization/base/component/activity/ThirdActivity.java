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
public class ThirdActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        Log.d("wangqingbin","C  onCreate........");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("wangqingbin","C  onRestart........");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("wangqingbin","C  onStart........");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("wangqingbin","C  onResume........");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("wangqingbin","C  onPause........");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("wangqingbin","C  onStop........");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("wangqingbin","C  onDestroy........");
    }

    public void jumpSingleTask(View view) {
        Intent intent = new Intent(this, SingleTaskActivity.class);
        intent.putExtra("key","update key");
        startActivity(intent);
    }

    public void jumpSingleInstance(View view) {
        Intent intent = new Intent(this, SingleInstanceActivity.class);
        intent.putExtra("key","update key");
        startActivity(intent);
    }
}
