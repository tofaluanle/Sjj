package cn.sjj.ktv.v;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.sjj.ktv.R;

/**
 * @author 宋疆疆
 * @since 2017/8/8.
 */
public class SongAdapter extends BaseAdapter {

    private List<String> data;

    public SongAdapter(List<String> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.song_item, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.tv.setText(data.get(position));
        return convertView;
    }

    private static class ViewHolder {
        TextView tv;

        public ViewHolder(View v) {
            v.setClickable(false);
            v.setOnClickListener(null);
            this.tv = (TextView) v.findViewById(R.id.item_tv_name);
        }
    }
}
