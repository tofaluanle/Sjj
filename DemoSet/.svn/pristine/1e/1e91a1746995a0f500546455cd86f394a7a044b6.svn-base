package cn.demoset.newsclient.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import cn.demoset.R;
import cn.demoset.newsclient.NewsClientActivity;
import cn.demoset.newsclient.ui.ViewPagerCompat;

public class BaseFragment extends Fragment {
	protected View mMainView;
	protected static ArrayList<Map<String, Object>> mlistItems;
	protected Context mContext;

	static {
		mlistItems = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < 20; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("name", "name#" + i);
			map.put("sex", i % 2 == 0 ? "male" : "female");
			mlistItems.add(map);
		}
	}

	public BaseFragment() {
		super();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mContext = activity.getApplicationContext();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mMainView = inflater.inflate(R.layout.fragment_one, container, false);

		ViewPagerCompat vp = (ViewPagerCompat) mMainView
				.findViewById(R.id.viewpager);
		vp.setAdapter(new MyPagerAdapter(getActivity()));
		vp.setCurrentItem(Integer.MAX_VALUE / 2);
		vp.bsl = ((NewsClientActivity) getActivity()).bidirSldingLayout;

		ListView listView = (ListView) mMainView.findViewById(R.id.list);
		SimpleAdapter adapter = new SimpleAdapter(mContext, mlistItems,
				R.layout.listview_item, new String[] { "name", "sex" },
				new int[] { R.id.name, R.id.download });
		listView.setAdapter(adapter);
		return mMainView;
	}

	public static class MyPagerAdapter extends PagerAdapter {

		static final List<PageInfo> PAGES = new ArrayList<PageInfo>();
		static {
			PAGES.add(new PageInfo(android.R.color.holo_blue_light,
					"This one swipes vertically"));
			PAGES.add(new PageInfo(android.R.color.holo_red_light,
					"Simply set the orientation flag"));
			PAGES.add(new PageInfo(android.R.color.holo_green_light,
					"Use the same PagerAdapter"));
			PAGES.add(new PageInfo(android.R.color.holo_orange_light,
					"Nothing Unusual"));
		}

		Context mContext;
		LayoutInflater mLayoutInflater;

		public MyPagerAdapter(Context context) {
			super();
			mContext = context;
			mLayoutInflater = LayoutInflater.from(mContext);
		}

		@Override
		public int getCount() {
			return Integer.MAX_VALUE;
		}

		@Override
		public boolean isViewFromObject(View view, Object o) {
			return view == o;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View v = mLayoutInflater.inflate(R.layout.listview_item, container,
					false);

			final PageInfo info = PAGES.get(position % PAGES.size());

			View page = v.findViewById(R.id.container);
			page.setBackgroundResource(info.background);

			TextView content = (TextView) v.findViewById(R.id.name);
			content.setText(info.text);

			container.addView(v);
			return v;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		private static class PageInfo {
			int background;
			String text;

			PageInfo(int background, String text) {
				this.background = background;
				this.text = text;
			}
		}
	}

}
