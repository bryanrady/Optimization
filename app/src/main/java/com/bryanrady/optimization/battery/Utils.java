package com.bryanrady.optimization.battery;

import java.io.Closeable;
import java.io.IOException;

/**
 * 被动获取是否wifi
 */
public class Utils {

    public static void safeColose(Closeable closeable){
        if (null != closeable){
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
