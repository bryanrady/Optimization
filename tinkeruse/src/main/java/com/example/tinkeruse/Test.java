package com.example.tinkeruse;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2018/1/24 0024.
 */

public class Test {

    public void test(Context context) {
        int i=10;
        int j=0;
        Toast.makeText(context," 计算结果 "+(i/j), Toast.LENGTH_SHORT).show();
    }

}
