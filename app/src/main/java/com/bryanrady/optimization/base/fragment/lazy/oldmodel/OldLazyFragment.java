package com.bryanrady.optimization.base.fragment.lazy.oldmodel;

import com.bryanrady.optimization.base.fragment.lazy.base.LogFragment;

public abstract class OldLazyFragment extends LogFragment {

    private boolean mIsLoaded = false;
    private boolean mIsVisibleToUser;

    public OldLazyFragment(String tag) {
        super(tag);
    }

    @Override
    public void onResume() {
        super.onResume();
        judgeLazyLoad();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        mIsVisibleToUser = !hidden;
        judgeLazyLoad();
    }

    private void judgeLazyLoad(){
        if (!mIsLoaded && !mIsVisibleToUser) {
            lazyLoad();
            mIsLoaded = true;
        }
    }

    public abstract void lazyLoad();

}
