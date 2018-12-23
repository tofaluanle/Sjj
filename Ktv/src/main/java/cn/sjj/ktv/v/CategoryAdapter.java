package cn.sjj.ktv.v;

import android.content.Context;
import android.text.ClipboardManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.sjj.ktv.R;
import cn.sjj.ktv.bean.Song;
import cn.sjj.ktv.util.ToastUtil;
import cn.sjj.ktv.widget.BaseRecyclerView2;
import cn.sjj.ktv.widget.FlowTagLayout;

/**
 * @author 宋疆疆
 * @since 2017/6/16.
 */
public class CategoryAdapter extends BaseRecyclerView2.ListAdapter<Song, CategoryAdapter.ViewHolder> {

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return new ViewHolder(inflater.inflate(R.layout.item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Song song = getData(position);
        List<String> info = new ArrayList<>(song.getTags());
        info.add(0, song.getFrom());
        info.add(0, song.getAuthor());
        info.add(0, song.getName());
        SongAdapter adapter = new SongAdapter(info);
        holder.ftl.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    static final class ViewHolder extends BaseRecyclerView2.ViewHolder<Song> {

        FlowTagLayout ftl;

        public ViewHolder(final View v) {
            super(v);

            ftl = (FlowTagLayout) v.findViewById(R.id.ftl);
            ftl.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_NONE);
            ftl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onSongClick(v);
                }
            });
            ftl.setOnTagClickListener(new FlowTagLayout.OnTagClickListener() {
                @Override
                public void onItemClick(FlowTagLayout parent, View view, int position) {
                    onSongClick(view);
                }
            });
        }

        private void onSongClick(View v) {
            String text;
            if (v instanceof LinearLayout) {
                LinearLayout ll = (LinearLayout) v;
                TextView tv = (TextView) ll.getChildAt(0);
                text = tv.getText().toString();
            } else {
                ViewGroup vp = (ViewGroup) v;
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < vp.getChildCount(); i++) {
                    LinearLayout ll = (LinearLayout) vp.getChildAt(i);
                    TextView tv = (TextView) ll.getChildAt(0);
                    sb.append(tv.getText() + " ");
                }
                sb.delete(sb.length() - 1, sb.length());
                text = sb.toString();
            }
            ClipboardManager cm = (ClipboardManager) v.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            cm.setText(text);
            ToastUtil.showToast(text);
        }
    }

}
