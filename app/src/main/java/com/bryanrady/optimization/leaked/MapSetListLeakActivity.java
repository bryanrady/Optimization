package com.bryanrady.optimization.leaked;

import android.os.Bundle;
import android.widget.TextView;

import com.bryanrady.optimization.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

/**
 * 集合中对象没清理造成的内存泄漏
 * @author: wangqingbin
 * @date: 2020/1/6 17:01
 */
public class MapSetListLeakActivity extends AppCompatActivity {

    private List<Object> mList;
    private static Map<String,Object> mMap;
    private Set<Object> mSet;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaked_model);

        TextView tv = findViewById(R.id.tv_prompt);
        tv.setText("集合中对象没清理造成的内存泄漏");

        mList = new ArrayList<>();
        mMap = new HashMap<>();
        mSet = new HashSet<>();

        for (int i=1; i<100; i++){
            mList.add(new Object());
        }
        for (int i=1; i<100; i++){
            mMap.put("Object "+i, new Object());
        }
        for (int i=1; i<100; i++){
            mSet.add(new Object());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //华为手机引发的泄漏
        FixLeakedUtils.fixInputMethodManagerLastSrvView(this);
        FixLeakedUtils.fixInputMethodManagerLeak(this);

        if(mList != null){
            mList.clear();
            mList = null;
        }
        if(mMap != null){
            mMap.clear();
            mMap = null;
        }
        if(mSet != null){
            mSet.clear();
            mSet = null;
        }

    }
}
