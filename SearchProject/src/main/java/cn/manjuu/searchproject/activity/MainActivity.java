package cn.manjuu.searchproject.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import cn.manjuu.searchproject.R;
import cn.manjuu.searchproject.engine.AutoUpdateEngine;
import cn.manjuu.searchproject.manager.BLocationManager;
import cn.manjuu.searchproject.manager.NavigationManager;
import cn.manjuu.searchproject.util.Logger;

public class MainActivity extends FragmentActivity implements OnClickListener {

	private RelativeLayout rl_publish;
	private RelativeLayout rl_search;
	private RelativeLayout rl_setting;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		findView();
		registerListener();
		init();
	}

	private void findView() {
		rl_publish = (RelativeLayout) findViewById(R.id.rl_publish);
		rl_search = (RelativeLayout) findViewById(R.id.rl_search);
		rl_setting = (RelativeLayout) findViewById(R.id.rl_setting);
	}

	private void registerListener() {
		rl_publish.setOnClickListener(this);
		rl_search.setOnClickListener(this);
		rl_setting.setOnClickListener(this);
	}

	private void init() {
		AutoUpdateEngine engine = new AutoUpdateEngine(this);
		engine.checkUpdateInfo();

		NavigationManager.getInstance().setActivity(this);
		NavigationManager.getInstance().init();

		BLocationManager.getInstance().init(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_search:
		case R.id.rl_publish:
		case R.id.rl_setting:
			NavigationManager.getInstance().onClick(v);
			break;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		switch (keyCode) {
//		case KeyEvent.KEYCODE_BACK:
//			moveTaskToBack(false);
//			return true;
//		}
		
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onPause() {
		Logger.v("MainActivity onPause");
		super.onPause();
	}

	@Override
	protected void onStop() {
		Logger.v("MainActivity onStop");
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		Logger.v("MainActivity onDestroy");
		super.onDestroy();
	}
}
