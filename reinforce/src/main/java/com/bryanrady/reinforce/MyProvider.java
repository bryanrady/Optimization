package com.bryanrady.reinforce;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author Lance
 * @date 2017/12/28
 */

public class MyProvider extends ContentProvider {

    public static final String TAG = "MyProvider";


    /**
     * Provider的onCreate()函数在Application的onCreate()函数之前。    installContentProviders(app, data.providers);
     *       installContentProviders（）函数实在我们没有替换Application之前就调用的，所以ContentProvider没有成功打印出自己的
     *
     *
     *
     * @return
     */
    @Override
    public boolean onCreate() {
        Log.e(TAG, "provider onCreate:" + getContext());
        Log.e(TAG, "provider onCreate:" + getContext().getApplicationContext());
        Log.e(TAG, "provider onCreate:" + getContext().getApplicationInfo().className);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String
            selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[]
            selectionArgs) {
        Log.e(TAG, "provider delete:" + getContext());
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String
            selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
