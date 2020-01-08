package com.bryanrady.optimization.leaked;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.bryanrady.optimization.R;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.sql.SQLData;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * 资源未关闭或未释放造成的内存泄露
 * @author: wangqingbin
 * @date: 2020/1/6 17:01
 */
public class ResourceNotCloseLeakActivity extends AppCompatActivity {

    private DatabaseHelper mHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaked_model);

        TextView tv = findViewById(R.id.tv_prompt);
        tv.setText("资源连接未关闭或未正常释放造成的内存泄露");

        mHelper = new DatabaseHelper(this, "person.db", null, 1);
        SQLiteDatabase database = mHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name","张三");
        cv.put("age",24);
        database.insert("user",null, cv);
        database.close();

        String path = "sssssssss";
        String readFile1 = readFile(path);
        String readFile = readFile2(path);
        Log.e("wangqingbin","readFile=="+readFile);

        String name = getName();
        Log.e("wangqingbin","name=="+name);
        if(!TextUtils.isEmpty(name)){
            tv.setText(name);
        }

    }

    /**
     * 读文件 存在内存泄漏隐患
     * @param path
     * @return
     */
    private String readFile(String path){
        InputStream is;
        BufferedReader br;
        try {
            is = new FileInputStream(path);
            br = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer sb = new StringBuffer();
            while ((line = br.readLine()) != null){
                sb.append(line);
                sb.append("\n");
            }
            //一旦在读文件的过程中发生异常，下面两句关闭流的代码就得不到关闭，就会造成内存泄漏
            // 解决办法就是在 finally 代码块执行关闭流操作,这样能确保流被关闭
            is.close();
            br.close();
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String readFile2(String path){
        InputStream is = null;
        BufferedReader br = null;
        try {
            is = new FileInputStream(path);
            br = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer sb = new StringBuffer();
            while ((line = br.readLine()) != null){
                sb.append(line);
                sb.append("\n");
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if(is != null){
                    is.close();
                }
                if(br != null){
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private String getName(){
        SQLiteDatabase database = null;
        Cursor cursor = null;
        try{
            database = mHelper.getReadableDatabase();
            //创建游标对象
            cursor = database.query("user", new String[]{"name"}, null, null, null, null, null);
            //利用游标遍历所有数据对象
            //为了显示全部，把所有对象连接起来，放到TextView中
            StringBuffer sb = new StringBuffer();
            while(cursor.moveToNext()){
                String name = cursor.getString(cursor.getColumnIndex("name"));
                sb.append(name);
                sb.append("\n");
            }
            return sb.toString();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            // 关闭游标，释放资源
            if(cursor != null){
                cursor.close();
            }
            if(database != null){
                database.close();
            }
        }
        return null;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper{

        public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            //创建数据库sql语句 并 执行
            String sql = "create table if not exists user(" +
                    "id integer primary key autoincrement," +
                    "name varchar(20)," +
                    "age integer)";
            db.execSQL(sql);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //华为手机引发的泄漏
        FixLeakedUtils.fixInputMethodManagerLastSrvView(this);
        FixLeakedUtils.fixInputMethodManagerLeak(this);

        SQLiteDatabase database = mHelper.getWritableDatabase();
        database.delete("user", "name=?", new String[]{"张三"});
        database.close();
    }
}
