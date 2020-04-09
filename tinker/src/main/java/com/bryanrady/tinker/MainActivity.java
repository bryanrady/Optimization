package com.bryanrady.tinker;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @author: wangqingbin
 * @date: 2020/3/27 12:06
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void send(View view) {
        Intent intent = new Intent();
        intent.setAction("custom_action");
        intent.putExtra("data","have permission");
        intent.setComponent(new ComponentName("com.bryanrady.optimization",
                "com.bryanrady.optimization.base.component.receiver.PermissionBroadcastReceiver"));
        sendBroadcast(intent, "com.bryanrady.custom.BroadcastReceiver");

        Intent intent2 = new Intent();
        intent2.setAction("custom_action");
        intent2.putExtra("data","no permission");
        intent2.setComponent(new ComponentName("com.bryanrady.optimization",
                "com.bryanrady.optimization.base.component.receiver.NonPermissionBroadcastReceiver"));
        sendBroadcast(intent2);
    }

    public void test(View view) {
        Bug bug = new Bug();
        bug.test(this);
    }

    public void fix(View view) {
        TinkerManager.moveSdcardDex2Odex(this);
        TinkerManager.fix(this);
    }

}
