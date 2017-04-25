package cn.demoset.newsclient;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class MyLinearLayout extends LinearLayout {

	public MyLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	public MyLinearLayout(Context context) {
		super(context);

	}

	public BidirSlidingLayout bsl;

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (bsl.isLeftLayoutVisible() || bsl.isRightLayoutVisible()) {
			System.out.println("MyRelativeLayout onInterceptTouchEvent: true");
			return true;
		}
		return super.onInterceptTouchEvent(ev);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (bsl.isLeftLayoutVisible() || bsl.isRightLayoutVisible()) {
			System.out.println("MyRelativeLayout onTouchEvent: true");
			return true;
		}
		return super.onTouchEvent(event);
	}
}
