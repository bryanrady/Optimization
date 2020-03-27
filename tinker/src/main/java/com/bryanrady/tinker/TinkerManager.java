package com.bryanrady.tinker;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;

import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

/**
 * @author: wangqingbin
 * @date: 2020/3/27 14:12
 */
public class TinkerManager {

    private static HashSet<File> mLoadDexSet = new HashSet<>();

    static {
        mLoadDexSet.clear();
    }

    public static void fix(Context context) {
        try {
            loadDex(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载odex目录下的所有dex文件
     * @param context
     */
    private static void loadDex(Context context) throws Exception{
        if (context == null) {
            return;
        }
        File odexDir = context.getDir("odex", Context.MODE_PRIVATE);
        //1.先获取odex目录下面的所有文件
        File[] files = odexDir.listFiles();
        for (File file : files){
            if (file.getName().startsWith("classes") || file.getName().endsWith(".dex")){
                Log.d("wangqingbin","found dex file: " + file.getName());
                mLoadDexSet.add(file);
            }
        }
        //optimizedDirectory dex文件缓存路径
        //librarySearchPath  dex文件中包含的lib库查找路径
        String optimizedDirectory = odexDir.getAbsolutePath() + File.separator + "opt_dex";
        for (File dex : mLoadDexSet){

            Class<?> baseDexClassLoaderClass = Class.forName("dalvik.system.BaseDexClassLoader");

            //2. 找到应用中原来的Element数组(DexClassLoader可以用来加载外部的dex文件)
            DexClassLoader dexClassLoader = new DexClassLoader(dex.getAbsolutePath(), optimizedDirectory,
                    null, context.getClassLoader());
            Field myPathListField = baseDexClassLoaderClass.getDeclaredField("pathList");
            myPathListField.setAccessible(true);
            Object myPathList = myPathListField.get(dexClassLoader);

            Field myDexElementsField = myPathList.getClass().getDeclaredField("dexElements");
            myDexElementsField.setAccessible(true);
            Object myDexElements = myDexElementsField.get(myPathList);

            //3. 找到应用中原来的Element数组
            PathClassLoader pathClassLoader = (PathClassLoader) context.getClassLoader();
            Field pathListField = baseDexClassLoaderClass.getDeclaredField("pathList");
            pathListField.setAccessible(true);
            Object pathList = pathListField.get(pathClassLoader);

            Field dexElementsField = pathList.getClass().getDeclaredField("dexElements");
            dexElementsField.setAccessible(true);
            Object dexElements = dexElementsField.get(pathList);

            //4.融合两个Elements数组  将自己的Elements数组融合到系统的数组
            //获得dexElements的类型
            Class<?> componentTypeClass = dexElements.getClass().getComponentType();
            //获取数组长度
            int systemLength = Array.getLength(dexElements);
            int myLength = Array.getLength(myDexElements);
            int newLength = systemLength + myLength;
            //生成一个新的dexElements数组
            Object newDexElements = Array.newInstance(componentTypeClass, newLength);
            for (int i = 0; i < newLength; i++){
                //这里要保证修复的dex文件在dexElements数组的前面，才能达到修复
                if (i <myLength){
                    Array.set(newDexElements, i, Array.get(myDexElements, i));
                }else{
                    Array.set(newDexElements, i, Array.get(dexElements,i-myLength));
                }
            }

            //5.将系统里面的dexElements数组替换为新的Elements数组
            dexElementsField.set(pathList,newDexElements);
        }

    }

    /**
     * 将外置卡中的dex文件复制到data/data/packageName/odex/目录下
     * @param context
     */
    public static void moveSdcardDex2Odex(Context context){
        File odexDir = context.getDir("odex", Context.MODE_PRIVATE);
        String name = "out.dex";
        String filePath = new File(odexDir, name).getAbsolutePath();
        Log.d("wangqingbin","filePath == " + filePath);
        File file = new File(filePath);
        if (file.exists()){
            file.delete();
        }
        //先从外置卡的读取out.dex文件
        InputStream is = null;
        OutputStream os = null;
        try {
            Log.d("wangqingbin","sdcard dex path == " + new File(Environment.getExternalStorageDirectory(), name).getAbsolutePath());
            is = new FileInputStream(new File(Environment.getExternalStorageDirectory(), name));
            os = new FileOutputStream(filePath);
            int len;
            byte[] buffer = new byte[1024];
            while ((len = is.read(buffer)) != -1){
                os.write(buffer,0, len);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (os != null){
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
