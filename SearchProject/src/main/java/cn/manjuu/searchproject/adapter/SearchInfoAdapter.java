package cn.manjuu.searchproject.adapter;

import java.util.List;
import java.util.zip.Inflater;

import com.baidu.platform.comapi.map.h;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.manjuu.searchproject.R;
import cn.manjuu.searchproject.domain.PublishInfo;

public class SearchInfoAdapter extends BaseAdapter {

	private List<PublishInfo> infoList;
	private Context mContext;
	private LayoutInflater inflater;

	public void setInfoList(List<PublishInfo> infoList) {
		this.infoList = infoList;
	}

	public SearchInfoAdapter(Context context, List<PublishInfo> infoList) {
		super();
		this.mContext = context;
		this.infoList = infoList;
		init();
	}

	private void init() {
		inflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		if(null == infoList) {
			return 0;
		}
		return infoList.size();
	}

	@Override
	public Object getItem(int position) {
		if(null == infoList) {
			return null;
		}
		return infoList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		ViewHolder holder = null;
		if (null == convertView) {
			view = inflater.inflate(R.layout.search_item, null);
			holder = new ViewHolder();
			holder.tv_search_title = (TextView) view
					.findViewById(R.id.tv_title_search);
			holder.tv_search_content = (TextView) view
					.findViewById(R.id.tv_content_search);
			view.setTag(holder);
		} else {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}

		holder.tv_search_title.setText(infoList.get(position).getTitle());
		holder.tv_search_content.setText(infoList.get(position).getContent());

		return view;
	}

	private final class ViewHolder {
		private TextView tv_search_title;
		private TextView tv_search_content;
	}

}
