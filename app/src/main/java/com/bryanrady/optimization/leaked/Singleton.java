package com.bryanrady.optimization.leaked;

import android.content.Context;

import com.bryanrady.optimization.MyApplication;

public class Singleton {

    private static Singleton mInstance = null;
    private Context mContext;



    private Singleton(Context context){
        this.mContext = context;
        //因为使用的是应用生命周期的Context，所以这里也可以不用外部传递来，直接获取Application的Context即可
    //    this.mContext = MyApplication.getContext();
    }

    public static Singleton getInstance(Context context){
        if(mInstance == null){
            mInstance = new Singleton(context);
        }
        return mInstance;
    }

}
