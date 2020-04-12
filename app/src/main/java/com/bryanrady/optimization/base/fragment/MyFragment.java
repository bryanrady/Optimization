package com.bryanrady.optimization.base.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bryanrady.optimization.R;

/**
 * @author: wangqingbin
 * @date: 2020/4/10 15:43
 */
public class MyFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_content, container, false);
        initView(view);
        return view;
    }

    private void initView(View view){
        TextView tvContent = view.findViewById(R.id.tv_content);
        tvContent.setText("使用Fragment做我的主页");
    }

}
