package cn.demoset.vertical.viewpager;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.demoset.R;

/**
 * 
 * @author https://github.com/rharter/ViewPager-Android
 * @date 2014-3-26 上午10:54:38
 */
public class VerticalViewPagerActivity extends Activity {
	ViewPager mPager;
	ViewPager mPager2;
	HorizontalPagerAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vertical_viewpager);

		mPager = (ViewPager) findViewById(R.id.pager);
		mPager2 = (ViewPager) findViewById(R.id.pager2);
		mAdapter = new HorizontalPagerAdapter(this);
		mPager.setAdapter(mAdapter);
		mPager2.setAdapter(mAdapter);
	}

	public static class HorizontalPagerAdapter extends PagerAdapter {

		static final List<PageInfo> PAGES = new ArrayList<PageInfo>();
		static {
			PAGES.add(new PageInfo(android.R.color.holo_blue_light, "This one swipes vertically"));
			PAGES.add(new PageInfo(android.R.color.holo_red_light, "Simply set the orientation flag"));
			PAGES.add(new PageInfo(android.R.color.holo_green_light, "Use the same PagerAdapter"));
			PAGES.add(new PageInfo(android.R.color.holo_orange_light, "Nothing Unusual"));
		}

		Context mContext;
		LayoutInflater mLayoutInflater;

		public HorizontalPagerAdapter(Context context) {
			super();
			mContext = context;
			mLayoutInflater = LayoutInflater.from(mContext);
		}

		@Override
		public int getCount() {
			return PAGES.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object o) {
			return view == o;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View v = mLayoutInflater.inflate(R.layout.listview_item, container,
					false);

			final PageInfo info = PAGES.get(position);

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
