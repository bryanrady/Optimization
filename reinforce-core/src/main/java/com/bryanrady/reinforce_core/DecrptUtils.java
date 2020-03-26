package com.bryanrady.reinforce_core;

import java.io.File;
import java.io.RandomAccessFile;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author: wangqingbin
 * @date: 2020/3/26 17:23
 */
public class DecrptUtils {

    static {
        System.loadLibrary("dn_ssl");
    }

    public static native void decrypt(byte[] data,String path);


    /**
     * 读取文件
     * @param file
     * @return
     * @throws Exception
     */
    public static byte[] getBytes(File file) throws Exception {
        RandomAccessFile r = new RandomAccessFile(file, "r");
        byte[] buffer = new byte[(int) r.length()];
        r.readFully(buffer);
        r.close();
        return buffer;
    }

}
