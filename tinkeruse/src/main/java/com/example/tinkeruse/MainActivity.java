package com.example.tinkeruse;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import com.tencent.tinker.lib.tinker.TinkerInstaller;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void loadPatch(View view) {
        String patchLocation = Environment.getExternalStorageDirectory().getAbsolutePath() + "/patch_signed_7zip.apk";
        TinkerInstaller.onReceiveUpgradePatch(getApplicationContext(), patchLocation);
    }

    public void result(View view) {
        Test test = new Test();
        test.test(this);
    }
}
