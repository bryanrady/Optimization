package com.bryanrady.optimization.handler;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import com.bryanrady.optimization.R;
import com.bryanrady.optimization.leaked.FixLeakedUtils;

import androidx.appcompat.app.AppCompatActivity;

public class IdleHandlerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler);

        MyAsyncTask asyncTask = new MyAsyncTask();
        asyncTask.execute();
    }

    static class MyAsyncTask extends AsyncTask<String,Integer,String>{

        @Override
        protected String doInBackground(String... strings) {
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //华为手机引发的泄漏
        FixLeakedUtils.fixInputMethodManagerLastSrvView(this);
        FixLeakedUtils.fixInputMethodManagerLeak(this);
    }

    public void handler_thread(View view) {
        Intent intent = new Intent(this, IdleHandlerActivity.class);
        startActivity(intent);
    }

    public void pool(View view) {
        Intent intent = new Intent(this, IdleHandlerActivity.class);
        startActivity(intent);
    }

}
