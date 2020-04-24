package com.bryanrady.optimization.base.fragment.lazy.base;

import android.app.Activity;

import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;

public class FragmentTransactionUtil {

    /**
     * 加载根Fragment
     * @param containerId
     * @param rootFragment
     */
    public static void loadRootFragment(int containerId, Fragment rootFragment){
        //loadFragmentsTransaction(containerViewId, 0, childFragmentManager, rootFragment)
    }

    /**
     * 加载同级的Fragment
     * @param containerId
     * @param showPosition
     * @param fragment
     */
    public static void loadFragments(int containerId, int showPosition, Fragment fragment){
        //loadFragmentsTransaction(containerViewId, showPosition, childFragmentManager, *fragments)
    }

    /**
     * 显示目标Fragment 并隐藏其他fragment
     * @param showFragment
     */
    public static void showHideFragment(Fragment showFragment){
        //        showHideFragmentTransaction(childFragmentManager, showFragment)
    }

    public static void showHideFragmentTransaction(FragmentActivity activity, Fragment showFragment){
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

    }

    /**
     * 显示需要显示的Fragment[showFragment]，并设置其最大Lifecycle为Lifecycle.State.RESUMED。
     * 同时隐藏其他Fragment,并设置最大Lifecycle为Lifecycle.State.STARTED
     * @param fragmentManager
     * @param showFragment
     */
//    private fun showHideFragmentTransaction(fragmentManager: FragmentManager, showFragment: Fragment) {
//        fragmentManager.beginTransaction().apply {
//            show(showFragment)
//            setMaxLifecycle(showFragment, Lifecycle.State.RESUMED)
//
//            //获取其中所有的fragment,其他的fragment进行隐藏
//            val fragments = fragmentManager.fragments
//            for (fragment in fragments) {
//                if (fragment != showFragment) {
//                    hide(fragment)
//                    setMaxLifecycle(fragment, Lifecycle.State.STARTED)
//                }
//            }
//        }.commit()
//    }

}
