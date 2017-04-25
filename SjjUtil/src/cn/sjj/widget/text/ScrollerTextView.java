package cn.sjj.widget.text;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Scroller;
import android.widget.TextView;

/**
 * 自定义了一个方法实现平滑滑动效果，利用Scroller类
 * @author 宋疆疆
 * @date 2014-4-8 下午1:36:35
 */
public class ScrollerTextView extends TextView {

	private Scroller mScrollers;

	public ScrollerTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public ScrollerTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);

	}

	public ScrollerTextView(Context context) {
		super(context);
		init(context);

	}

	private void init(Context context) {
		mScrollers = new Scroller(context);
	}

	void smoothScrollTo(int x, int y) {
		mScrollers.startScroll(getScrollX(), getScrollY(), getScrollX() + x,
				getScrollY() + y, 1000);
		invalidate();
	}

	@Override
	public void scrollTo(int x, int y) {
		super.scrollTo(x, y);
	}

	@Override
	public void computeScroll() {
//		 super.computeScroll();
		if (mScrollers.computeScrollOffset()) {
			int x = mScrollers.getCurrX();
			int y = mScrollers.getCurrY();

			scrollTo(x, y);
			invalidate();
		}

	}
}
