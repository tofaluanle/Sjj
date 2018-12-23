package cn.sjj.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * 仅仅为了将事件传递给其他控件的类，一般情况下是为了扩大触摸范围
 *
 * @author 宋疆疆
 * @date 2016/8/2.
 */
public class DelegateView extends RelativeLayout {

    private View mView;

    public void setView(View view) {
        mView = view;
    }

    public DelegateView(Context context) {
        super(context);
    }

    public DelegateView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DelegateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DelegateView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mView != null) {
            return mView.onTouchEvent(event);
        }
        return super.onTouchEvent(event);
    }
}
