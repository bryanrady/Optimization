package com.bryanrady.optimization.base.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bryanrady.optimization.R;

/**
 * Fragment的返回栈
 * @author: wangqingbin
 * @date: 2020/4/10 15:42
 */
public class FragmentBackStackActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("wangqingbin","Activity onCreate........");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_back_stack);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        FirstFragment firstFragment = new FirstFragment();
        transaction.add(R.id.fl_content, firstFragment);
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
