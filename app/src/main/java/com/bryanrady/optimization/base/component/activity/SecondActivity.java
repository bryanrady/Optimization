package com.bryanrady.optimization.base.component.activity;

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
public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Log.d("wangqingbin","B  onCreate........");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("wangqingbin","B  onRestart........");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("wangqingbin","B  onStart........");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("wangqingbin","B  onResume........");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("wangqingbin","B  onPause........");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("wangqingbin","B  onStop........");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("wangqingbin","B  onDestroy........");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        /**
         * 这里记得要写在super之前
         */
        Log.d("wangqingbin","B  onSaveInstanceState........");
        outState.putString("key","bryanrady");
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d("wangqingbin","B  onRestoreInstanceState........");
        String key = savedInstanceState.getString("key");
        Log.d("wangqingbin",key);
    }

    public void back(View view) {
        finish();
    }

}
