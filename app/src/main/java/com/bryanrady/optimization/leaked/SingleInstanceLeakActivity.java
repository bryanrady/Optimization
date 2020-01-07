package com.bryanrady.optimization.leaked;

import android.os.Bundle;
import android.widget.TextView;

import com.bryanrady.optimization.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * 单例模式持有Activity或者Service的引用造成的内存泄漏
 * @author: wangqingbin
 * @date: 2020/1/6 17:01
 */
public class SingleInstanceLeakActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaked_model);

        TextView tv = findViewById(R.id.tv_prompt);
        tv.setText("单例模式持有Activity或者Service的引用造成的内存泄漏");

    //    Singleton.getInstance(this);
        /**
         * 这里就会引发内存泄漏，Singleton持有Activity的引用，而Singleton又是内部自己的一个static变量，
         * 当我们退出Activity时，该Activity就没有用了，但是因为singleton作为静态单例（在应用程序的整个生命周期中存在）
         * 会继续持有这个Activity的引用，导致这个Activity对象无法被回收释放，这就造成了内存泄露。
         *
         * 修改：将context修改为Application的context
         */

        Singleton.getInstance(getApplicationContext());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //华为手机引发的泄漏
        FixLeakedUtils.fixInputMethodManagerLastSrvView(this);
        FixLeakedUtils.fixInputMethodManagerLeak(this);
    }
}
