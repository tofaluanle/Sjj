package cn.manjuu.searchproject.activity;

import java.net.URISyntaxException;

import com.baidu.location.f;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import cn.manjuu.searchproject.R;
import cn.manjuu.searchproject.domain.PublishInfo;
import cn.manjuu.searchproject.util.Logger;
import cn.manjuu.searchproject.util.PreferencesUtil;

public class ExpandSearchInfoActivity extends BaseActivity implements
		OnClickListener {

	private TextView tv_expand_title;
	private TextView tv_expand_contact;
	private TextView tv_expand_content;
	private PublishInfo mInfo;
	private Button btn_expand_map;
	private PreferencesUtil pf;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.expand_search_info);
		super.onCreate(savedInstanceState);

	}

	@Override
	protected void findView() {
		tv_expand_title = (TextView) findViewById(R.id.tv_expand_title);
		tv_expand_contact = (TextView) findViewById(R.id.tv_expand_contact);
		tv_expand_content = (TextView) findViewById(R.id.tv_expand_content);
		btn_expand_map = (Button) findViewById(R.id.btn_expand_map);
	}

	@Override
	protected void registerListener() {
		btn_expand_map.setOnClickListener(this);
	}

	@Override
	protected void init() {
		String[] types = getResources().getStringArray(R.array.contact_type);

		mInfo = getIntent().getParcelableExtra("info");
		tv_expand_title.setText(mInfo.getTitle());
		String type = types[mInfo.getContactType()];
		tv_expand_contact.setText(type + "：" + mInfo.getContact());
		tv_expand_content.setText(mInfo.getContent());

		pf = new PreferencesUtil(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_expand_map:
			choiceMap();
			break;
		}
	}

	private void choiceMap() {
		int mapType = pf.getIntPref(PreferencesUtil.SETTING_MAP, 1);
		if (2 == mapType) {
			toMyMap();
			return;
		}
		PackageManager pm = getPackageManager();
		boolean isHaveBDMap = false;
		try {
			pm.getApplicationInfo("com.baidu.BaiduMap",
					PackageManager.GET_META_DATA);
			isHaveBDMap = true;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		if (isHaveBDMap) {
			toBaiduMap();
		} else {
			Toast.makeText(this, getString(R.string.no_baidu_map_app),
					Toast.LENGTH_SHORT).show();
			toMyMap();
		}
	}

	private void toMyMap() {
		Intent intent = new Intent(this, MyBaiDuMap.class);
		intent.putExtra("latitude", mInfo.getLatitude());
		intent.putExtra("longitude", mInfo.getLongitude());
		startActivity(intent);
	}

	private void toBaiduMap() {
		Intent intent = null;
		try {
			String content = mInfo.getContent().length() >= 23 ? mInfo
					.getContent().substring(0, 20) + "..." : mInfo.getContent();
			String title = mInfo.getTitle().length() >= 23 ? mInfo.getTitle()
					.substring(0, 20) + "..." : mInfo.getTitle();
			String uri = "intent://map/marker?location=" + mInfo.getLatitude()
					+ "," + mInfo.getLongitude() + "&title=" + title
					+ "&content=" + content + "&src=" + getPackageName() + "|"
					+ getString(R.string.app_name)
					+ "#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end";
			Logger.d(uri);
			intent = Intent.getIntent(uri);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		startActivity(intent);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (KeyEvent.KEYCODE_BACK == keyCode) {
			finish();
			overridePendingTransition(R.anim.translate_to_right_in,
					R.anim.translate_to_right_out);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
