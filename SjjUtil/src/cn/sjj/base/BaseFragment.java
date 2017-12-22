package cn.sjj.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.sjj.IStatics;
import cn.sjj.Logger;
import cn.sjj.util.TextFormat;


/**
 * Fragment的基类，提供一些框架性的代码
 * Created by 宋疆疆 on 2014/12/29.
 */
public abstract class BaseFragment extends Fragment {

    protected static final boolean DEBUG    = false;
    protected static final boolean UI_DEBUG = IStatics.UI_DEBUG;

    protected FragmentManager mFm;
    protected View            mFragmentView;//ENN fragment界面的的组件

    protected void findView() {
    }

    protected void registerListener() {
    }

    protected abstract void init();

    @Override
    public void onAttach(Context context) {
        Logger.v(DEBUG, this + "   onAttach");
        super.onAttach(context);
        mFm = getFragmentManager();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.v(DEBUG, this + "   onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Logger.i(DEBUG, this + "   onCreateView ");
        return null;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Logger.v(DEBUG, this + "   onViewCreated ");
        super.onViewCreated(view, savedInstanceState);
        findView();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Logger.v(DEBUG, this + "   onActivityCreated ");
        super.onActivityCreated(savedInstanceState);
        init();
        registerListener();
    }

    @Override
    public void onStart() {
        Logger.v(DEBUG, this + "   onStart ");
        super.onStart();
    }

    @Override
    public void onResume() {
        Logger.v(DEBUG, this + "   onResume");
        super.onResume();
    }

    @Override
    public void onPause() {
        Logger.v(DEBUG, this + "   onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
        Logger.v(DEBUG, this + "   onStop");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        Logger.v(DEBUG, this + "   onDestroyView ");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Logger.v(DEBUG, this + "   onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Logger.v(DEBUG, this + "   onDetach ");
        super.onDetach();
    }

    @Override
    public String toString() {
        String name = this.getClass().getSimpleName();
        name = TextFormat.flushLeft(' ', 21, name);
        return name;
    }

}
