package com.bryanrady.optimization.base.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.bryanrady.optimization.R;
import com.bryanrady.optimization.base.fragment.lazy.oldmodel.OldFragment1;
import com.bryanrady.optimization.base.fragment.lazy.oldmodel.OldFragment2;
import com.bryanrady.optimization.base.fragment.lazy.oldmodel.OldFragment3;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * Fragment的动态使用
 * @author: wangqingbin
 * @date: 2020/4/10 15:42
 */
public class FragmentLazyLoadUseActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_one;
    private TextView tv_two;
    private TextView tv_three;

    private OldFragment1 mOldFragment1;
    private OldFragment2 mOldFragment2;
    private OldFragment3 mOldFragment3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_fragment_lazy_load_use);

        tv_one = findViewById(R.id.tv_one);
        tv_two = findViewById(R.id.tv_two);
        tv_three = findViewById(R.id.tv_three);
        tv_one.setOnClickListener(this);
        tv_two.setOnClickListener(this);
        tv_three.setOnClickListener(this);

        setDefaultFragment();
    }

    private void setDefaultFragment(){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        mOldFragment1 = new OldFragment1("OldFragment1");
        transaction.replace(R.id.fl_content, mOldFragment1);
        //提交事务
        transaction.commit();
    }

    @Override
    public void onClick(View v) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        switch (v.getId()){
            case R.id.tv_one:
                if (mOldFragment1 == null){
                    mOldFragment1 = new OldFragment1("OldFragment1");
                }
                transaction.replace(R.id.fl_content, mOldFragment1);
                break;
            case R.id.tv_two:
                if (mOldFragment2 == null){
                    mOldFragment2 = new OldFragment2("OldFragment2");
                }
                transaction.replace(R.id.fl_content, mOldFragment2);
                break;
            case R.id.tv_three:
                if (mOldFragment3 == null){
                    mOldFragment3 = new OldFragment3("OldFragment3");
                }
                transaction.replace(R.id.fl_content, mOldFragment3);
                break;
        }
        transaction.commit();
    }
}
