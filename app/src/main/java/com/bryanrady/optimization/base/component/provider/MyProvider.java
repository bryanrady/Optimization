package com.bryanrady.optimization.base.component.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * https://www.jianshu.com/p/ea8bc4aaf057
 * @author: wangqingbin
 * @date: 2020/4/10 10:39
 */
public class MyProvider extends ContentProvider {

    public static final String AUTHORITIES = "com.bryanrady.optimization.MyProvider";
    public static final int USER_URI_CODE = 1;
    private static UriMatcher mUriMatcher;

    private SQLiteDatabase mDatabase;

    static {
        //使用UriMatcher注册uri
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMatcher.addURI(AUTHORITIES,"user", USER_URI_CODE);
        // 若uri资源路径 = content://com.bryanrady.optimization/user ，则返回注册码USER_URI_CODE
    }

    @Override
    public boolean onCreate() {
        //初始化数据库
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        mDatabase = dbHelper.getWritableDatabase();

        return true;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        int matchCode = mUriMatcher.match(uri);
        long row;
        if (matchCode == USER_URI_CODE){
            //如果找到这个uri，向表中添加数据，返回插入的数据的行号
            row = mDatabase.insert(DatabaseHelper.USER_TABLE_NAME, null, contentValues);
        }else{
            throw new IllegalArgumentException("unSupport Uri : " + uri);
        }
        //插入成功
        if (row > 0){
            // 当该uri的ContentProvider数据发生变化时，通知外界（即访问该ContentProvider数据的访问者）
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return uri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int matchCode = mUriMatcher.match(uri);
        int row;
        if (matchCode == USER_URI_CODE){
            row = mDatabase.delete(DatabaseHelper.USER_TABLE_NAME, selection, selectionArgs);
        }else{
            throw new IllegalArgumentException("unSupport Uri : " + uri);
        }
        if (row > 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return row;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        int matchCode = mUriMatcher.match(uri);
        int row;
        if (matchCode == USER_URI_CODE){
            row = mDatabase.update(DatabaseHelper.USER_TABLE_NAME, contentValues, selection, selectionArgs);
        }else{
            throw new IllegalArgumentException("unSupport Uri : " + uri);
        }
        if (row > 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return row;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        int matchCode = mUriMatcher.match(uri);
        Cursor cursor;
        if (matchCode == USER_URI_CODE){
            //查询该表中的数据
            cursor = mDatabase.query(DatabaseHelper.USER_TABLE_NAME, projection, selection, null,
                    null, sortOrder, null);
        }else{
            throw new IllegalArgumentException("unSupport Uri : " + uri);
        }
        return cursor;
    }


    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }


}
