package com.bryanrady.optimization.base.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bryanrady.optimization.R;

/**
 * Fragment的动态使用
 * @author: wangqingbin
 * @date: 2020/4/10 15:42
 */
public class FragmentDynamicUseActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTvMsg;
    private TextView mTvMy;

    private MessageFragment mMessageFragment;
    private MyFragment mMyFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("wangqingbin","Activity onCreate........");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_fragment_dynamic_use);

        mTvMsg = findViewById(R.id.tv_msg);
        mTvMy = findViewById(R.id.tv_my);
        mTvMsg.setOnClickListener(this);
        mTvMy.setOnClickListener(this);

        setDefaultFragment();
    }

    private void setDefaultFragment(){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        mMessageFragment = new MessageFragment();
        transaction.replace(R.id.fl_content,mMessageFragment);
        //提交事务
        transaction.commit();
    }

    @Override
    public void onClick(View v) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        switch (v.getId()){
            case R.id.tv_msg:
                if (mMessageFragment == null){
                    mMessageFragment = new MessageFragment();
                }
                transaction.replace(R.id.fl_content, mMessageFragment);
                break;
            case R.id.tv_my:
                if (mMyFragment == null){
                    mMyFragment = new MyFragment();
                }
                transaction.replace(R.id.fl_content, mMyFragment);
                break;
        }
        transaction.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("wangqingbin","Activity onStart........");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("wangqingbin","Activity onResume........");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("wangqingbin","Activity onPause........");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("wangqingbin","Activity onStop........");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("wangqingbin","Activity onDestroy........");
    }
}
