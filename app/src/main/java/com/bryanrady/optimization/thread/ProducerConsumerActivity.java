package com.bryanrady.optimization.thread;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 *
 */
public class ProducerConsumerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Producer().start();
        new Consumer().start();
    }

    static class Product{
        public static String value = null;
    }

    static class Producer extends Thread{
        @Override
        public void run() {
            super.run();
            while (true){
                //如果没有产品就生产
                if(Product.value == null){
                    Product.value = "NO:"+ System.currentTimeMillis();
                    Log.d("wangqingbin","生产产品: "+ Product.value);
                }
            }
        }
    }

    static class Consumer extends Thread{
        @Override
        public void run() {
            super.run();
            while (true){
                //如果有产品就消费
                if(Product.value != null){
                    Log.d("wangqingbin","消费产品: "+ Product.value);
                    Product.value = null;
                }
            }
        }
    }
}
