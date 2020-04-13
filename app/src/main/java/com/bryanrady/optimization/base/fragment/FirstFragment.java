package com.bryanrady.optimization.base.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bryanrady.optimization.R;

/**
 * @author: wangqingbin
 * @date: 2020/4/10 15:43
 */
public class FirstFragment extends Fragment implements View.OnClickListener {

    private FirstBtnClickListener mListener;

    public interface FirstBtnClickListener{
        void onFirstBtnClick();
    }

    public void setFirstBtnClickListener(FirstBtnClickListener listener){
        this.mListener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("wangqingbin","FirstFragment onCreateView");
        View view = inflater.inflate(R.layout.fragment_statck_content, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d("wangqingbin","FirstFragment onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("wangqingbin","FirstFragment onCreate");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("wangqingbin","FirstFragment onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("wangqingbin","FirstFragment onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("wangqingbin","FirstFragment onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("wangqingbin","FirstFragment onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("wangqingbin","FirstFragment onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("wangqingbin","FirstFragment onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("wangqingbin","FirstFragment onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("wangqingbin","FirstFragment onDetach");
    }

    private void initView(View view){
        Button jump = view.findViewById(R.id.btn_jump);
        jump.setOnClickListener(this);
        jump.setText("First Fragment");
        jump.setAllCaps(false);
        EditText etContent = view.findViewById(R.id.et_content);
        etContent.setText("First Fragment");
        etContent.setAllCaps(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_jump:
                if (mListener != null){
                    mListener.onFirstBtnClick();
                }
                FragmentManager fm = getFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                SecondFragment secondFragment = new SecondFragment();
            //    transaction.hide(this);
            //    transaction.add(R.id.fl_content,secondFragment);
                transaction.replace(R.id.fl_content,secondFragment);
                //添加一个Fragment事务到回退栈
                transaction.addToBackStack(null);
                transaction.commit();
                break;
        }
    }

    /**
     *
     *  我们在点击按钮的时候，使用了replace()方法，replace()是remove()和add()的结合，所以我们点击进入SecondFragment中的时候，
     *  FirstFragment视图就会被销毁，即会走onDestroyView()。并且如果我们不添加事务到返回栈的时候，当从SecondFragment中返回的
     *  时候，FirstFragment实例会被销毁，即返回的时候不会返回到FirstFragment中。如果我们添加了这个事务到返回栈中时，那么当从
     *  SecondFragment返回的时候，会返回到FirstFragment中，这个FirstFragment就不会被销毁，但是视图还是会被重新创建，即会重新
     *  走FirstFragment的onCreateView()。
     *
     *  有问题：既然FirstFragment的视图会被销毁和重建，那么之前输入的东西为什么还会保存下来？按道理说不应该重新保存下来的。
     *
     *        而且我没有保存任何东西，感觉不合理。
     *
     */

//2020-04-13 11:41:06.897 24374-24374/com.bryanrady.optimization D/wangqingbin: Activity onCreate........
//2020-04-13 11:41:06.922 24374-24374/com.bryanrady.optimization D/wangqingbin: FirstFragment onAttach
//2020-04-13 11:41:06.922 24374-24374/com.bryanrady.optimization D/wangqingbin: FirstFragment onCreate
//2020-04-13 11:41:06.922 24374-24374/com.bryanrady.optimization D/wangqingbin: FirstFragment onCreateView
//2020-04-13 11:41:06.947 24374-24374/com.bryanrady.optimization D/wangqingbin: FirstFragment onActivityCreated
//2020-04-13 11:41:06.947 24374-24374/com.bryanrady.optimization D/wangqingbin: FirstFragment onStart
//2020-04-13 11:41:06.947 24374-24374/com.bryanrady.optimization D/wangqingbin: Activity onStart........
//2020-04-13 11:41:06.948 24374-24374/com.bryanrady.optimization D/wangqingbin: Activity onResume........
//2020-04-13 11:41:06.949 24374-24374/com.bryanrady.optimization D/wangqingbin: FirstFragment onResume
//
//2020-04-13 11:41:10.744 24374-24374/com.bryanrady.optimization D/wangqingbin: SecondFragment onAttach
//2020-04-13 11:41:10.744 24374-24374/com.bryanrady.optimization D/wangqingbin: SecondFragment onCreate
//2020-04-13 11:41:10.745 24374-24374/com.bryanrady.optimization D/wangqingbin: FirstFragment onPause
//2020-04-13 11:41:10.745 24374-24374/com.bryanrady.optimization D/wangqingbin: FirstFragment onStop
//2020-04-13 11:41:10.746 24374-24374/com.bryanrady.optimization D/wangqingbin: FirstFragment onDestroyView
//2020-04-13 11:41:10.750 24374-24374/com.bryanrady.optimization D/wangqingbin: SecondFragment onCreateView
//2020-04-13 11:41:10.760 24374-24374/com.bryanrady.optimization D/wangqingbin: SecondFragment onActivityCreated
//2020-04-13 11:41:10.760 24374-24374/com.bryanrady.optimization D/wangqingbin: SecondFragment onStart
//2020-04-13 11:41:10.760 24374-24374/com.bryanrady.optimization D/wangqingbin: SecondFragment onResume
//
//
//2020-04-13 11:41:17.293 24374-24374/com.bryanrady.optimization D/wangqingbin: SecondFragment onPause
//2020-04-13 11:41:17.293 24374-24374/com.bryanrady.optimization D/wangqingbin: SecondFragment onStop
//2020-04-13 11:41:17.294 24374-24374/com.bryanrady.optimization D/wangqingbin: SecondFragment onDestroyView
//2020-04-13 11:41:17.295 24374-24374/com.bryanrady.optimization D/wangqingbin: SecondFragment onDestroy
//2020-04-13 11:41:17.296 24374-24374/com.bryanrady.optimization D/wangqingbin: SecondFragment onDetach
//2020-04-13 11:41:17.296 24374-24374/com.bryanrady.optimization D/wangqingbin: FirstFragment onCreateView
//2020-04-13 11:41:17.306 24374-24374/com.bryanrady.optimization D/wangqingbin: FirstFragment onActivityCreated
//2020-04-13 11:41:17.307 24374-24374/com.bryanrady.optimization D/wangqingbin: FirstFragment onStart
//2020-04-13 11:41:17.307 24374-24374/com.bryanrady.optimization D/wangqingbin: FirstFragment onResume


}
