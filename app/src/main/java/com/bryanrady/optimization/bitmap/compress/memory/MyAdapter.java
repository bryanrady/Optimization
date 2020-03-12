package com.bryanrady.optimization.bitmap.compress.memory;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bryanrady.optimization.R;

/**
 * @author: wangqingbin
 * @date: 2020/2/28 11:43
 */
public class MyAdapter extends BaseAdapter {

    private Context mContext;

    public MyAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return 999;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (null == convertView) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listview_memory_compress_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //从内存缓存中获取
        Bitmap bitmap = ImageCache.getInstance().getBitmapFromMemoryCache(String.valueOf(position));
        Log.e("wangqingbin","从内存缓存中获取:" + bitmap);
        if (bitmap == null){
            //从复用池中找可以被复用内存的图片
            Bitmap reusable = ImageCache.getInstance().getBitmapFromReusablePool(40, 40, 1);
            Log.d("wangqingbin","从复用池中找可以被复用内存的图片:" + reusable);

            //从磁盘缓存中获取
            bitmap = ImageCache.getInstance().getBitmapFromDiskCache(String.valueOf(position), reusable);
            Log.e("wangqingbin","从磁盘缓存获取:" + bitmap);

            if (bitmap == null){
                //然后使用被复用的bitmap的内存从服务器上加载新的图片
                bitmap = ImageResize.resizeBitmap(mContext, R.mipmap.lance, 80, 80, false, reusable);
                Log.e("wangqingbin","从服务器上获取:" + bitmap);

                ImageCache.getInstance().putBitmap2MemoryCache(String.valueOf(position),bitmap);
                ImageCache.getInstance().putBitmap2DiskCache(String.valueOf(position),bitmap);
            }

        }

        holder.iv.setImageBitmap(bitmap);
        return convertView;
    }

    static class ViewHolder {

        ImageView iv;

        ViewHolder(View view) {
            iv = view.findViewById(R.id.iv);
        }
    }
}
