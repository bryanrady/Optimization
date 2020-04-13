package com.bryanrady.optimization.base.fragment;

import android.os.Bundle;
import android.view.Window;

import com.bryanrady.optimization.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

/**
 * @author: wangqingbin
 * @date: 2020/4/13 14:16
 */
public class FragmentConfigureChangeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_back_stack);

        /**
         * 我们每次旋转屏幕的时候首先通过savedInstanceState来判断Activity是否发生了重建，
         *
         */
        if (savedInstanceState == null){
//            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//            ChangeFragment changeFragment = new ChangeFragment();
//            transaction.add(R.id.fl_content,changeFragment);
//            transaction.commit();

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            ChangeFragment changeFragment = new ChangeFragment();
            transaction.replace(R.id.fl_content,changeFragment);
            transaction.commit();
        }

    }

}
