package com.bryanrady.optimization.base.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

    private static final String TAG = "FirstFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG,"onCreateView");
        View view = inflater.inflate(R.layout.fragment_statck_content, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG,"onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG,"onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG,"onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG,"onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG,"onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG,"onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG,"onDetach");
    }

    private void initView(View view){
        Button jump = view.findViewById(R.id.btn_jump);
        jump.setOnClickListener(this);
        jump.setText("Firs Fragment");
        EditText etContent = view.findViewById(R.id.et_content);
        etContent.setText("Firs Fragment");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_jump:
                FragmentManager fm = getFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                SecondFragment secondFragment = new SecondFragment();
                transaction.hide(this);
                transaction.add(R.id.fl_content,secondFragment);
            //    transaction.replace(R.id.fl_content,secondFragment);
                //添加一个Fragment事务到回退栈
                transaction.addToBackStack(null);
                transaction.commit();
                break;
        }
    }
}
