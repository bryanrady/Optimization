package com.bryanrady.reinforce_core;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import dalvik.system.BaseDexClassLoader;
import dalvik.system.PathClassLoader;

/**
 * 这个工程用来解密与加载多dex文件
 * @author: wangqingbin
 * @date: 2020/3/26 16:25
 */
public class ProxyApplication extends Application {

    private String app_name;
    private String app_version;

    /**
     * ActivityThread 创建 Application后调用的第一个函数
     * @param base
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        /**
         * 1.解密
         */
        //(1)获得当前的apk文件
        File apkFile = new File(getApplicationInfo().sourceDir);

        //(2)创建目录存放dex文件
        getMetaData();
        if (app_name == null || app_version == null){
            throw new RuntimeException("must bu set meta-data");
        }
        // data/data/packagename/app_name_app_version
        File versionDir = getDir(app_name + "_" + app_version, MODE_PRIVATE);
        //存放apk解压后的所有文件及文件夹
        File appDir = new File(versionDir,"'app");
        //存放appDir目录中需要解密的所有dex文件
        File dexDir = new File(appDir, "dexDir");

        //(3)提取我们需要加载的所有dex文件
        List<File> dexFiles =  new ArrayList<>();
        //第一次需要解密(如果在考虑安全一点，最好对文件坐下MD5校验)
        if (!dexDir.exists() && dexDir.list().length == 0){
            Zip.unZip(apkFile, appDir);
            File[] files = appDir.listFiles();
            for (File file : files){
                //不能加密主dex,所以这里主dex也不用进行解密
                if (file.getName().endsWith(".dex") && !"classes.dex".equals(file.getName())){
                    try {
                        //从文件中读取 byte数组 加密后的dex数据
                        byte[] bytes = DecrptUtils.getBytes(file);
                        //将dex 文件 解密 并且写入 原文件file目录
                        DecrptUtils.decrypt(bytes, file.getAbsolutePath());
                        dexFiles.add(file);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }else{
            //第二次后直接从目录中拿，不需要解密了
            File[] files = appDir.listFiles();
            for (File file : files){
                dexFiles.add(file);
            }
        }

        /**
         * 2. 加载解密后的多dex
         */
        loadDex(dexFiles, versionDir);
    }

    /**
     * 将ProxyApplication替换MyApplication    如果在attachBaseContext()里面替换的话，Application又会被替换回原来的，具体看源码逻辑
     */
    @Override
    public void onCreate() {
        super.onCreate();
        try {
            replaceApplication();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Application mRealApplication;
    boolean isBindReal;

    private void replaceApplication() throws Exception {
        if (isBindReal){
            return;
        }
        Context baseContext = getBaseContext();
        //如果使用这个库的开发者)没有配置Application 就不用管了
        if (TextUtils.isEmpty(app_name)){
            return;
        }
        /**
         * 获得要真正的Application  MyApplication
         */
        Class<?> applicationClass = Class.forName(app_name);
        mRealApplication = (Application) applicationClass.newInstance();
        Method attachMethod = applicationClass.getDeclaredMethod("attach", Context.class);
        attachMethod.setAccessible(true);
        attachMethod.invoke(mRealApplication,baseContext);

        /**
         *  替换
         *  ContextImpl -> mOuterContext ProxyApplication->MyApplication
         */
        Class<?> contextImplClass = Class.forName("android.app.ContextImpl");
        Field mOuterContextField = contextImplClass.getDeclaredField("mOuterContext");
        mOuterContextField.setAccessible(true);
        mOuterContextField.set(baseContext,mRealApplication);

        /**
         * ActivityThread  mAllApplications 与 mInitialApplication
         */
        //获得ActivityThread对象 ActivityThread 可以通过 ContextImpl 的 mMainThread 属性获得
        Field mMainThreadField = contextImplClass.getDeclaredField("mMainThread");
        mMainThreadField.setAccessible(true);
        Object mMainThread = mMainThreadField.get(baseContext);

        //替换 ActivityThread 里面的 mInitialApplication
        Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
        Field mInitialApplicationField = activityThreadClass.getDeclaredField("mInitialApplication");
        mInitialApplicationField.setAccessible(true);
        mInitialApplicationField.set(mMainThread,mRealApplication);

        //替换 mAllApplications
        Field mAllApplicationsField = activityThreadClass.getDeclaredField("mAllApplications");
        mAllApplicationsField.setAccessible(true);
        ArrayList<Application> mAllApplications = (ArrayList<Application>) mAllApplicationsField.get(mMainThread);
        mAllApplications.remove(this);
        mAllApplications.add(mRealApplication);


        /**
         * LoadedApk -> mApplication ProxyApplication
         */
        //LoadedApk 可以通过 ContextImpl 的 mPackageInfo 属性获得
        Field mPackageInfoField = contextImplClass.getDeclaredField("mPackageInfo");
        mPackageInfoField.setAccessible(true);
        Object mPackageInfo = mPackageInfoField.get(baseContext);

        Class<?> loadedApkClass = Class.forName("android.app.LoadedApk");
        Field mApplicationField = loadedApkClass.getDeclaredField("mApplication");
        mApplicationField.setAccessible(true);
        mApplicationField.set(mPackageInfo,mRealApplication);

        //修改ApplicationInfo className LoadedApk
        Field mApplicationInfoField = loadedApkClass.getDeclaredField("mApplicationInfo");
        mApplicationInfoField.setAccessible(true);
        ApplicationInfo mApplicationInfo = (ApplicationInfo) mApplicationInfoField.get(mPackageInfo);
        mApplicationInfo.className = app_name;

        mRealApplication.onCreate();
        isBindReal = true;
    }

    @Override
    public String getPackageName() {
        //如果meta-data 设置了 application
        //让ContentProvider创建的时候使用的上下文 在ActivityThread中的installProvider函数
        //命中else
        if (!TextUtils.isEmpty(app_name)){
            return "";
        }
        return super.getPackageName();
    }


    @Override
    public Context createPackageContext(String packageName, int flags) throws PackageManager.NameNotFoundException {
        if (TextUtils.isEmpty(app_name)){
            return super.createPackageContext(packageName, flags);
        }
        try {
            replaceApplication();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mRealApplication;
    }

    /**
     * 加载dex文件
     * @param dexFiles
     * @param optimizedDirectory
     */
    private void loadDex(List<File> dexFiles, File optimizedDirectory){
        try {
            //(1)获得系统的dexElements数组
            Field pathListField = ReflexUtils.findField(getClassLoader(), "pathList");
            Object pathList =  pathListField.get(getClassLoader());

            Field dexElementsField = ReflexUtils.findField(pathList, "dexElements");
            Object[] dexElements = (Object[]) dexElementsField.get(pathList);

            //(2)创建一个新的elements数组
            Method makeDexElements = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                makeDexElements = ReflexUtils.findMethod(pathList, "makeDexElements",
                        ArrayList.class, File.class, ArrayList.class);
            } else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                makeDexElements = ReflexUtils.findMethod(pathList, "makePathElements",
                        ArrayList.class, File.class, ArrayList.class);
            }
            ArrayList<IOException> suppressedExceptions = new ArrayList<IOException>();
            Object[] addElements = (Object[]) makeDexElements.invoke(pathList, dexFiles, optimizedDirectory, suppressedExceptions);

            //(3)合并两个数组
            Object[] newElements = (Object[]) Array.newInstance(dexElements.getClass().getComponentType(),
                    dexElements.length + addElements.length);
            System.arraycopy(dexElements, 0, newElements, 0, dexElements.length);
            System.arraycopy(addElements, 0, newElements, dexElements.length, addElements.length);

            //(4)替换DexPathList里面的dexElements数组
            dexElementsField.set(pathList, newElements);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取meta属性
     */
    private void getMetaData(){
        try {
            ApplicationInfo applicationInfo = getPackageManager().getApplicationInfo(getPackageName(),
                    PackageManager.GET_META_DATA);
            Bundle metaData = applicationInfo.metaData;
            if (metaData != null){
                if (metaData.containsKey("app_name")){
                    app_name = metaData.getString("app_name");
                }
                if (metaData.containsKey("app_version")){
                    app_version = metaData.getString("app_version");
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

//        Bundle metaData = getApplicationInfo().metaData;
//        if (metaData != null){
//            if (metaData.containsKey("app_name")){
//                app_name = metaData.getString("app_name");
//            }
//            if (metaData.containsKey("app_version")){
//                app_version = metaData.getString("app_version");
//            }
//        }
    }


}
