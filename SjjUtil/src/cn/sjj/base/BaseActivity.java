package cn.sjj.base;

import android.app.Activity;

import cn.sjj.IStatics;

public abstract class BaseActivity extends Activity {
	
	protected static final boolean DEBUG = IStatics.DEBUG;
	protected static final boolean UI_DEBUG = IStatics.UI_DEBUG;

	protected abstract void findView();

	protected abstract void registerListener();

	protected abstract void init();

}
