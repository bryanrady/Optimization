package com.bryanrady.optimization.shake;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bryanrady.optimization.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * 内存抖动
 * @author: wangqingbin
 * @date: 2020/1/20 15:09
 */
public class ShakeActivity extends AppCompatActivity {

    private static final String TAG = ShakeActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shake);
    }

    public void shake(View view) {
        MyThread thread = new MyThread();
        thread.start();
    }

    private static class MyThread extends Thread{

        private byte[] data;

        public MyThread(){
            data = new byte[1024];
        }

        @Override
        public void run() {
            for (int i = 0;i < Integer.MAX_VALUE; i++){
                getData();
                printLog(i);
            }
        }

        private byte[] getData(){
            //这样写的话相当于在循环中一直创建byte[]数组，然后出了方法后一直被回收，频繁GC，造成内存抖动
        //    byte[] data = new byte[1024];
            return data;
        }

        private void printLog(int index){
            //字符串拼接会创建多个String对象
        //    Log.d(TAG,"打第"+ index +"个粑粑");

            //这样写就比较好
            StringBuffer sb = new StringBuffer();
            sb.append("打第");
            sb.append(index);
            sb.append("个粑粑");
            Log.d(TAG, sb.toString());
        }

    }
}
