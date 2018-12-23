package cn.sjj.widget.recycler;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import cn.sjj.util.LazyUtil;

/**
 * LinearLayoutManager的封装基类，用来方便开发调试
 *
 * @author 宋疆疆
 * @since 2016/12/1.
 */
public class BaseLinearLayoutManager extends LinearLayoutManager {

    private Object mUIObject;

    /**
     * @param context
     * @param mUIObject 直接用Activity或者Fragment的this
     */
    public BaseLinearLayoutManager(Context context, Object mUIObject) {
        super(context);
        this.mUIObject = mUIObject;
    }

    /**
     * @param context
     * @param attrs
     * @param defStyleAttr
     * @param defStyleRes
     * @param mUIObject    直接用Activity或者Fragment的this
     */
    public BaseLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes, Object mUIObject) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mUIObject = mUIObject;
    }

    /**
     * @param context
     * @param orientation
     * @param reverseLayout
     * @param mUIObject     直接用Activity或者Fragment的this
     */
    public BaseLinearLayoutManager(Context context, int orientation, boolean reverseLayout, Object mUIObject) {
        super(context, orientation, reverseLayout);
        this.mUIObject = mUIObject;
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException e) {
            LazyUtil.sendUnExpect(this, new Exception(), mUIObject.getClass().getSimpleName());
            throw e;
        }
    }

}
