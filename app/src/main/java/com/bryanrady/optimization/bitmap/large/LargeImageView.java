package com.bryanrady.optimization.bitmap.large;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import java.io.IOException;
import java.io.InputStream;
import java.security.AlgorithmConstraints;

import androidx.annotation.Nullable;

/**
 * 巨图
 * @author: wangqingbin
 * @date: 2020/3/12 17:06
 */
public class LargeImageView extends View implements GestureDetector.OnGestureListener, View.OnTouchListener {

    //指定要加载的矩形区域
    private Rect mRect;
    //用于解码图片的配置
    private BitmapFactory.Options mOptions;
    //图片宽高
    private int mImageWidth;
    private int mImageHeight;
    //区域解码器
    private BitmapRegionDecoder mRegionDecoder;
    //View的宽高
    private int mViewWidth;
    private int mViewHeight;
    //缩放因子
    private float mScale;
    private Bitmap mBitmap;
    //手势
    private GestureDetector mGestureDetector;
    //滑动
    private Scroller mScroller;

    public LargeImageView(Context context) {
        this(context, null);
    }

    public LargeImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LargeImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //要加载的图片矩形区域
        mRect = new Rect();
        //图片解码配置
        mOptions = new BitmapFactory.Options();
        //手势识别
        mGestureDetector = new GestureDetector(context, this);
        //设置OnTouchListener
        setOnTouchListener(this);
        //滑动帮助
        mScroller = new Scroller(context);
    }

    public void setImage(InputStream is){
        mOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is,null, mOptions);
        mImageWidth = mOptions.outWidth;
        mImageHeight = mOptions.outHeight;
        //设置异变 为了复用
        mOptions.inMutable = true;
        //设置成565节省点内存
        mOptions.inPreferredConfig = Bitmap.Config.RGB_565;
        mOptions.inJustDecodeBounds = false;

        try {
            //第2个参数 共享数据 false代表解码器会拷贝一份数据 这样即使is被关闭了 也不会造成影响
            mRegionDecoder = BitmapRegionDecoder.newInstance(is, false);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //刷新一下 这样就会调用测量
        requestLayout();
    }

    public void setImage(String path){

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mRegionDecoder == null){
            return;
        }
        mViewWidth = getMeasuredWidth();
        mViewHeight = getMeasuredHeight();
        //设置要加载的图片矩形区域
        mRect.left = 0;
        mRect.top = 0;
        /**
         * 根据图片宽来计算缩放因子  所以计算横长图的话会有问题，这里只针对竖长图
         * 但是知晓竖长图加载原理后，横长图也是没问题的
         */
        mScale = (float)(mViewWidth / mImageWidth);
        mRect.right = mImageWidth;
        // 需要加载的高(屏幕高) * 缩放因子 = 视图view的高
        // h * mScale = mViewHeight
        //(mViewHeight / mScale) 就是一个屏幕的高
        mRect.bottom = (int)(mViewHeight / mScale);

        Log.d("wangqingbin","mViewWidth=="+mViewWidth);
        Log.d("wangqingbin","mViewHeight=="+mViewHeight);
        Log.d("wangqingbin","mImageWidth=="+mImageWidth);
        Log.d("wangqingbin","mImageHeight=="+mImageHeight);
        Log.d("wangqingbin","mScale=="+mScale);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mRegionDecoder == null){
            return;
        }
        //复用上一张Bitmap的内存
        mOptions.inBitmap = mBitmap;
        mBitmap = mRegionDecoder.decodeRegion(mRect, mOptions);
        Log.d("wangqingbin","图片内存 == " + mBitmap.getByteCount());
        //使用矩阵 对图片进行 缩放
        Matrix matrix = new Matrix();
        matrix.setScale(mScale, mScale);
        canvas.drawBitmap(mBitmap, matrix, null);
    }

    /**
     * 手指按下屏幕的回调
     * @param e
     * @return
     */
    @Override
    public boolean onDown(MotionEvent e) {
        //如果滑动没有停止 就让它强制定制
        if (!mScroller.isFinished()){
            mScroller.forceFinished(true);
        }
        //这里让它返回true 才能继续接收后续事件
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    /**
     * 手指不离开屏幕的滑动 实际上就是拖动
     * @param e1    手指按下去的事件  -- 获取开始的坐标
     * @param e2    当前手势事件      -- 获取当前的坐标
     * @param distanceX x轴 方向移动的距离
     * @param distanceY y轴 方向移动的距离
     * @return
     */
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        /**
         * 滑动的目的是为了改变加载图片的区域
         */
        // 手指从下往上滑 图片也要往上滚 distanceY是负数, top 和 bottom 在减
        // 手指从上往下滑 图片也要往下滚 distanceY是正数, top 和 bottom 在加

        //更新Rect 这里宽就不用管了，因为是按照宽来进行缩放的
        mRect.offset(0,(int)distanceY);
        //做一下安全校验
        // 如果bottom大于图片高了, 代表到底
        if(mRect.bottom > mImageHeight){
            //top和bottom最大值
            mRect.bottom = mImageHeight;
            mRect.top = mImageHeight - (int)(mViewHeight / mScale);
        }
        // 如果top小于0了,代表到顶了
        if (mRect.top < 0){
            //top和bottom最小值
            mRect.top = 0;
            mRect.bottom = (int)(mViewHeight / mScale);
        }
        //重绘
        invalidate();
        return false;
    }


    @Override
    public void onLongPress(MotionEvent e) {

    }

    /**
     * 手指离开屏幕后的惯性滑动
     * @param e1    手指按下去的事件  -- 获取开始的坐标
     * @param e2    当前手势事件      -- 获取当前的坐标
     * @param velocityX  速度  每秒x方向 移动的像素
     * @param velocityY  速度  每秒y方向 移动的像素
     * @return
     */
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        /**
         * startX: 滑动开始的x坐标
         * velocityX: x方向速度
         * minX: x方向的最小值
         * maxX: x方向的最大值
         * y
         */
        //计算 计算之后然后从computeScroll()中获取计算结果
        mScroller.fling(0, mRect.top,
                0,(int)-velocityY,
                0,0,0,
                mImageHeight - (int) (mViewHeight / mScale));
        return false;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        //已经计算结束 return
        if (mScroller.isFinished()){
            return;
        }
        //true 表示当前动画未结束
        if (mScroller.computeScrollOffset()){
            //计算top和bottom
            mRect.top = mScroller.getCurrY();
            mRect.bottom = mRect.top + (int)(mViewHeight / mScale);
            //重绘
            invalidate();
        }
    }

    /**
     * OnTouchListener 触摸屏幕
     * @param v
     * @param event
     * @return
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //触摸回调 交给手势来处理
        return mGestureDetector.onTouchEvent(event);
    }
}
