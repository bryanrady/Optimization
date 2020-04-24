package com.bryanrady.optimization.base.fragment.lazy.oldmodel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bryanrady.optimization.R;
import com.bryanrady.optimization.base.fragment.lazy.base.LogFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class OldFragment2 extends LogFragment {

    private TextView tv_content;

    public OldFragment2(String tag) {
        super(tag);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lazy_content, container, false);
        initView(view);
        return view;
    }

    private void initView(View view){
        tv_content = view.findViewById(R.id.tv_content);
        tv_content.setText("OldFragment2");
        tv_content.setAllCaps(false);
    }

//    @Override
//    public void lazyLoad() {
//
//    }

}
