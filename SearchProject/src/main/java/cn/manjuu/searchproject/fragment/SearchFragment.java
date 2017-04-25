package cn.manjuu.searchproject.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.manjuu.searchproject.R;
import cn.manjuu.searchproject.activity.ExpandSearchInfoActivity;
import cn.manjuu.searchproject.adapter.SearchInfoAdapter;
import cn.manjuu.searchproject.domain.PublishInfo;
import cn.manjuu.searchproject.manager.BLocationManager;
import cn.manjuu.searchproject.net.SAsyncHttpClient;
import cn.manjuu.searchproject.statics.IStatics;
import cn.manjuu.searchproject.util.Logger;
import cn.manjuu.searchproject.util.NetUtil;
import cn.manjuu.searchproject.util.PreferencesUtil;
import cn.manjuu.searchproject.widget.FuzzyArrayAdapter;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class SearchFragment extends Fragment implements OnClickListener,
		TextWatcher, OnItemClickListener {

	private RelativeLayout mMainView;
	private AutoCompleteTextView actv_search;
	private ListView lv_search;
	private FuzzyArrayAdapter<String> sugAdapter;
	private Button btn_search;
	private SearchInfoAdapter lvAdapter;
	private TextView tv_hidden;
	private PreferencesUtil pf;
	private View line_publish_1;
	private View line_publish_2;
	private String oldKeyword;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mMainView = (RelativeLayout) inflater.inflate(R.layout.search, null);
		findView();
		registerListener();
		return mMainView;
	}

	private void findView() {
		actv_search = (AutoCompleteTextView) mMainView
				.findViewById(R.id.actv_search);
		btn_search = (Button) mMainView.findViewById(R.id.btn_search);
		lv_search = (ListView) mMainView.findViewById(R.id.lv_search);
		tv_hidden = (TextView) mMainView.findViewById(R.id.tv_hidden);
		line_publish_1 = mMainView.findViewById(R.id.line_publish_1);
		line_publish_2 = mMainView.findViewById(R.id.line_publish_2);

		line_publish_1.setVisibility(View.GONE);
		line_publish_2.setVisibility(View.GONE);
		tv_hidden.setVisibility(View.GONE);
	}

	private void registerListener() {
		actv_search.addTextChangedListener(this);
		btn_search.setOnClickListener(this);
		lv_search.setOnItemClickListener(this);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		init();
	}

	private void init() {
		oldKeyword = "";
		sugAdapter = new FuzzyArrayAdapter<String>(getActivity(),
				android.R.layout.simple_dropdown_item_1line);
		actv_search.setAdapter(sugAdapter);

		BLocationManager.getInstance().registerListener(
				new MyLocationListenner());

		pf = new PreferencesUtil(getActivity());
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		Logger.d("search keyword: " + s.toString());
		if (TextUtils.isEmpty(s) || oldKeyword.equals(s.toString())
				|| sugAdapter.contains(s.toString())) {
			return;
		}
		oldKeyword = s.toString();
		SAsyncHttpClient client = new SAsyncHttpClient(getActivity());
		RequestParams params = new RequestParams();
		params.add("action", "search_keyword");
		params.add("search_keyword", s.toString());
		client.post(IStatics.URL_SERVER, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] body) {
						super.onSuccess(arg0, arg1, body);
						Logger.d(new String(body));
						analyzeResult(new String(body));
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable e) {
						super.onFailure(arg0, arg1, arg2, e);

						e.printStackTrace();
					}
				});
	}

	@Override
	public void afterTextChanged(Editable s) {
	}

	private void analyzeResult(String string) {
		if (string.equals("[]")) {
			return;
		}
		try {
			JSONArray array = new JSONArray(string);
			sugAdapter.clear();
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = array.getJSONObject(i);
				String keyword = object.getString("search_keyword");
				sugAdapter.add(keyword);
			}
			sugAdapter.notifyDataSetChanged();
			int selection = actv_search.getSelectionStart();
			actv_search.setText(actv_search.getText());
			actv_search.setSelection(selection);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		((InputMethodManager) getActivity().getSystemService(
				Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
				getActivity().getCurrentFocus().getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
		switch (v.getId()) {
		case R.id.btn_search:
			search();
			break;
		}
	}

	private void search() {
		if (0 == actv_search.getText().length()) {
			Toast.makeText(getActivity(),
					getString(R.string.info_search_no_tag), Toast.LENGTH_SHORT)
					.show();
			actv_search.requestFocus();
			return;
		}
		if (!NetUtil.isNetworkAvalible(getActivity())) {
			Toast.makeText(getActivity(), getString(R.string.no_network),
					Toast.LENGTH_LONG).show();
			return;
		}
		tv_hidden.setVisibility(View.GONE);
		btn_search.setText(getResources().getString(R.string.btn_searching));
		btn_search.setClickable(false);
		if (null != lvAdapter) {
			line_publish_1.setVisibility(View.GONE);
			line_publish_2.setVisibility(View.GONE);
			lvAdapter.setInfoList(null);
			lvAdapter.notifyDataSetChanged();
			lv_search.startLayoutAnimation();
		}
		BLocationManager.getInstance().registerListener(
				new MyLocationListenner());
		BLocationManager.getInstance().getLoc();
	}

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null) {
				Logger.w("location is null");
				btn_search.setText(getResources()
						.getString(R.string.btn_search));
				btn_search.setClickable(true);
				return;
			}

			double latitude = location.getLatitude();
			double longitude = location.getLongitude();
			Logger.d("latitude: " + latitude);
			Logger.d("longitude: " + longitude);
			if (-74 > latitude || -180 > longitude || 74 < latitude
					|| 180 < longitude || 1 > (latitude * 1e6)
					|| 1 > (longitude * 1e6)) {
				Toast.makeText(getActivity(),
						getResources().getString(R.string.search_loc_fail),
						Toast.LENGTH_SHORT).show();
				btn_search.setText(getResources()
						.getString(R.string.btn_search));
				btn_search.setClickable(true);
				return;
			}

			int range = getRange();
			RequestParams params = new RequestParams();
			params.put("action", "search");
			params.put("keyword", actv_search.getText().toString().trim());
			params.put("minlatitude", latitude - range * IStatics.SCALE + "");
			params.put("minlongitude", longitude - range * IStatics.SCALE + "");
			params.put("maxlatitude", latitude + range * IStatics.SCALE + "");
			params.put("maxlongitude", longitude + range * IStatics.SCALE + "");

			Logger.d(params.toString());

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(IStatics.TIME_OUT);
			client.post(IStatics.URL_SERVER, params,
					new AsyncHttpResponseHandler() {

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] body) {
							super.onSuccess(arg0, arg1, body);
							Logger.d(new String(body));
							btn_search.setText(getResources().getString(
									R.string.btn_search));
							btn_search.setClickable(true);
							fillListView(new String(body));
						}

						@Override
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable e) {
							super.onFailure(arg0, arg1, arg2, e);

							e.printStackTrace();
							btn_search.setText(getResources().getString(
									R.string.btn_search));
							btn_search.setClickable(true);
						}

					});
		}

		private int getRange() {
			int range = pf.getIntPref(PreferencesUtil.SETTING_RANGE, 3);
			switch (range) {
			case 1:
				range = 1000;
				break;
			case 2:
				range = 3000;
				break;
			case 3:
				range = 10 * 1000;
				break;
			default:
				range = 1000;
				break;
			}
			return range;
		}

		@Override
		public void onReceivePoi(BDLocation arg0) {

		}
	}

	private void fillListView(String body) {
		List<PublishInfo> infoList = new ArrayList<PublishInfo>();
		try {
			JSONArray array = new JSONArray(body);
			PublishInfo info = null;
			JSONObject object = null;
			for (int i = 0; i < array.length(); i++) {
				object = array.getJSONObject(i);
				info = new PublishInfo();
				info.setTitle(object.getString("title"));
				info.setContent(object.getString("content"));
				info.setContact(object.getString("contact"));
				info.setContactType(object.getInt("contact_type"));
				info.setTag(object.getString("tag"));
				info.setLatitude(object.getDouble("latitude"));
				info.setLongitude(object.getDouble("longitude"));
				infoList.add(info);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		if (0 == infoList.size()) {
			Toast.makeText(getActivity(),
					getString(R.string.info_search_nothing), Toast.LENGTH_SHORT)
					.show();
			return;
		}
		line_publish_1.setVisibility(View.VISIBLE);
		line_publish_2.setVisibility(View.VISIBLE);
		if (null != lvAdapter) {
			lvAdapter.setInfoList(infoList);
			lvAdapter.notifyDataSetChanged();
		} else {
			lvAdapter = new SearchInfoAdapter(getActivity(), infoList);
			lv_search.setAdapter(lvAdapter);
		}
		lv_search.startLayoutAnimation();
	}

	@Override
	public void onResume() {
		super.onResume();

		tv_hidden.requestFocus();
		tv_hidden.requestFocusFromTouch();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		BLocationManager.getInstance().stop();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		PublishInfo info = (PublishInfo) lvAdapter.getItem(position);
		Intent intent = new Intent(getActivity(),
				ExpandSearchInfoActivity.class);
		Bundle extras = new Bundle();
		extras.putParcelable("info", info);
		intent.putExtras(extras);
		startActivity(intent);
		getActivity().overridePendingTransition(R.anim.translate_to_left_in,
				R.anim.translate_to_left_out);
	}

}
