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
public class SecondFragment extends Fragment implements View.OnClickListener {

    public interface SecondBtnClickListener{
        void onSecondBtnClick();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("wangqingbin","SecondFragment onCreateView");
        View view = inflater.inflate(R.layout.fragment_statck_content, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d("wangqingbin","SecondFragment onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("wangqingbin","SecondFragment onCreate");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("wangqingbin","SecondFragment onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("wangqingbin","SecondFragment onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("wangqingbin","SecondFragment onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("wangqingbin","SecondFragment onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("wangqingbin","SecondFragment onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("wangqingbin","SecondFragment onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("wangqingbin","SecondFragment onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("wangqingbin","SecondFragment onDetach");
    }

    private void initView(View view){
        Button jump = view.findViewById(R.id.btn_jump);
        jump.setOnClickListener(this);
        jump.setText("Second Fragment");
        jump.setAllCaps(false);
        EditText etContent = view.findViewById(R.id.et_content);
        etContent.setText("Second Fragment");
        etContent.setAllCaps(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_jump:
                //如果哪个Activity实现了这个接口就可以调用接口里面的方法
                if (getActivity() instanceof SecondBtnClickListener){
                    ((SecondBtnClickListener) getActivity()).onSecondBtnClick();
                }
                FragmentManager fm = getFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                ThirdFragment thirdFragment = new ThirdFragment();
                transaction.hide(this);
                transaction.add(R.id.fl_content,thirdFragment);
            //    transaction.replace(R.id.fl_content,thirdFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
        }
    }

    /**
     * 这里点击的时候我们没有使用replace()，而是先隐藏了当前Fragment，然后添加了ThirdFragment实例，最后将事务添加到返回栈中。
     *  使用了hide()和add(),意味着SecondFragment的视图就不会被销毁和重建。
     *
     */

//2020-04-13 11:49:22.265 26422-26422/com.bryanrady.optimization D/wangqingbin: SecondFragment onAttach
//2020-04-13 11:49:22.265 26422-26422/com.bryanrady.optimization D/wangqingbin: SecondFragment onCreate
//2020-04-13 11:49:22.266 26422-26422/com.bryanrady.optimization D/wangqingbin: FirstFragment onPause
//2020-04-13 11:49:22.266 26422-26422/com.bryanrady.optimization D/wangqingbin: FirstFragment onStop
//2020-04-13 11:49:22.267 26422-26422/com.bryanrady.optimization D/wangqingbin: FirstFragment onDestroyView
//2020-04-13 11:49:22.268 26422-26422/com.bryanrady.optimization D/wangqingbin: SecondFragment onCreateView
//2020-04-13 11:49:22.279 26422-26422/com.bryanrady.optimization D/wangqingbin: SecondFragment onActivityCreated
//2020-04-13 11:49:22.279 26422-26422/com.bryanrady.optimization D/wangqingbin: SecondFragment onStart
//2020-04-13 11:49:22.279 26422-26422/com.bryanrady.optimization D/wangqingbin: SecondFragment onResume
//
//
//2020-04-13 11:49:36.321 26422-26422/com.bryanrady.optimization D/wangqingbin: ThirdFragment onAttach
//2020-04-13 11:49:36.321 26422-26422/com.bryanrady.optimization D/wangqingbin: ThirdFragment onCreate
//2020-04-13 11:49:36.323 26422-26422/com.bryanrady.optimization D/wangqingbin: ThirdFragment onCreateView
//2020-04-13 11:49:36.333 26422-26422/com.bryanrady.optimization D/wangqingbin: ThirdFragment onActivityCreated
//2020-04-13 11:49:36.333 26422-26422/com.bryanrady.optimization D/wangqingbin: ThirdFragment onStart
//2020-04-13 11:49:36.333 26422-26422/com.bryanrady.optimization D/wangqingbin: ThirdFragment onResume
//
//
//2020-04-13 11:49:42.302 26422-26422/com.bryanrady.optimization D/wangqingbin: ThirdFragment onPause
//2020-04-13 11:49:42.302 26422-26422/com.bryanrady.optimization D/wangqingbin: ThirdFragment onStop
//2020-04-13 11:49:42.302 26422-26422/com.bryanrady.optimization D/wangqingbin: ThirdFragment onDestroyView
//2020-04-13 11:49:42.304 26422-26422/com.bryanrady.optimization D/wangqingbin: ThirdFragment onDestroy
//2020-04-13 11:49:42.304 26422-26422/com.bryanrady.optimization D/wangqingbin: ThirdFragment onDetach

}
