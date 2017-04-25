package cn.sjj.widget.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import cn.sjj.R;

public class GvAdapter extends BaseAdapter {

	private Context mContext;
	public Test t;

	public GvAdapter(Context mContext) {
		super();
		this.mContext = mContext;
	}

	@Override
	public int getCount() {

		return 10;
	}

	@Override
	public Object getItem(int position) {

		return position;
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView iv = new ImageView(mContext);
		iv.setImageResource(R.drawable.ic_launcher);
		iv.setBackgroundColor(Color.RED);
		return iv;
	}
	
	public class Test {
		public int index;
	}

}
