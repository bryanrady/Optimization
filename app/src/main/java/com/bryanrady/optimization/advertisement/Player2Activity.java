package com.bryanrady.optimization.advertisement;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.VideoView;

import com.bryanrady.optimization.MyApplication;
import com.bryanrady.optimization.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.request.RequestOptions;
import com.danikula.videocache.HttpProxyCacheServer;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import java.io.IOException;
import java.util.ArrayList;
import androidx.appcompat.app.AppCompatActivity;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * @author: wangqingbin
 * @date: 2020/1/9 17:11
 */
public class Player2Activity extends AppCompatActivity {
    private final String TAG_NAME = "ScreenSaverActivity: %s";
    private SurfaceView surfaceView;
    private IjkMediaPlayer mPlayer;
    private Banner banner;
    private ArrayList<String> screenSaverResource = new ArrayList<>();

    private SurfaceHolder.Callback callback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            createPlayer();

            mPlayer.setDisplay(surfaceView.getHolder());
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            if (surfaceView != null) {
                surfaceView.getHolder().removeCallback(callback);
                surfaceView = null;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//remove title bar  即隐藏标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_player2);

        surfaceView = findViewById(R.id.surface_view);
        banner = findViewById(R.id.banner);
        init();
    }

    private void init() {

        /** 网络资源测试**/
        screenSaverResource.add("http://vd3.bdstatic.com/mda-jinpn2ghm53htxvb/sc/mda-jinpn2ghm53htxvb.mp4");
        screenSaverResource.add("http://vd2.bdstatic.com/mda-jhffxctu0n1tvhhd/sc/mda-jhffxctu0n1tvhhd.mp4");

        //获取本地屏保资源
        if (screenSaverResource == null || screenSaverResource.isEmpty()) {
            return;
        }

        //判断是否是视频资源
        if (screenSaverResource.get(0).contains(".mp4") || screenSaverResource.get(0).contains(".MP4")) {
            banner.setVisibility(View.GONE);
            surfaceView.getHolder().addCallback(callback);
        } else {

            surfaceView.setVisibility(View.GONE);

            //设置图片加载器
            banner.setImageLoader(new GlideImageLoader());
            banner.setImages(screenSaverResource);
            banner.setDelayTime(15 * 1000);

            //banner设置方法全部调用完毕时最后调用
            banner.start();
        }
    }


    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context).load(path).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE)).into(imageView);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        this.finish();
        return super.dispatchTouchEvent(ev);
    }


    private void switchResources() {
        mPlayer.reset();
        try {

            String resource = screenSaverResource.get(0);
            screenSaverResource.remove(0);
            screenSaverResource.add(resource);
            adaptionLayout(resource);
            mPlayer.setDataSource(this, Uri.parse(resource));
        } catch (IOException e) {
            Log.e(TAG_NAME, "屏保文件解析错误：" + e.getMessage());
            e.printStackTrace();
        }
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mPlayer.prepareAsync();
        mPlayer.setDisplay(surfaceView.getHolder());
    }


    /***********************************************************************************************
     * 创建视频播放器
     **********************************************************************************************/
    private void createPlayer() {
        if (mPlayer == null) {
            mPlayer = new IjkMediaPlayer();
            IjkMediaPlayer.loadLibrariesOnce(null);
            IjkMediaPlayer.native_profileBegin("libijkplayer.so");
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {
                String resource = screenSaverResource.get(screenSaverResource.size() - 1);
                adaptionLayout(resource);
                mPlayer.setDataSource(this, Uri.parse(resource));

            } catch (IOException e) {
                Log.e(TAG_NAME, "屏保文件解析错误：" + e.getMessage());
                e.printStackTrace();
            }
            mPlayer.prepareAsync();
            mPlayer.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(IMediaPlayer iMediaPlayer) {
                    switchResources();
                }
            });
        }
    }

    /***********************************************************************************************
     * 释放视频播放器资源
     **********************************************************************************************/
    private void release() {
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
            IjkMediaPlayer.native_profileEnd();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        release();
    }

    /***********************************************************************************************
     * 视频自动适应布局
     * @param path 播放资源
     **********************************************************************************************/
    private void adaptionLayout(String path) {
        android.media.MediaMetadataRetriever mmr = new android.media.MediaMetadataRetriever();
        if (path == null) {
            return;
        }
        try {
            mmr.setDataSource(path);
            int width = Integer.parseInt(mmr.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));//宽
            int height = Integer.parseInt(mmr.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));//高

            //根据屏幕宽高设置1920*1080
            double againValue = Math.min(1920.0 / height, 1080.0 / width);
            surfaceView.getLayoutParams().width = (int) (width * againValue);
            surfaceView.getLayoutParams().height = (int) (height * againValue);
        } catch (Exception ex) {
            Log.e(TAG_NAME, "屏保文件解析错误：" + ex.getMessage());
        } finally {
            mmr.release();
        }
    }

}
