package com.bryanrady.optimization.base.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.bryanrady.optimization.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * @author: wangqingbin
 * @date: 2020/4/10 15:43
 */
public class ChangeFragment extends Fragment{

    private EditText etContent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("wangqingbin","ChangeFragment onCreateView");
        View view = inflater.inflate(R.layout.fragment_statck_content, container, false);
        initView(view);
        if (savedInstanceState != null){
            initData(savedInstanceState);
        }
        return view;
    }

    private void initData(Bundle bundle){
        Log.d("wangqingbin","content ==" + bundle.getString("content"));
        etContent.setText(bundle.getString("content"));
    }

    /**
     * 在这个方法中保持数据，然后在onCreate或者onCreateView或者onActivityCreated进行恢复都可以
     * @param outState
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        String content = etContent.getText().toString().trim();
        if (!TextUtils.isEmpty(content)){
            outState.putString("content",content);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("wangqingbin","ChangeFragment onDestroyView");
    }

    private void initView(View view){
        Button jump = view.findViewById(R.id.btn_jump);
        jump.setText("Change Fragment");
        jump.setAllCaps(false);
        etContent = view.findViewById(R.id.et_content);
        etContent.setText("Change Fragment");
        etContent.setAllCaps(false);
    }


}
