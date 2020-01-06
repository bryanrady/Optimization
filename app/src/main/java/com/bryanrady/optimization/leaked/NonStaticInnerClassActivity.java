package com.bryanrady.optimization.leaked;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * https://www.imooc.com/article/70889
 * https://blog.csdn.net/liu3364575/article/details/80108312
 * https://www.jianshu.com/p/e766b52d84a9
 * 非静态内部类造成的内存泄漏
 * @author: wangqingbin
 * @date: 2020/1/6 17:01
 */
public class NonStaticInnerClassActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    public class Inner{

    }

}
