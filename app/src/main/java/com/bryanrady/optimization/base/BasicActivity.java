package com.bryanrady.optimization.base;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bryanrady.optimization.R;
import com.bryanrady.optimization.base.component.ComponentActivity;
import com.bryanrady.optimization.base.fragment.FragmentStaticUseActivity;
import com.bryanrady.optimization.base.fragment.FragmentUseActivity;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @author: wangqingbin
 * @date: 2020/4/2 10:31
 */
public class BasicActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);
    }

    public void component(View view) {
        Intent intent = new Intent(this, ComponentActivity.class);
        startActivity(intent);
    }

    public void fragment(View view) {
        Intent intent = new Intent(this, FragmentUseActivity.class);
        startActivity(intent);
    }

}

