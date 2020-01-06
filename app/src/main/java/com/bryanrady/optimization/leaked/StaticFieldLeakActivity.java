package com.bryanrady.optimization.leaked;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bryanrady.optimization.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

/**
 * 静态变量造成的内存泄漏
 * @author: wangqingbin
 * @date: 2020/1/6 17:01
 */
public class StaticFieldLeakActivity extends AppCompatActivity {

    private static StaticFieldLeakActivity mActivity;
    private static TextView mTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaked_model);

        mActivity = this;
        mTextView = findViewById(R.id.tv_prompt);
        mTextView.setText("静态变量造成的内存泄漏");
    }

    /**
     * StaticFieldLeakActivity.mActivity
     *
     * StaticFieldLeakActivity.mTextView
     *
     * AppcompatTextView.mContext
     *
     *
     * 静态变量导致了Activity内存泄漏
     *
     *      泄漏原因：static变量生命周期和Application一样长，而mActivity变量指向的对象就是StaticFieldLeakActivity，而控件mTextView也是持有的
     *                StaticFieldLeakActivity引用，所以导致StaticFieldLeakActivity界面销毁的导致内存无法被回收，从而引发了Activity发生泄漏
     *
     *      修改方法：在Activity销毁的时候将mActivity和mTextView置为null
     */

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mActivity != null){
            mActivity = null;
        }
        if (mTextView != null){
            mTextView = null;
        }
    }
}
