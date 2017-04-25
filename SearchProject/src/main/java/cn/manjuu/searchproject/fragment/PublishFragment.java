package cn.manjuu.searchproject.fragment;

import org.apache.http.Header;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;
import cn.manjuu.searchproject.R;
import cn.manjuu.searchproject.domain.PublishInfo;
import cn.manjuu.searchproject.manager.BLocationManager;
import cn.manjuu.searchproject.net.SAsyncHttpClient;
import cn.manjuu.searchproject.statics.IStatics;
import cn.manjuu.searchproject.util.Logger;
import cn.manjuu.searchproject.util.NetUtil;
import cn.manjuu.searchproject.util.PreferencesUtil;
import cn.manjuu.searchproject.util.SIMCardInfo;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

public class PublishFragment extends Fragment implements OnClickListener,
		OnItemSelectedListener {

	private RelativeLayout mMainView;
	private Spinner spn_contact;
	private ArrayAdapter<CharSequence> adapter;
	private EditText et_contact;
	private EditText et_title;
	private EditText et_content;
	private EditText et_tag;
	private EditText et_tag2;
	private EditText et_tag3;
	private Button btn_save;
	private Button btn_submit;
	private PreferencesUtil prefer;
	private PublishInfo info;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mMainView = (RelativeLayout) inflater.inflate(R.layout.publish, null);
		findView();
		registerListener();
		return mMainView;
	}

	private void findView() {
		spn_contact = (Spinner) mMainView.findViewById(R.id.spn_contact);
		et_contact = (EditText) mMainView.findViewById(R.id.et_contact);
		et_title = (EditText) mMainView.findViewById(R.id.et_title);
		et_content = (EditText) mMainView.findViewById(R.id.et_content);
		et_tag = (EditText) mMainView.findViewById(R.id.et_tag);
		et_tag2 = (EditText) mMainView.findViewById(R.id.et_tag2);
		et_tag3 = (EditText) mMainView.findViewById(R.id.et_tag3);
		btn_save = (Button) mMainView.findViewById(R.id.btn_save);
		btn_submit = (Button) mMainView.findViewById(R.id.btn_submit);
	}

	private void registerListener() {
		btn_save.setOnClickListener(this);
		btn_submit.setOnClickListener(this);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		init();
	}

	private void init() {
		prefer = new PreferencesUtil(getActivity());

		adapter = ArrayAdapter.createFromResource(getActivity(),
				R.array.contact_type, android.R.layout.simple_spinner_item);
		// 设置下拉列表的风格
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spn_contact.setAdapter(adapter);
		spn_contact.setOnItemSelectedListener(this);

		info = new PublishInfo();

		initView();
	}

	private void initView() {
		String contact = prefer.getStringPref(PreferencesUtil.PUBLISH_CONTACT,
				null);
		int contactType = prefer.getIntPref(
				PreferencesUtil.PUBLISH_CONTACT_TYPE, 0);
		String content = prefer.getStringPref(PreferencesUtil.PUBLISH_CONTENT,
				null);
		String title = prefer
				.getStringPref(PreferencesUtil.PUBLISH_TITLE, null);
		String tag = prefer.getStringPref(PreferencesUtil.PUBLISH_TAG, null);

		spn_contact.setSelection(contactType);
		if (null != title) {
			et_title.setText(title);
		}
		if (null != content) {
			et_content.setText(content);
		}
		if (null != contact) {
			et_contact.setText(contact);
		}
		if (null != tag) {
			String[] split = tag.split(" ");
			if (0 == split.length) {
				return;
			}
			et_tag.setText(split[0]);
			try {
				et_tag2.setText(split[1]);
				et_tag3.setText(split[2]);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}

	@Override
	public void onClick(View v) {
		InputMethodManager imm = (InputMethodManager) getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		boolean active = imm.isAcceptingText();
		if (active) {
			View currentFocus = getActivity().getCurrentFocus();
			imm.hideSoftInputFromWindow(currentFocus.getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
		switch (v.getId()) {
		case R.id.btn_save:
			saveInfo(true);
			break;
		case R.id.btn_submit:
			submit();
			break;
		}
	}

	private void submit() {
		if (50 < et_title.getText().toString().length()) {
			Toast.makeText(getActivity(), getString(R.string.et_title),
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (!NetUtil.isNetworkAvalible(getActivity())) {
			Toast.makeText(getActivity(), getString(R.string.no_network),
					Toast.LENGTH_LONG).show();
			return;
		}

		btn_submit.setText(getResources().getString(R.string.btn_submiting));
		btn_submit.setClickable(false);
		saveInfo(false);
		BLocationManager.getInstance().registerListener(
				new MyLocationListenner());
		BLocationManager.getInstance().getLoc();
	}

	private void saveInfo(boolean flag) {
		getInfoByView();

		prefer.setStringPref(PreferencesUtil.PUBLISH_TITLE, info.getTitle());
		prefer.setStringPref(PreferencesUtil.PUBLISH_CONTACT, info.getContact());
		prefer.setIntPref(PreferencesUtil.PUBLISH_CONTACT_TYPE,
				info.getContactType());
		prefer.setStringPref(PreferencesUtil.PUBLISH_CONTENT, info.getContent());
		prefer.setStringPref(PreferencesUtil.PUBLISH_TAG, info.getTag());

		if (flag) {
			Toast.makeText(getActivity(),
					getResources().getString(R.string.save_success),
					Toast.LENGTH_SHORT).show();
		}
	}

	private void getInfoByView() {
		info.setContact(et_contact.getText().toString().trim());
		info.setContactType(spn_contact.getSelectedItemPosition());
		info.setContent(et_content.getText().toString().trim());
		info.setTitle(et_title.getText().toString().trim());
		info.setTag(et_tag.getText().toString().trim().replace(" ", "") + " "
				+ et_tag2.getText().toString().trim().replace(" ", "") + " "
				+ et_tag3.getText().toString().trim().replace(" ", ""));

		Logger.i(info.toString());
	}

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null) {
				Logger.w("location is null");
				btn_submit.setText(getResources()
						.getString(R.string.btn_submit));
				btn_submit.setClickable(true);
				return;
			}

			SIMCardInfo siminfo = new SIMCardInfo(getActivity());

			double latitude = location.getLatitude();
			double longitude = location.getLongitude();
			Logger.d("latitude: " + latitude);
			Logger.d("longitude: " + longitude);
			if (-74 > latitude || -180 > longitude || 74 < latitude
					|| 180 < longitude || 1 > (latitude * 1e6)
					|| 1 > (longitude * 1e6)) {
				Toast.makeText(getActivity(),
						getResources().getString(R.string.public_loc_fail),
						Toast.LENGTH_SHORT).show();
				btn_submit.setText(getResources()
						.getString(R.string.btn_submit));
				btn_submit.setClickable(true);
				return;
			}

			RequestParams params = new RequestParams();
			params.put("action", "publish");
			params.put("phone_num", siminfo.getNativePhoneNumber());
			params.put("latitude", latitude + "");
			params.put("longitude", longitude + "");
			params.put("title", info.getTitle());
			params.put("content", info.getContent());
			params.put("contact_type", info.getContactType() + "");
			params.put("contact", info.getContact());
			params.put("tag", info.getTag());

			Logger.d(params.toString());

			SAsyncHttpClient client = new SAsyncHttpClient(getActivity());
			RequestHandle post = client.post(IStatics.URL_SERVER, params,
					new AsyncHttpResponseHandler() {

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] body) {
							super.onSuccess(arg0, arg1, body);
							Logger.d(new String(body));
							btn_submit.setText(getResources().getString(
									R.string.btn_submit));
							btn_submit.setClickable(true);
						}

						@Override
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable e) {
							super.onFailure(arg0, arg1, arg2, e);
							Logger.d(new String(arg2));
							e.printStackTrace();
							btn_submit.setText(getResources().getString(
									R.string.btn_submit));
							btn_submit.setClickable(true);
						}

					});
			if (null == post) {
				btn_submit.setText(getResources()
						.getString(R.string.btn_submit));
				btn_submit.setClickable(true);
			}
		}

		@Override
		public void onReceivePoi(BDLocation arg0) {

		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		BLocationManager.getInstance().stop();
	}
}
