package com.bryanrady.optimization.battery;

import android.os.Bundle;

import com.bryanrady.optimization.R;

import androidx.appcompat.app.AppCompatActivity;

public class BatteryActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battery);
        //注意动态权限

        Battery.addDozeWhite(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
