package cn.manjuu.searchproject.activity;

import android.app.Activity;
import android.os.Bundle;

public abstract class BaseActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		findView();
		registerListener();
		init();
	}

	protected abstract void findView();

	protected abstract void registerListener();

	protected abstract void init();
}
