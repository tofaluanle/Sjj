package cn.demoset;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import cn.demoset.bidiectionality.BidirectionalityActivity;
import cn.demoset.focusBox.FocusBoxActivity;
import cn.demoset.ledongli.LeDongLiActivity;
import cn.demoset.newsclient.NewsClientActivity;
import cn.demoset.progresswheel.ProgressWheelActivity;
import cn.demoset.scrolltab.ScrollTabActivity;
import cn.demoset.vertical.viewpager.VerticalViewPagerActivity;

public class MainActivity extends ListActivity {

	private List<String> array;
	private static final int NEWS_CLIENT = 0;
	private static final int LE_DONG_LI = 1;
	private static final int CIRCLE_PROGRESS = 2;
	private static final int TAB_SCROLLER = 3;
	private static final int FOCUS_BOX = 4;
	private static final int BIDIR_SLIDING = 5;
	private static final int FALL_PHOTO = 6;
	private static final int VERTICAL_VIEWPAGER = 7;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initArray();
		ListAdapter adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, array);
		setListAdapter(adapter);
	}

	private void initArray() {
		array = new ArrayList<String>();
		array.add("网易新闻布局实现");
		array.add("乐动力，两个小太阳的自传和中点循转");
		array.add("自定义的圆形进度条");
		array.add("Tab标题下划线可滑动");
		array.add("可随着焦点目标移动的控件");
		array.add("双向滑动菜单");
		array.add("图片瀑布墙");
		array.add("垂直的viewpager");
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Intent intent = null;
		switch (position) {
		case LE_DONG_LI:
			intent = new Intent(this, LeDongLiActivity.class);
			break;
		case CIRCLE_PROGRESS:
			intent = new Intent(this, ProgressWheelActivity.class);
			break;
		case TAB_SCROLLER:
			intent = new Intent(this, ScrollTabActivity.class);
			break;
		case FOCUS_BOX:
			intent = new Intent(this, FocusBoxActivity.class);
			break;
		case BIDIR_SLIDING:
			intent = new Intent(this, BidirectionalityActivity.class);
			break;
		case FALL_PHOTO:
			intent = new Intent(this,
					cn.demoset.fallphotowall.MainActivity.class);
			break;
		case NEWS_CLIENT:
			intent = new Intent(this, NewsClientActivity.class);
			break;
		case VERTICAL_VIEWPAGER:
			intent = new Intent(this, VerticalViewPagerActivity.class);
			break;
		}
		if (null != intent) {
			startActivity(intent);
		}
		super.onListItemClick(l, v, position, id);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
