package com.bryanrady.reinforce;

import androidx.appcompat.app.AppCompatActivity;
import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Log.e("wangqingbin",getClassLoader().getClass().getName());
//        2020-03-26 14:56:22.695 32018-32018/com.bryanrady.reinforce E/wangqingbin: dalvik.system.PathClassLoader

        Log.e("wangqingbin", "activity:" + getApplication());
        Log.e("wangqingbin", "activity:" + getApplicationContext());
        Log.e("wangqingbin", "activity:" + getApplicationInfo().className);
        startService(new Intent(this, MyService.class));

        Intent intent = new Intent("com.dongnao.broadcast.test");
        intent.setComponent(new ComponentName(getPackageName(), MyBroadCastReciver.class.getName
                ()));
        sendBroadcast(intent);

        getContentResolver().delete(Uri.parse("content://com.dongnao.myprovider/test"), null, null);

    }


    /**
     *  Android中用的类加载器有两个 PathClassLoader 和 DexClassLoader
     *
     *      PathClassLoader     只能加载系统应用和已安装应用的dex
     *
     *      DexClassLoader      支持加载apk、dex和jar，也可以从sd卡中加载。
     *
     *      类加载流程：
     *
     *      我们加载一个类是调用的loadClass（String name）, 不管是PathClassLoader还是DexClassLoader都是调用的
     *      父类BaseClassLoader的父类ClassLoader的loadClass(String name)来加载。而且PathClassLoader和DexClassLoader
     *      在初始化的时候都会调用到父类的构造函数，并且把一个parent字段的加载器最终传递到ClassLoader中来。
     *
     *      1. ClassLoader 的 loadClass(String name) 做的事情：
     *
     *          (1)如果parent加载器不为null，就先通过parent加载器来进行加载，如果加载成功直接返回加载好的类。
     *
     *          (2)如果parent加载器没有成功加载到类，就调用ClassLoader的findClass()来进行加载。而findClass()在ClassLoader
     *             中是个空实现，真正的实现是在ClassLoader的子类BaseClassLoader中实现。
     *
     *      2. BaseClassLoader 的 findClass(String name) 做的事情：
     *
     *          (1)里面直接调用了pathList的findClass()来进行类的加载,也就是调用了DexPathList类的findClass来进行类的加载。
     *             pathList是BaseClassLoader里的一个成员属性。
     *
     *          (2)pathList的初始化是在BaseClassLoader的构造函数中进行初始化的，所以在初始化PathClassLoader或者是DexClassLoader
     *            完成的时候pathList也初始化完成了，里面携带了从PathClassLoader或者是DexClassLoader的构造函数中传来的dexPath文件路径。
     *
     *      3. DexPathList 的 findClass() 做的事情：
     *
     *          (1)经过(2)、(3)dexElements数组就有值了，遍历DexPathList类中的成员变量dexElements数组，然后找到element中的dex文件
     *              DexFile，DexFile在makeDexElements()中就已经封装好了的。然后经过dex.loadClassBinaryName()就可以找到
     *
     *          (2)elements数组是在DexPathList的构造函数中进行的，里面携带了从DexPathList构造函数中传来的dexPath文件路径。
     *             elemetns数组的初始化实际上是调用makeDexElements(splitDexPath(dexPath))。splitDexPath（）就是根据文件路径
     *             进行一系列操作转成文件集合的过程。
     *
     *          (3)makeDexElements()中主要做的事情，就是遍历文件集合，找出文件集合中的dex文件(DexFile)和资源文件，然后添加
     *            进dexElements数组中。
     *
     *      4. DexFile 的 loadClassBinaryName()
     *
     *          loadClassBinaryName() -》 defineClass() --> defineClassNative（）
     *
     *          最后会调用到native函数里面进行类的加载,这样就加载出来了一个类.
     *
     *
     *  双亲委派机制
     *
     * 　　双亲委派机制是指当一个类加载器收到一个类加载请求时，该类加载器首先会把请求委派给父类加载器。每个类加载器都是如此，
     * `   只有在父类加载器在自己的搜索范围内找不到指定类时，子类加载器才会尝试自己去加载。
     *
     */

}
