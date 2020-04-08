package com.bryanrady.optimization.alive.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;

/**
 * @author: wangqingbin
 * @date: 2020/4/8 11:27
 */
public class KeepAliveActivity extends Activity {

    private static final String TAG = "KeepAliveActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "创建KeepAliveActivity");

        //创建1个像素的Activity
        Window window = getWindow();
        window.setGravity(Gravity.LEFT | Gravity.TOP);
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.x = 0;
        attributes.y = 0;
        attributes.width = 1;
        attributes.height = 1;
        window.setAttributes(attributes);

        KeepAliveManager.getInstance().setKeepAlive(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "销毁KeepAliveActivity");
    }
}
