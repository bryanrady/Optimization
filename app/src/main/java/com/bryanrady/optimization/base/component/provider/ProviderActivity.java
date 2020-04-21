package com.bryanrady.optimization.base.component.provider;

import android.content.ContentValues;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.bryanrady.optimization.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

/**
 * 这是进程内访问Provider 进程外也是一样的 我们可以权限控制来保证安全
 */
public class ProviderActivity extends AppCompatActivity {

    private Uri mUri;
    private ObserverHandler mHandler;
    private ContentObserver mContentObserver;

    private static class ObserverHandler extends Handler {

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider);

        mUri = Uri.parse("content://" + MyProvider.AUTHORITIES + "/user");
        mHandler = new ObserverHandler();
        //注册了观察者，只要这个uri对应的Provider有改变，就会回调到这里。
        mContentObserver = new ContentObserver(mHandler) {
            @Override
            public void onChange(boolean selfChange) {
                super.onChange(selfChange);
                Log.d("wangqingbin","onChange : selfChange = " + selfChange + " , uri = " + mUri.toString());
            }
        };
        getContentResolver().registerContentObserver(mUri,true, mContentObserver);
    }

    public void provider_insert(View view) {
        ContentValues cv = new ContentValues();
        cv.put("id",10086);
        cv.put("name", "张三");
        cv.put("age", 20);
        getContentResolver().insert(mUri, cv);
    }

    public void provider_delete(View view) {
        getContentResolver().delete(mUri,"name=?", new String[]{"张三"});
    }

    public void provider_update(View view) {
        ContentValues cv = new ContentValues();
        cv.put("name", "李四");
        cv.put("age", 21);
        getContentResolver().update(mUri, cv, "id=?", new String[]{"10086"});
    }

    public void provider_query(View view) {
        List<User> userList = queryUser();
        if (userList != null){
            for (User user : userList){
                Log.d("wangqingbin","user == " + user);
            }
        }
    }

    private List<User> queryUser(){
        List<User> list = new ArrayList<>();
        Cursor cursor = getContentResolver().query(mUri, new String[]{"id", "name", "age"}, null, null, null);
        if (cursor != null){
            while (cursor.moveToNext()){
                User user = new User();
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                int age = cursor.getInt(cursor.getColumnIndex("age"));
                user.setId(id);
                user.setName(name);
                user.setAge(age);
                list.add(user);
            }
            //记得关闭游标
            cursor.close();
        }
        return list;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mContentObserver != null){
            getContentResolver().unregisterContentObserver(mContentObserver);
        }
    }
}
