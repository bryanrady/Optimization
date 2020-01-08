package com.bryanrady.optimization.leaked;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bryanrady.optimization.R;

import androidx.appcompat.app.AppCompatActivity;

public class LeakedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaked);
    }

    public void static_field(View view) {
        Intent intent = new Intent(this, StaticFieldLeakActivity.class);
        startActivity(intent);
    }

    public void non_static_inner(View view) {
        Intent intent = new Intent(this, NonStaticInnerClassLeakActivity.class);
        startActivity(intent);
    }

    public void anonymous_inner(View view) {
        Intent intent = new Intent(this, AnonymousInnerClassLeakActivity.class);
        startActivity(intent);
    }

    public void single_instance(View view) {
        Intent intent = new Intent(this, SingleInstanceLeakActivity.class);
        startActivity(intent);
    }

    public void unregister_remove(View view) {
        Intent intent = new Intent(this, UnRegisterRemoveLeakActivity.class);
        startActivity(intent);
    }

    public void resource_not_close(View view) {
        Intent intent = new Intent(this, ResourceNotCloseLeakActivity.class);
        startActivity(intent);
    }

    public void map_set_not_clear(View view) {
        Intent intent = new Intent(this, MapSetListLeakActivity.class);
        startActivity(intent);
    }

    public void webView(View view) {
        Intent intent = new Intent(this, WebViewLeakActivity.class);
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
