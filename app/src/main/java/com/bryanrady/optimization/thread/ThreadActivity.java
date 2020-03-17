package com.bryanrady.optimization.thread;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bryanrady.optimization.R;
import com.bryanrady.optimization.leaked.FixLeakedUtils;

import androidx.appcompat.app.AppCompatActivity;

public class ThreadActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //华为手机引发的泄漏
        FixLeakedUtils.fixInputMethodManagerLastSrvView(this);
        FixLeakedUtils.fixInputMethodManagerLeak(this);
    }

    public void producer_consumer(View view) {
        Intent intent = new Intent(this, ThreadActivity.class);
        startActivity(intent);
    }

    public void pool(View view) {
        Intent intent = new Intent(this, ThreadActivity.class);
        startActivity(intent);
    }

}
