package cn.sjj.ktv.v;

import android.content.Context;
import android.text.ClipboardManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.sjj.ktv.R;
import cn.sjj.ktv.bean.Song;
import cn.sjj.ktv.util.ToastUtil;
import cn.sjj.ktv.widget.BaseRecyclerView2;

/**
 * @auther 宋疆疆
 * @since 2017/6/16.
 */
public class SongAdapter extends BaseRecyclerView2.ListAdapter<Song, SongAdapter.ViewHolder> {

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return new ViewHolder(inflater.inflate(R.layout.item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Song song = getData(position);
        holder.tvName.setText(song.getName());
        holder.tvAuthor.setText(song.getAuthor());
        holder.tvFrom.setText(song.getFrom());
    }

    static final class ViewHolder extends BaseRecyclerView2.ViewHolder<Song> {

        TextView tvName;
        TextView tvAuthor;
        TextView tvFrom;

        public ViewHolder(View v) {
            super(v);
            tvName = (TextView) v.findViewById(R.id.item_tv_name);
            tvAuthor = (TextView) v.findViewById(R.id.item_tv_author);
            tvFrom = (TextView) v.findViewById(R.id.item_tv_from);

            tvName.setOnClickListener(new OnSongInfoClickListener());
            tvAuthor.setOnClickListener(new OnSongInfoClickListener());
            tvFrom.setOnClickListener(new OnSongInfoClickListener());

            v.setOnClickListener(new OnSongInfoClickListener());
        }

        private final class OnSongInfoClickListener implements View.OnClickListener {

            @Override
            public void onClick(View v) {
                String text;
                if (v instanceof TextView) {
                    TextView tv = (TextView) v;
                    text = tv.getText().toString();
                } else {
                    text = String.format("%s %s %s", tvName.getText(), tvAuthor.getText(), tvFrom.getText());
                }
                ClipboardManager cm = (ClipboardManager) v.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(text);
                ToastUtil.showToast(text);
            }
        }
    }

}
