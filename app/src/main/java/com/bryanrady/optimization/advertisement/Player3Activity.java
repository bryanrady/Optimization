package com.bryanrady.optimization.advertisement;

import android.Manifest;
import android.graphics.ImageFormat;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;

import com.bryanrady.optimization.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.lang.ref.WeakReference;

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
public class Player3Activity extends AppCompatActivity {

    private VideoView videoView;
    private ImageView imageView;
    private int index = 0;
    private final int IMAGE_COMPLETED = 1;
    private MyHandler myHandler;

    private String[] resources = {
//            Environment.getExternalStorageDirectory()+"/sdcard/a.jpg",
//            "http://pic67.nipic.com/file/20150523/9252150_163049145403_2.jpg",
//            "http://i1.sinaimg.cn/ent/d/2008-06-04/U105P28T3D2048907F326DT20080604225106.jpg",
//            Environment.getExternalStorageDirectory()+"/sdcard/b.jpg",
            "http://vd3.bdstatic.com/mda-jinpn2ghm53htxvb/sc/mda-jinpn2ghm53htxvb.mp4",
            "http://vd2.bdstatic.com/mda-jhffxctu0n1tvhhd/sc/mda-jhffxctu0n1tvhhd.mp4",
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player3);

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
        switchResource();
    }

    private void switchResource() {
        Log.e("wangqingbin","index == " + index);
        if (index == resources.length){
            index = 0;
        }

        String url = resources[index];
        Log.e("wangqingbin","url == " + url);
        if(url.contains("mp4")){
            playVideo(url);
        }else{
            loadImage(url);
        }

    }

    private void playVideo(String url){
        Log.e("wangqingbin","视频");
        imageView.setVisibility(View.GONE);
        videoView.setVisibility(View.VISIBLE);


        videoView.setVideoPath(url);
        videoView.start();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Log.e("wangqingbin","视频准备好了");
            }
        });
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mPlayer) {
                Log.e("wangqingbin","视频播放完成");
                index++;
                switchResource();
            }
        });
        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.e("wangqingbin","视频发送错误");

                index++;
                switchResource();

                return true;
            }
        });
    }

    private class MyHandler extends Handler{

        private WeakReference<Player3Activity> mActivity;

        public MyHandler(Player3Activity activity){
            this.mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Player3Activity activity = mActivity.get();
            if (activity != null && !activity.isFinishing()){
                switch (msg.what){
                    case IMAGE_COMPLETED:
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                        index++;
                        switchResource();
                        break;
                }
            }
        }
    }

    private void loadImage(String url){
        Log.e("wangqingbin","图片");
        imageView.setVisibility(View.VISIBLE);
        videoView.setVisibility(View.GONE);
        Glide.with(this)
                .load(url)
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Log.e("wangqingbin","图片加载失败");
                        myHandler.sendEmptyMessage(IMAGE_COMPLETED);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        Log.e("wangqingbin","图片加载成功");
                        myHandler.sendEmptyMessage(IMAGE_COMPLETED);
                        return false;
                    }
                })
                .into(imageView);



    }

}
