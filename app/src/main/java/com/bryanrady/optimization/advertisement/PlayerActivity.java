package com.bryanrady.optimization.advertisement;

import android.Manifest;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;

import com.bryanrady.optimization.MyApplication;
import com.bryanrady.optimization.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.danikula.videocache.HttpProxyCacheServer;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

/**
 * @author: wangqingbin
 * @date: 2020/1/9 17:11
 */
public class PlayerActivity extends AppCompatActivity {

    private VideoView videoView;
    private ImageView imageView;
    private int index = 0;
    private final int IMAGE_SUCCESS = 1;
    private final int IMAGE_FAILED = 2;
    private final int RESOURCE_READY = 3;
    private MyHandler myHandler;
    //https://blog.csdn.net/zhqw_csdn/article/details/81514313
    private HttpProxyCacheServer proxy;
    private List<String> resources;
    //多长时间更新一次
    private final int UPDATE_DURATION = 5 * 60 * 1000;
    private Timer timer;
    private UpdateTimerTask timerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        videoView = findViewById(R.id.videoView);
        imageView = findViewById(R.id.imageView);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//如果 API level 是大于等于 23(Android 6.0) 时
            //判断是否具有权限
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE},
                        0);
            }
        }

        myHandler = new MyHandler(this);
        proxy = MyApplication.getProxy(this);
        timer = new Timer();
        timerTask = new UpdateTimerTask(this);

        autoUpdate();
    }

    private void autoUpdate(){
        timer.schedule(timerTask,0, UPDATE_DURATION);
    }

    private static class UpdateTimerTask extends TimerTask{

        //记录请求次数 模拟
        private int times = 1;

        private WeakReference<PlayerActivity> mActivity;

        public UpdateTimerTask(PlayerActivity activity){
            this.mActivity = new WeakReference<>(activity);
        }

        @Override
        public void run() {
            PlayerActivity activity = mActivity.get();
            if (activity != null && !activity.isFinishing()){
                List<String> resources;
                if(activity.needRefresh()){
                    Log.e("wangqingbin","更新资源");
                    resources = activity.getUrlFromServer();

                    //模拟资源更新
                    if (times > 1){
                        if (resources != null){
                            resources.add("http://img.pconline.com.cn/images/upload/upc/tx/wallpaper/1212/14/c2/16604701_1355474001518.jpg");
                            resources.add("http://pic4.zhimg.com/50/v2-a0e788b695e558b9c93100a7568f7860_hd.jpg");
                        }
                    }
                    times++;

                }else{
                    Log.e("wangqingbin","不需要更新");
                    //String resourceUrlJson = SharePreferenceManager.getResourceUrlJson();
                    //转json得到resources集合

                    //模拟
                    resources = new ArrayList<>();
                    resources.add("https://img.pc841.com/2018/1213/20181213121657163.jpg");
                    resources.add("http://vd3.bdstatic.com/mda-jinpn2ghm53htxvb/sc/mda-jinpn2ghm53htxvb.mp4");
                    resources.add("http://pic.sc.chinaz.com/files/pic/pic9/201810/zzpic14677.jpg");
                    resources.add("http://vd2.bdstatic.com/mda-jhffxctu0n1tvhhd/sc/mda-jhffxctu0n1tvhhd.mp4");

                    Log.e("wangqingbin","本地存储得到Url");
                }

                Message message = Message.obtain();
                message.what = activity.RESOURCE_READY;
                message.obj = resources;
                activity.myHandler.sendMessage(message);
            }
        }
    }

    private boolean needRefresh(){
        long currentTimeMillis = System.currentTimeMillis();
        long lastRefreshTime = SharePreferenceManager.getLastRefreshTime();
        Log.e("wangqingbin","currentTimeMillis=="+currentTimeMillis);
        Log.e("wangqingbin","lastRefreshTime=="+lastRefreshTime);
        if(lastRefreshTime > 0){
            if (currentTimeMillis >= lastRefreshTime + UPDATE_DURATION){
                return true;
            }else{
                String resourceUrlJson = SharePreferenceManager.getResourceUrlJson();
                if(!TextUtils.isEmpty(resourceUrlJson)){
                    return false;
                }else{
                    return true;
                }
            }
        }else{
            return true;
        }
    }

    private List<String> getUrlFromServer() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /**
         * 这里得到的是一个json串 缓存起来
         */
    //    SharePreferenceManager.setResourceUrlJson(response);
        SharePreferenceManager.setLastRefreshTime(System.currentTimeMillis());

        //json转换成数组resources

        List<String> resources = new ArrayList<>();
        resources.add("https://img.pc841.com/2018/1213/20181213121657163.jpg");
        resources.add("http://vd3.bdstatic.com/mda-jinpn2ghm53htxvb/sc/mda-jinpn2ghm53htxvb.mp4");
        resources.add("http://pic.sc.chinaz.com/files/pic/pic9/201810/zzpic14677.jpg");
        resources.add("http://vd2.bdstatic.com/mda-jhffxctu0n1tvhhd/sc/mda-jhffxctu0n1tvhhd.mp4");

        Log.e("wangqingbin","网络请求得到Url");
        return resources;
    }


    private void switchResource() {
        if (index == resources.size()){
            index = 0;
        }
        Log.e("wangqingbin","index == " + index);
        String url = resources.get(index);
        Log.e("wangqingbin","url == " + url);
        if(url.contains("mp4")){
            playVideo(url);
        }else{
            loadImage(url);
        }
    }

    private void playVideo(String url){
        imageView.setVisibility(View.GONE);
        videoView.setVisibility(View.VISIBLE);

        if (proxy.isCached(url)) {
            Log.e("wangqingbin", "已缓存");
        } else {
            Log.e("wangqingbin", "未缓存");
        }
        String proxyUrl = proxy.getProxyUrl(url);
        Log.e("wangqingbin","proxyUrl == " + proxyUrl);

        videoView.setVideoPath(proxyUrl);
        videoView.start();
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mPlayer) {
                index++;
                switchResource();
            }
        });
        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {

                index++;
                switchResource();

                return true;
            }
        });
    }

    private class MyHandler extends Handler{

        private WeakReference<PlayerActivity> mActivity;

        public MyHandler(PlayerActivity activity){
            this.mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            PlayerActivity activity = mActivity.get();
            if (activity != null && !activity.isFinishing()){
                switch (msg.what){
                    case IMAGE_SUCCESS:
                    case IMAGE_FAILED:
                        index++;
                        switchResource();
                        break;
                    case RESOURCE_READY:
                        resources = (List<String>) msg.obj;
                        if (activity.resources != null && activity.resources.size() > 0){
                            activity.switchResource();
                        }
                        break;
                }
            }
        }
    }

    private void loadImage(String url){
        imageView.setVisibility(View.VISIBLE);
        videoView.setVisibility(View.GONE);
        Glide.with(this)
                .load(url)
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        e.printStackTrace();
                        myHandler.sendEmptyMessageDelayed(IMAGE_FAILED,2000);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        myHandler.sendEmptyMessageDelayed(IMAGE_SUCCESS,6000);
                        return false;
                    }
                })
                .into(imageView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (videoView.isPlaying()){
            videoView.stopPlayback();
        }
        timer.cancel();
        timerTask.cancel();
        myHandler.removeCallbacksAndMessages(true);
    }
}
