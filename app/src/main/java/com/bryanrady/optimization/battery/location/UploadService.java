package com.bryanrady.optimization.battery.location;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.bryanrady.optimization.battery.Utils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import androidx.annotation.Nullable;

/**
 * @author: wangqingbin
 * @date: 2020/1/9 17:11
 */
public class UploadService extends IntentService {


    public UploadService() {
        super("upload Location");
    }

    public static void UploadLocation(Context context, String location) {
        Intent intent = new Intent(context, UploadService.class);
        intent.putExtra("DATA", location);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String location = intent.getStringExtra("DATA");
        Log.i("wangqingbin", "IntentService 获得了位置信息:" + location);
        HttpURLConnection conn = null;
        OutputStream os = null;
        try {
            conn = (HttpURLConnection) new URL("https://www.baidu.com/")
                    .openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            os = conn.getOutputStream();
            os.write(location.getBytes());
            os.flush();
            Log.i("wangqingbin", "IntentService 上传位置信息");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Utils.safeColose(os);
            if (null != conn) {
                conn.disconnect();
            }
        }
    }
}
