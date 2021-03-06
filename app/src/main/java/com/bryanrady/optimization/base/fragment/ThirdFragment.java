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
public class ThirdFragment extends Fragment{

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("wangqingbin","ThirdFragment onCreateView");
        View view = inflater.inflate(R.layout.fragment_statck_content, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d("wangqingbin","ThirdFragment onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("wangqingbin","ThirdFragment onCreate");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("wangqingbin","ThirdFragment onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("wangqingbin","ThirdFragment onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("wangqingbin","ThirdFragment onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("wangqingbin","ThirdFragment onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("wangqingbin","ThirdFragment onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("wangqingbin","ThirdFragment onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("wangqingbin","ThirdFragment onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("wangqingbin","ThirdFragment onDetach");
    }

    private void initView(View view){
        Button jump = view.findViewById(R.id.btn_jump);
        jump.setText("Third Fragment");
        jump.setAllCaps(false);
        EditText etContent = view.findViewById(R.id.et_content);
        etContent.setText("Third Fragment");
        etContent.setAllCaps(false);
    }

}
