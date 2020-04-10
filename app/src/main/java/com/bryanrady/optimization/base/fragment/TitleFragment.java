package com.bryanrady.optimization.base.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bryanrady.optimization.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * @author: wangqingbin
 * @date: 2020/4/10 15:43
 */
public class TitleFragment extends Fragment {

    //启动Activity显示Fragment的过程
//            2020-04-10 16:14:50.562 2346-2346/com.bryanrady.optimization D/wangqingbin: Activity onCreate........
//            2020-04-10 16:14:50.589 2346-2346/com.bryanrady.optimization E/wangqingbin: Fragment onAttach........
//            2020-04-10 16:14:50.589 2346-2346/com.bryanrady.optimization E/wangqingbin: Fragment onCreate........
//            2020-04-10 16:14:50.590 2346-2346/com.bryanrady.optimization E/wangqingbin: Fragment onCreateView........
//            2020-04-10 16:14:50.603 2346-2346/com.bryanrady.optimization E/wangqingbin: Fragment onActivityCreated........
//            2020-04-10 16:14:50.603 2346-2346/com.bryanrady.optimization E/wangqingbin: Fragment onStart........
//            2020-04-10 16:14:50.603 2346-2346/com.bryanrady.optimization D/wangqingbin: Activity onStart........
//            2020-04-10 16:14:50.605 2346-2346/com.bryanrady.optimization D/wangqingbin: Activity onResume........
//            2020-04-10 16:14:50.606 2346-2346/com.bryanrady.optimization E/wangqingbin: Fragment onResume........

    //返回上一页面
//            2020-04-10 16:15:50.256 2346-2346/com.bryanrady.optimization E/wangqingbin: Fragment onPause........
//            2020-04-10 16:15:50.257 2346-2346/com.bryanrady.optimization D/wangqingbin: Activity onPause........
//            2020-04-10 16:15:50.610 2346-2346/com.bryanrady.optimization E/wangqingbin: Fragment onStop........
//            2020-04-10 16:15:50.610 2346-2346/com.bryanrady.optimization D/wangqingbin: Activity onStop........
//            2020-04-10 16:15:50.612 2346-2346/com.bryanrady.optimization E/wangqingbin: Fragment onDestroyView........
//            2020-04-10 16:15:50.614 2346-2346/com.bryanrady.optimization E/wangqingbin: Fragment onDestroy........
//            2020-04-10 16:15:50.614 2346-2346/com.bryanrady.optimization E/wangqingbin: Fragment onDetach........
//            2020-04-10 16:15:50.614 2346-2346/com.bryanrady.optimization D/wangqingbin: Activity onDestroy........

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e("wangqingbin","Fragment onAttach........");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("wangqingbin","Fragment onCreate........");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e("wangqingbin","Fragment onCreateView........");
        View view = inflater.inflate(R.layout.fragment_title, container, false);
        initView(view);
        return view;
    }

    private void initView(View view){
        TextView tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText("使用Fragment做标题");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e("wangqingbin","Fragment onActivityCreated........");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("wangqingbin","Fragment onStart........");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("wangqingbin","Fragment onResume........");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("wangqingbin","Fragment onPause........");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("wangqingbin","Fragment onStop........");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e("wangqingbin","Fragment onDestroyView........");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("wangqingbin","Fragment onDestroy........");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e("wangqingbin","Fragment onDetach........");
    }
}
