package com.bryanrady.optimization.advertisement;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;

import com.bryanrady.optimization.MyApplication;
import com.bryanrady.optimization.R;
import com.bumptech.glide.Glide;
import com.danikula.videocache.HttpProxyCacheServer;

import java.util.Calendar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @author: wangqingbin
 * @date: 2020/1/9 17:11
 */
public class PlayerActivity extends AppCompatActivity {
//https://www.cnblogs.com/ygj0930/p/7742996.html

    private VideoView videoView;
    private ImageView imageView;
    private HttpProxyCacheServer proxy;

    private String[] resources = {
            "https://www.baidu.com/img/bd_logo1.png",
            "http://vd3.bdstatic.com/mda-jinpn2ghm53htxvb/sc/mda-jinpn2ghm53htxvb.mp4",
            "http://i1.sinaimg.cn/ent/d/2008-06-04/U105P28T3D2048907F326DT20080604225106.jpg",
            "http://vd2.bdstatic.com/mda-jhffxctu0n1tvhhd/sc/mda-jhffxctu0n1tvhhd.mp4",
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        videoView = findViewById(R.id.videoView);
        imageView = findViewById(R.id.imageView);

        imageView.setVisibility(View.VISIBLE);
        videoView.setVisibility(View.GONE);
        Glide.with(this).load("http://i1.sinaimg.cn/ent/d/2008-06-04/U105P28T3D2048907F326DT20080604225106.jpg").into(imageView);
    //    loadResource();
    }

    private void loadResource(){

        for (String url : resources){
            int index = url.lastIndexOf(".");
            String suffix = url.substring(index+1);
            if("png".equals(suffix) || "jpg".equals(suffix)){
                playImage(url);
            }else{
                playVideo(url);
            }
        }
    }

    private void playImage(String url) {
        imageView.setVisibility(View.VISIBLE);
        videoView.setVisibility(View.GONE);
        Glide.with(getApplicationContext()).load(url).into(imageView);
    }

    private void playVideo(String url){
        imageView.setVisibility(View.GONE);
        videoView.setVisibility(View.VISIBLE);

        //创建缓存代理
        proxy = MyApplication.getProxy(this);

        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                videoView.stopPlayback(); //播放异常，则停止播放，防止弹窗使界面阻塞
                return true;
            }
        });

        //视频url拼接日期，实现按日更新
        String proxyUrl = proxy.getProxyUrl(url + "&date=" + getTimeStamp());
        videoView.setVideoPath(proxyUrl); //为videoview设置播放路径，而不是设置播放url
        videoView.start();
    }

    //获取当天年月日，作为动态后缀，每天变化一次
    public String getTimeStamp(){
        Calendar now = Calendar.getInstance();
        String timeStamp = ""+now.get(Calendar.YEAR)+now.get(Calendar.MONTH)+now.get(Calendar.DAY_OF_MONTH);
        return timeStamp;
    }

}
