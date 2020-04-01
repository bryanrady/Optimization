package com.bryanrady.optimization.base.component.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bryanrady.optimization.R;

/**
 * @author: wangqingbin
 * @date: 2020/4/1 15:47
 */
public class SingleInstanceActivity extends AppCompatActivity {

    private String mKey;

    private void initData(){
        mKey = getIntent().getStringExtra("key");
        Log.d("wangqingbin","SingleInstance  key === " + mKey);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode);
        Log.d("wangqingbin","SingleInstance  onCreate........");
        initData();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("wangqingbin","SingleInstance  onRestart........");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("wangqingbin","SingleInstance  onStart........");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("wangqingbin","SingleInstance  onResume........");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("wangqingbin","SingleInstance  onPause........");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("wangqingbin","SingleInstance  onStop........");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("wangqingbin","SingleInstance  onDestroy........");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d("wangqingbin","SingleInstance  onNewIntent........");
        setIntent(intent);
        initData();
    }

    public void jump(View view) {
        Intent intent = new Intent(this, ThirdActivity.class);
        startActivity(intent);
    }

//            04-01 21:37:02.696 3194-3194/com.bryanrady.optimization D/wangqingbin: A  onCreate........
//            04-01 21:37:02.696 3194-3194/com.bryanrady.optimization D/wangqingbin: A  onStart........
//            04-01 21:37:02.696 3194-3194/com.bryanrady.optimization D/wangqingbin: A  onResume........

//            04-01 21:37:03.686 3194-3194/com.bryanrady.optimization D/wangqingbin: A  onPause........
//            04-01 21:37:03.756 3194-3194/com.bryanrady.optimization D/wangqingbin: SingleInstance  onCreate........
//            04-01 21:37:03.756 3194-3194/com.bryanrady.optimization D/wangqingbin: SingleInstance  key === new key
//            04-01 21:37:03.766 3194-3194/com.bryanrady.optimization D/wangqingbin: SingleInstance  onStart........
//            04-01 21:37:03.766 3194-3194/com.bryanrady.optimization D/wangqingbin: SingleInstance  onResume........
//            04-01 21:37:04.066 3194-3194/com.bryanrady.optimization D/wangqingbin: A  onStop........

//            04-01 21:37:04.656 3194-3194/com.bryanrady.optimization D/wangqingbin: SingleInstance  onPause........
//            04-01 21:37:04.706 3194-3194/com.bryanrady.optimization D/wangqingbin: C  onCreate........
//            04-01 21:37:04.706 3194-3194/com.bryanrady.optimization D/wangqingbin: C  onStart........
//            04-01 21:37:04.706 3194-3194/com.bryanrady.optimization D/wangqingbin: C  onResume........
//            04-01 21:37:05.116 3194-3194/com.bryanrady.optimization D/wangqingbin: SingleInstance  onStop........

//            04-01 21:37:09.046 3194-3194/com.bryanrady.optimization D/wangqingbin: C  onPause........
//            04-01 21:37:09.056 3194-3194/com.bryanrady.optimization D/wangqingbin: SingleInstance  onNewIntent........
//            04-01 21:37:09.056 3194-3194/com.bryanrady.optimization D/wangqingbin: SingleInstance  key === update key
//            04-01 21:37:09.056 3194-3194/com.bryanrady.optimization D/wangqingbin: SingleInstance  onRestart........
//            04-01 21:37:09.056 3194-3194/com.bryanrady.optimization D/wangqingbin: SingleInstance  onStart........
//            04-01 21:37:09.056 3194-3194/com.bryanrady.optimization D/wangqingbin: SingleInstance  onResume........
//            04-01 21:37:09.406 3194-3194/com.bryanrady.optimization D/wangqingbin: C  onStop........

}
