package com.example.reinforce_tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * 用来加密的dex的工程
 * Created by Administrator on 2018/1/22 0022.
 */
public class Main {

    /**
     *      reinforce-core      android library工程   专门用来进行dex解密的
     *
     *          library         编译后生成的文件是   arr
     *          application     编译后生成的文件是   apk
     *
     *      reinforce-tools     java工程              专门用来进行dex加密的
     *
     */

    public static void main(String[] args) throws Exception {
        /**
         * 1、制作 reinforce-core 解密工程的 classes.dex文件，存在于reinforce-tools工程的temp目录下
         */
        //(1) 将reinforce-core生成的reinforce-core-debug.aar文件解压到reinforce-tools的temp目录下面
        File coreAarFile = new File("reinforce-core/build/outputs/aar/reinforce-core-debug.aar");
        File toolsTemp = new File("reinforce-tools/temp");
        Zip.unZip(coreAarFile, toolsTemp);

        //(2) 得到reinforce-tools工程下面temp目录的classes.jar文件  执行dx命令 将classes.jar文件变成同级目录下的classes.dex文件
        File toolsClassesJar = new File(toolsTemp, "classes.jar");
        File toolsClassesDex = new File(toolsTemp, "classes.dex");
        //执行命令  windows:cmd /c  linux/mac不需要（cmd /c）
        Process process = Runtime.getRuntime().exec(
                "cmd /c dx --dex --output "
                + toolsClassesDex.getAbsolutePath() + " " + toolsClassesJar.getAbsolutePath());
        process.waitFor();
        if (process.exitValue() != 0) {
            throw new RuntimeException("classes.jar change to classes.dex error");
        }

        /**
         * 2、将reinforce工程的apk进行解压，然后对reinforce工程的apk解压后的所有dex文件进行加密
         */
        //(1) 将reinforce工程的reinforce-debug.apk文件解压到一个temp目录下
        File reinforceApkFile = new File("reinforce/build/outputs/apk/debug/reinforce-debug.apk");
        File reinforceApkTemp = new File("reinforce/build/outputs/apk/debug/temp");
        Zip.unZip(reinforceApkFile, reinforceApkTemp);

        //(2)获取reinforce工程temp目录下的所有dex文件
        File[] dexFiles = reinforceApkTemp.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                return s.endsWith(".dex");
            }
        });

        //(3) 使用AES加密算法加密reinforce工程的所有dex文件
        AES.init(AES.DEFAULT_PWD);
        for (File dex : dexFiles) {
            //读取dex文件数据
            byte[] bytes = getBytes(dex);
            //加密
            byte[] encrypt = AES.encrypt(bytes);
            //写到指定文件
            FileOutputStream fos = new FileOutputStream(new File(reinforceApkTemp, "secret-" + dex.getName()));
            fos.write(encrypt);
            fos.flush();
            fos.close();
            //把已经加了密的dex文件进行删除
            dex.delete();
        }

        /**
         * 3、将reinforce-tools工程的temp目录下面的classes.dex文件放到reinforce工程下面的temp目录，
         *    然后将reinforce工程的temp目录下的所有文件压缩成reinforce-unsigned.apk
         */
        toolsClassesDex.renameTo(new File(reinforceApkTemp, "classes.dex"));
        File unSignedApk = new File("reinforce/build/outputs/apk/debug/reinforce-unsigned.apk");
        Zip.zip(reinforceApkTemp, unSignedApk);

        /**
         *  4、对齐与签名  Android用户指南
         */
        //(1) 对齐
        //对齐命令: zipalign -v -p 4 reinforce-unsigned.apk reinforce-unsigned-aligned.apk
        File alignedApk = new File("reinforce/build/outputs/apk/debug/reinforce-unsigned-aligned.apk");
        //26.0.2不认识-p参数
        process = Runtime.getRuntime().exec("cmd /c zipalign -f 4 "
                + unSignedApk.getAbsolutePath() + " " + alignedApk.getAbsolutePath());
        process.waitFor();
        if (process.exitValue() != 0) {
            throw new RuntimeException("zipalign failed");
        }

        //(2) 签名
        //签名命令: apksigner sign  --ks jks文件地址 --ks-key-alias 别名 --ks-pass pass:jsk密码 --key-pass  pass:别名密码 --out  out.apk in.apk
        File signedApk = new File("reinforce/build/outputs/apk/debug/reinforce-signed-aligned.apk");
        File jks = new File("reinforce-tools/reinforce.jks");
        process = Runtime.getRuntime().exec("cmd /c apksigner sign  --ks " + jks.getAbsolutePath()
                + " --ks-key-alias reinforce --ks-pass pass:123456 --key-pass  pass:123456 --out"
                + " " + signedApk.getAbsolutePath() + " " + alignedApk.getAbsolutePath());
        process.waitFor();
        if (process.exitValue() != 0) {
            throw new RuntimeException("apksigner failed");
        }

    }

    public static byte[] getBytes(File file) throws Exception {
        RandomAccessFile r = new RandomAccessFile(file, "r");
        byte[] buffer = new byte[(int) r.length()];
        r.readFully(buffer);
        r.close();
        return buffer;
    }
}
