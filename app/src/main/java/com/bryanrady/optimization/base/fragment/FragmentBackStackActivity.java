package com.bryanrady.optimization.base.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bryanrady.optimization.R;

/**
 * Fragment的返回栈
 * @author: wangqingbin
 * @date: 2020/4/10 15:42
 */
public class FragmentBackStackActivity extends AppCompatActivity implements FirstFragment.FirstBtnClickListener,
        SecondFragment.SecondBtnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("wangqingbin","Activity onCreate........");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_back_stack);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        FirstFragment firstFragment = new FirstFragment();
        //设置一下回调接口
        firstFragment.setFirstBtnClickListener(this);
        transaction.replace(R.id.fl_content, firstFragment);
        transaction.commit();

        //通过这种方式来获取Fragment实例，就可以调用Fragment的方法，不过不推荐，最好用接口方式
        //Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        //Fragment fragment = getSupportFragmentManager().findFragmentById(id);
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

    @Override
    public void onFirstBtnClick() {
        Log.d("wangqingbin","onFirstBtnClick........");
    }

    @Override
    public void onSecondBtnClick() {
        Log.d("wangqingbin","onSecondBtnClick........");
    }
}
