package com.bryanrady.optimization.base.component.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 创建数据库
 * @author: wangqingbin
 * @date: 2020/4/10 10:32
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "bryanrady.db";
    private static final int DATABASE_VERSION = 1;
    public static final String USER_TABLE_NAME = "user";

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS "
                + USER_TABLE_NAME
                + "(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "id INTEGER,"
                + "name TEXT,"
                + "age INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
