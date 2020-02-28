package com.bryanrady.optimization.bitmap.compress.memory;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

        //从服务器上获取的图片
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.lance);
        bitmap = ImageResize.resizeBitmap(mContext, R.mipmap.lance, 80, 80, false, null);



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
