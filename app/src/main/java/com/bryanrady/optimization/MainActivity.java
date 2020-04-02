package com.bryanrady.optimization;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bryanrady.optimization.advertisement.PlayerActivity;
import com.bryanrady.optimization.base.component.ComponentActivity;
import com.bryanrady.optimization.base.component.activity.FirstActivity;
import com.bryanrady.optimization.battery.BatteryActivity;
import com.bryanrady.optimization.bean.Student;
import com.bryanrady.optimization.bean.Student2;
import com.bryanrady.optimization.bitmap.BitmapActivity;
import com.bryanrady.optimization.leaked.FixLeakedUtils;
import com.bryanrady.optimization.leaked.LeakedActivity;
import com.bryanrady.optimization.shake.ShakeActivity;
import com.bryanrady.optimization.handler.HandlerActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void leaked(View view) {
        Intent intent = new Intent(this, LeakedActivity.class);
        Student stu = new Student();
        stu.setAge(20);
        stu.setName("张三");
        intent.putExtra("stu",stu);

        Student2 stu2 = new Student2();
        stu2.setAge(21);
        stu2.setName("李四");
        intent.putExtra("stu2",stu2);
        startActivity(intent);
    }

    public void shake(View view) {
        Intent intent = new Intent(this, ShakeActivity.class);
        startActivity(intent);
    }

    public void wheel_play(View view) {
        Intent intent = new Intent(this, PlayerActivity.class);
        startActivity(intent);
    }

    public void bitmap(View view) {
        Intent intent = new Intent(this, BitmapActivity.class);
        startActivity(intent);
    }

    public void battery(View view) {
        Intent intent = new Intent(this, BatteryActivity.class);
        startActivity(intent);
    }

    public void handler(View view) {
        Intent intent = new Intent(this, HandlerActivity.class);
        startActivity(intent);
    }

    public void component(View view) {
        Intent intent = new Intent(this, ComponentActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //华为手机引发的泄漏
        FixLeakedUtils.fixInputMethodManagerLastSrvView(this);
        FixLeakedUtils.fixInputMethodManagerLeak(this);
    }

}
