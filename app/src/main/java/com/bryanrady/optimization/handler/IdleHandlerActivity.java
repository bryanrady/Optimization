package com.bryanrady.optimization.handler;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bryanrady.optimization.R;
import com.bryanrady.optimization.leaked.FixLeakedUtils;

import androidx.appcompat.app.AppCompatActivity;

public class IdleHandlerActivity extends AppCompatActivity implements DataModel.OnDataChangeListener
        ,DataModel2.OnDataChangeListener{

    private DataModel mDataModel;
    private DataModel2 mDataModel2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idlehandler);

//        mDataModel = new DataModel();
//        mDataModel.setOnDataChangeListener(this);

        mDataModel2 = new DataModel2();
        mDataModel2.setOnDataChangeListener(this);

    }

    public void sync(View view) {
//        mDataModel.syncData();

        mDataModel2.syncData();
    }

    @Override
    public void onDataChange() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.e("wangqingbin","更新数据111");
            }
        });
    }

    @Override
    public void onDataChange2() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.e("wangqingbin","更新数据2222");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //华为手机引发的泄漏
        FixLeakedUtils.fixInputMethodManagerLastSrvView(this);
        FixLeakedUtils.fixInputMethodManagerLeak(this);
    }

}
