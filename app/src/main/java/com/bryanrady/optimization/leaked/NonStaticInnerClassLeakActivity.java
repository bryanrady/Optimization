package com.bryanrady.optimization.leaked;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.bryanrady.optimization.R;

import java.lang.ref.WeakReference;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * 外部类持有非静态内部类的静态实例 造成的内存泄漏
 * @author: wangqingbin
 * @date: 2020/1/6 17:01
 */
public class NonStaticInnerClassLeakActivity extends AppCompatActivity {

    //private static Student mStudent;
    private static FixStudent mFixStudent;
    //private static FixStudent2 mFixStudent2;
    private static FixStudent3 mFixStudent3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaked_model);
        TextView tv = findViewById(R.id.tv_prompt);
        tv.setText("外部类含有非静态内部类的静态变量造成的内存泄漏");

//        if(mStudent == null){
//            mStudent = new Student();
//            mStudent.study();
//        }

        if(mFixStudent == null){
            mFixStudent = new FixStudent();
            mFixStudent.study();
        }

//        if(mFixStudent2 == null){
//            mFixStudent2 = new FixStudent2(this);
//            mFixStudent2.study();
//        }

        if(mFixStudent3 == null){
            mFixStudent3 = new FixStudent3(this);
            mFixStudent3.study();
        }


    }


    public class Student {

        void study(){
            Toast.makeText(NonStaticInnerClassLeakActivity.this, "学生在学习",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     *
     *  NonStaticInnerClassLeakActivity.mStudent
     *
     *  NonStaticInnerClassLeakActivity$Student.this$0
     *
     *  根据提示 我们可以看到因为NonStaticInnerClassActivity$Student发生了内存泄漏
     *
     *      原因： 首先我们把mStudent变量声明为static,而static变量的生命周期和Application一样，所以只要程序没关闭mStudent就一直会存在，
     *             Student对象就没法销毁，就不会被回收，又因为非静态内部类Student默认持有外部类NonStaticInnerClassActivity的引用，
     *             所以NonStaticInnerClassActivity也不会被回收，所以即使NonStaticInnerClassActivity销毁了，但是NonStaticInnerClassActivity的
     *             内存还是没有被回收，所以就会造成NonStaticInnerClassActivity发生内存泄漏
     *
     *      修改： 1.将非静态内部类Student改成静态内部类。这样的话静态内部类就不会持有外部类的引用，现在它和外部类一样
     *
     *             2.将非静态内部类Student提取出来，封装成单例。
     */

    public static class FixStudent{

        void study(){
            Log.e("wangqingbin","学生在学习");
        }

    }

    public static class FixStudent2{

        private final NonStaticInnerClassLeakActivity mActivity;

        public FixStudent2(NonStaticInnerClassLeakActivity activity){
            this.mActivity = activity;
        }

        void study(){
            Toast.makeText(mActivity, "学生在学习",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     *
     *  NonStaticInnerClassLeakActivity.mStudent
     *
     *  NonStaticInnerClassLeakActivity$Student$FixStudent.mActivity
     *
     *  按照上面的写法还是发生了内存泄漏，原因如下：
     *
     *  我们按照上面的FixStudent2来修改还是会发生内存泄漏，因为弹吐司需要context,这里的context就是NonStaticInnerClassActivity,
     *  现在FixStudent2中含有NonStaticInnerClassActivity的变量mActivity，就是FixStudent2还是会持有NonStaticInnerClassActivity的引用，
     *  而mFixStudent2指向的对象FixStudent2必须要等到程序结束后才会被回收，所以当NonStaticInnerClassActivity被销毁了，
     *  NonStaticInnerClassActivity的内存也不会被回收，从而发生了内存泄漏
     *
     *  修改：将mActivity包装成弱引用，只要一发生gc，mActivity就会被回收，但是在使用的时候要判空, 因为可能被回收了
     *
     */


    public static class FixStudent3{

        //使用弱引用包装
        private WeakReference<NonStaticInnerClassLeakActivity> mActivity;

        public FixStudent3(NonStaticInnerClassLeakActivity activity){
            this.mActivity = new WeakReference<>(activity);
        }

        void study(){
            NonStaticInnerClassLeakActivity activity = mActivity.get();
            if (activity != null && !activity.isFinishing()){
                Toast.makeText(activity, "学生在学习",Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //华为手机引发的泄漏
        FixLeakedUtils.fixInputMethodManagerLastSrvView(this);
        FixLeakedUtils.fixInputMethodManagerLeak(this);
    }
}
