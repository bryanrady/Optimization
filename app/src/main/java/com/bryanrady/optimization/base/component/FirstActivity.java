package com.bryanrady.optimization.base.component;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bryanrady.optimization.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @author: wangqingbin
 * @date: 2020/4/1 15:47
 */
public class FirstActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        Log.d("wangqingbin","A  onCreate........");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("wangqingbin","A  onRestart........");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("wangqingbin","A  onStart........");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("wangqingbin","A  onResume........");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("wangqingbin","A  onPause........");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("wangqingbin","A  onStop........");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("wangqingbin","A  onDestroy........");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        /**
         * 这里记得要写在super之前
         */
        Log.d("wangqingbin","A  onSaveInstanceState........");
        outState.putString("key","bryanrady");
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d("wangqingbin","A  onRestoreInstanceState........");
        String key = savedInstanceState.getString("key");
        Log.d("wangqingbin",key);
    }

    public void jump(View view) {
        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);
    }
}
