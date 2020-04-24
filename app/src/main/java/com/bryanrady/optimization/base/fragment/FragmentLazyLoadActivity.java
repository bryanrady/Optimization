package com.bryanrady.optimization.base.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bryanrady.optimization.R;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @author: wangqingbin
 * @date: 2020/4/2 10:31
 */
public class FragmentLazyLoadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_lazy_load);
    }

    public void old_lazy(View view) {
        Intent intent = new Intent(this, FragmentLazyLoadUseActivity.class);
        startActivity(intent);
    }

}

