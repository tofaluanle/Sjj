package cn.manjuu.searchproject.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import cn.manjuu.searchproject.R;
import cn.manjuu.searchproject.util.Logger;
import cn.manjuu.searchproject.util.PreferencesUtil;

public class SettingFragment extends Fragment {

	private PreferencesUtil pf;
	private RelativeLayout mMainView;
	private RadioGroup rg_search_range;
	private RadioButton rb_search_range_1;
	private RadioButton rb_search_range_3;
	private RadioButton rb_search_range_10;
	private RadioGroup rg_choice_map;
	private RadioButton rb_map_self;
	private RadioButton rb_map_baidu;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mMainView = (RelativeLayout) inflater.inflate(R.layout.setting, null);
		findView();
		registerListener();
		return mMainView;
	}

	private void findView() {
		rg_search_range = (RadioGroup) mMainView
				.findViewById(R.id.rg_search_range);
		rb_search_range_1 = (RadioButton) mMainView
				.findViewById(R.id.rb_search_range_1);
		rb_search_range_3 = (RadioButton) mMainView
				.findViewById(R.id.rb_search_range_3);
		rb_search_range_10 = (RadioButton) mMainView
				.findViewById(R.id.rb_search_range_10);

		rg_choice_map = (RadioGroup) mMainView.findViewById(R.id.rg_choice_map);
		rb_map_self = (RadioButton) mMainView.findViewById(R.id.rb_map_self);
		rb_map_baidu = (RadioButton) mMainView.findViewById(R.id.rb_map_baidu);
	}

	private void registerListener() {
		SearchRangeCheckListener searchRangeListener = new SearchRangeCheckListener();
		rb_search_range_1.setOnCheckedChangeListener(searchRangeListener);
		rb_search_range_3.setOnCheckedChangeListener(searchRangeListener);
		rb_search_range_10.setOnCheckedChangeListener(searchRangeListener);

		ChoiceMapCheckListener mapListener = new ChoiceMapCheckListener();
		rb_map_self.setOnCheckedChangeListener(mapListener);
		rb_map_baidu.setOnCheckedChangeListener(mapListener);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		init();
	}

	private void init() {
		pf = new PreferencesUtil(getActivity());
		initSearchRange();
		initChoiceMap();
	}

	private void initSearchRange() {
		rb_search_range_1.setTag(1);
		rb_search_range_3.setTag(2);
		rb_search_range_10.setTag(3);

		int range = pf.getIntPref(PreferencesUtil.SETTING_RANGE, 3);
		int id = 0;
		switch (range) {
		case 1:
			id = rb_search_range_1.getId();
			break;
		case 2:
			id = rb_search_range_3.getId();
			break;
		case 3:
			id = rb_search_range_10.getId();
			break;
		default:
			id = rb_search_range_1.getId();
			break;
		}
		rg_search_range.check(id);
	}

	private void initChoiceMap() {
		rb_map_baidu.setTag(1);
		rb_map_self.setTag(2);

		int map = pf.getIntPref(PreferencesUtil.SETTING_MAP, 1);
		Logger.d("choice map: " + map);
		int id = 0;
		switch (map) {
		case 1:
			id = rb_map_baidu.getId();
			break;
		case 2:
			id = rb_map_self.getId();
			break;
		default:
			id = rb_map_baidu.getId();
			break;
		}
		rg_choice_map.check(id);
	}

	private final class SearchRangeCheckListener implements
			OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (isChecked) {
				int i = (Integer) buttonView.getTag();
				pf.setIntPref(PreferencesUtil.SETTING_RANGE, i);
			}
		}

	}

	private final class ChoiceMapCheckListener implements
			OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (isChecked) {
				int i = (Integer) buttonView.getTag();
				pf.setIntPref(PreferencesUtil.SETTING_MAP, i);
			}
		}

	}

}
