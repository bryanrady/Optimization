package com.bryanrady.optimization.handler;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bryanrady.optimization.R;
import com.bryanrady.optimization.leaked.FixLeakedUtils;

import androidx.appcompat.app.AppCompatActivity;

public class IdleHandlerActivity extends AppCompatActivity implements DataModel.OnDataChangeListener {

    private DataModel mDataModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idlehandler);

        mDataModel = new DataModel();
        mDataModel.setOnDataChangeListener(this);
    }

    public void sync(View view) {
        mDataModel.syncData();
    }

    @Override
    public void onDataChange() {
        Log.d("wangqingbin","更新数据");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //华为手机引发的泄漏
        FixLeakedUtils.fixInputMethodManagerLastSrvView(this);
        FixLeakedUtils.fixInputMethodManagerLeak(this);
    }
}
