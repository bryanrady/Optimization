package com.bryanrady.optimization.base.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.bryanrady.optimization.R;
import com.bryanrady.optimization.base.component.ComponentActivity;

/**
 * @author: wangqingbin
 * @date: 2020/4/2 10:31
 */
public class FragmentUseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_use);
    }

    public void static_use(View view) {
        Intent intent = new Intent(this, FragmentStaticUseActivity.class);
        startActivity(intent);
    }

    public void dynamic_use(View view) {
        Intent intent = new Intent(this, FragmentDynamicUseActivity.class);
        startActivity(intent);
    }

    public void back_stack(View view) {
        Intent intent = new Intent(this, FragmentBackStackActivity.class);
        startActivity(intent);
    }

    public void configure_change(View view) {
        Intent intent = new Intent(this, FragmentConfigureChangeActivity.class);
        startActivity(intent);
    }
}

