package com.bryanrady.optimization.bitmap.compress.memory;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.bryanrady.optimization.R;
import com.bryanrady.optimization.leaked.FixLeakedUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @author: wangqingbin
 * @date: 2020/2/27 17:15
 */
public class MemoryCompressActivity extends AppCompatActivity {

    public static final String TAG = MemoryCompressActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bitmap_memory_compress);

        ImageCache.getInstance().init(this,"");

        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(new MyAdapter(this));

        /**
         * 我们把lance_p这张图片放到不同的资源目录下，看一下图片所占用的内存大小是否有变化？
         */
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.lance);
        if (bitmap != null){
            i(bitmap);
        }
        /**
         *  hdpi:       图片708 * 1106, 内存大小是: 3132192
         *
         *  mdpi:       图片1062 * 1659, 内存大小是:7047432
         *
         *  xhdpi:      图片531 * 830, 内存大小是:  1762920
         *
         *  xxhdpi:     图片354 * 553, 内存大小是:  783048
         *
         *  xxxhdpi:    图片266 * 415, 内存大小是:  441560
         *
         *  随便把一张图片放在不同的资源目录下，图片所占的内存变化十分巨大，所以我们要合理的将图片放在合适的分辨率资源中。
         *
         */


        /**
         * 这里加载同一资源目录下的三张分辨率一样的不同格式的图片  这三张图片所占用的内存大小一样吗?
         */
        Bitmap png = BitmapFactory.decodeResource(getResources(), R.mipmap.lance_p);
        if (png != null){
            i(png);
        }
        Bitmap jpeg = BitmapFactory.decodeResource(getResources(), R.mipmap.lance_j);
        if (jpeg != null){
            i(jpeg);
        }
        Bitmap webp = BitmapFactory.decodeResource(getResources(), R.mipmap.lance_w);
        if (webp != null){
            i(webp);
        }
        /**
         * 图片426 * 524, 内存大小是:892896
         * 通过打印得知 这三张图片所占用的内存大小都是一样的。
         * 由此可得知，位图所占的内存大小和图片格式没什么关系，只和图片分辨率和像素位有关系。
         */

    }

    void i(Bitmap bitmap){
        //bitmap.getByteCount() 返回可用于存储此位图像素的最小字节数
        Log.i(TAG,"图片"+bitmap.getWidth()+" * "+bitmap.getHeight()+", 内存大小是:" + bitmap.getByteCount());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //华为手机引发的泄漏
        FixLeakedUtils.fixInputMethodManagerLastSrvView(this);
        FixLeakedUtils.fixInputMethodManagerLeak(this);
    }
}
