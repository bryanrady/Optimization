package com.bryanrady.tinker;

import android.content.Context;
import android.widget.Toast;

/**
 * @author: wangqingbin
 * @date: 2020/3/27 14:26
 */
public class Bug {

    public void test(Context context){
        int result = 10 / 0;
        Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
    }

}
