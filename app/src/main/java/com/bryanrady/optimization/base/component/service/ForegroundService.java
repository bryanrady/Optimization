package com.bryanrady.optimization.base.component.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.bryanrady.optimization.R;

import androidx.annotation.Nullable;
import androidx.core.app.ComponentActivity;
import androidx.core.app.NotificationCompat;

/**
 * 前台服务
 * @author: wangqingbin
 * @date: 2020/4/2 15:29
 */
public class ForegroundService extends Service {

    private final int NOTIFICATION_ID = 0x10000;
    private static boolean mServiceIsLive = false;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("wangqingbin","onCreate.....");
        //启动服务的时候，创建通知
        Notification notification = createForegroundNotification();
        startForeground(NOTIFICATION_ID, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("wangqingbin","onStartCommand.....");
        mServiceIsLive = true;
        //intent参数获取
        String foreground = intent.getStringExtra("foreground");
        Log.d("wangqingbin","foreground == " + foreground);
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("wangqingbin","onBind.....");
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d("wangqingbin","onUnbind.....");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Log.d("wangqingbin","onDestroy.....");
        super.onDestroy();
        mServiceIsLive = false;
        //关闭前台服务并移除通知
        stopForeground(true);
    }

    /**
     * 创建服务通知内容，例如音乐播放，蓝牙设备正在连接等
     * @return
     */
    private Notification createForegroundNotification(){
        NotificationManager notificationManager = (NotificationManager) getApplicationContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);
        // 唯一的通知通道的id.
        String channelId = "notification_channel_id_01";
        //8.0以上通知消息通道
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O){
            //用户可见的通道名称
            String channelName = "Foreground Service Notification";
            //通道的重要程度
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, importance);
            notificationChannel.setDescription("channel description");
            //LED灯
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            //震动
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId);
        //通知小图标
        builder.setSmallIcon(R.mipmap.ic_launcher);
        //通知标题
        builder.setContentTitle("ContentTitle");
        //通知内容
        builder.setContentText("ContentText");
        //设定通知显示的时间
        builder.setWhen(System.currentTimeMillis());
        //设定启动的内容
        Intent activityIntent = new Intent(this, ComponentActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1,
                activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        //创建通知并返回
        return builder.build();
    }

}

