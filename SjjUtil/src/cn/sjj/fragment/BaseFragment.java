package cn.sjj.fragment;

import android.support.v4.app.Fragment;

import cn.sjj.IStatics;

/**
 * Created by Administrator on 2014/12/29.
 */
public abstract  class BaseFragment extends Fragment {

    protected static final boolean DEBUG = IStatics.DEBUG;
    protected static final boolean UI_DEBUG = IStatics.UI_DEBUG;

    protected abstract void findView();

    protected abstract void registerListener();

    protected abstract void init();
}
