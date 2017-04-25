package cn.demoset.newsclient;

import java.util.List;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;
import cn.demoset.R;
import cn.demoset.newsclient.fragment.FragmentOne;
import cn.demoset.newsclient.fragment.FragmentThree;
import cn.demoset.newsclient.fragment.FragmentTwo;
import cn.demoset.newsclient.ui.IndicatorFragmentActivity;
import cn.demoset.newsclient.ui.ViewPagerCompat;

public class NewsClientActivity extends IndicatorFragmentActivity {

	public static final int FRAGMENT_ONE = 0;
	public static final int FRAGMENT_TWO = 1;
	public static final int FRAGMENT_THREE = 2;
	public BidirSlidingLayout bidirSldingLayout;
	private Button showLeftButton;
	private Button showRightButton;
	private ViewPagerCompat pager;
	private Button btn1;
	private TextView tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.news_client);
		super.onCreate(savedInstanceState);

		findView();
		registerListener();
		init();
	}

	private void findView() {
		bidirSldingLayout = (BidirSlidingLayout) findViewById(R.id.bidir_sliding_layout);
		showLeftButton = (Button) findViewById(R.id.show_left_button);
		showRightButton = (Button) findViewById(R.id.show_right_button);
		pager = (ViewPagerCompat) findViewById(R.id.pager);
		btn1 = (Button) findViewById(R.id.button1);
		tv = (TextView) findViewById(R.id.textView1);
	}

	private void registerListener() {
		bidirSldingLayout.setOnTouchListener(bidirSldingLayout);
		showLeftButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (bidirSldingLayout.isLeftLayoutVisible()) {
					bidirSldingLayout.scrollToContentFromLeftMenu();
				} else {
					bidirSldingLayout.initShowLeftState();
					bidirSldingLayout.scrollToLeftMenu();
					bidirSldingLayout.showMenu = true;
				}
			}
		});
		showRightButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (bidirSldingLayout.isRightLayoutVisible()) {
					bidirSldingLayout.scrollToContentFromRightMenu();
				} else {
					bidirSldingLayout.initShowRightState();
					bidirSldingLayout.scrollToRightMenu();
					bidirSldingLayout.showMenu = true;
				}
			}
		});
		btn1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (bidirSldingLayout.isRightLayoutVisible()) {
					bidirSldingLayout.scrollToContentFromRightMenu();
				} else {
					bidirSldingLayout.initShowRightState();
					bidirSldingLayout.scrollToRightMenu();
					bidirSldingLayout.showMenu = true;
				}
			}
		});
	}

	private void init() {
		bidirSldingLayout.showMenu = false;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 50; i++) {
			sb.append("this is line " + i + "\n");
		}
		tv.setText(sb.toString());
	}

	@Override
	public void onPageSelected(int position) {
		if (null != bidirSldingLayout) {
			if (0 == position) {
				bidirSldingLayout.showMenu = true;
				bidirSldingLayout.showLeftMenu = true;
			} else if (2 == position) {
				bidirSldingLayout.showMenu = true;
				bidirSldingLayout.showRightMenu = true;
			} else {
				bidirSldingLayout.showMenu = false;
				bidirSldingLayout.showLeftMenu = false;
				bidirSldingLayout.showRightMenu = false;
			}
			System.out.println("showMenu changed: "
					+ bidirSldingLayout.showMenu);
		}
		super.onPageSelected(position);
	}

	@Override
	protected int supplyTabs(List<TabInfo> tabs) {
		tabs.add(new TabInfo(FRAGMENT_ONE, getString(R.string.fragment_one),
				FragmentOne.class));
		tabs.add(new TabInfo(FRAGMENT_TWO, getString(R.string.fragment_two),
				FragmentTwo.class));
		tabs.add(new TabInfo(FRAGMENT_THREE,
				getString(R.string.fragment_three), FragmentThree.class));

		return FRAGMENT_TWO;
	}

}
