package cn.sjj.glide;

import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.bumptech.glide.request.target.BitmapImageViewTarget;

/**
 * 用来防止快速滑动时，图片加载错位的类
 *
 * @auther 宋疆疆
 * @date 2016/5/10.
 */
public class CBitmapImageViewTarget extends BitmapImageViewTarget {

    protected RecyclerView.ViewHolder mHolder;
    protected String mKey;

    public CBitmapImageViewTarget(ImageView view, RecyclerView.ViewHolder holder, String key) {
        super(view);
        mHolder = holder;
        mKey = key;
    }

}
