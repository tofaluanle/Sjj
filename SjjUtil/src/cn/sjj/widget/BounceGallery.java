package cn.sjj.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Gallery;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

/**
 * 具有反弹效果的Gallery
 * @author 宋疆疆
 * @date 2014年6月23日 下午4:51:17
 */
public class BounceGallery extends Gallery {

	public BounceGallery(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public BounceGallery(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	public BounceGallery(Context context) {
		super(context);

	}

	// 最初按下的坐标
	private float x;// 移动结束时的坐标
	private float endx;
	// 按下去时选中的项的下标
	private int selectedIndex;
	private float mxwidth;
	private float damping = 0.3f;

	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		if (action == MotionEvent.ACTION_DOWN) {
			x = event.getX();
			endx = 0;
			selectedIndex = getSelectedItemPosition();
			if (selectedIndex == 0)
				mxwidth = getWidth() - x;
			if (selectedIndex == 0)
				mxwidth = x;
		} else if (action == MotionEvent.ACTION_MOVE) {
			if (selectedIndex == 0 || selectedIndex == getCount() - 1) {
				float mx = event.getX();
				Animation translate = null;
				endx = mx - x;
				if (selectedIndex == 0) {
					if (endx > 0 && endx < getWidth() && mx > x - mxwidth) {
						translate = new TranslateAnimation(endx * damping, endx
								* damping, 0, 0);
						translate.setDuration(25);
						translate.setFillAfter(true);
						startAnimation(translate);
					} else {
						endx = 0;
						selectedIndex = getSelectedItemPosition();
					}
				} else {
					if (endx < 0 && endx > -getWidth() * 2 && mx < x + mxwidth) {
						translate = new TranslateAnimation(endx * damping, endx
								* damping, 0, 0);
						translate.setDuration(25);
						translate.setFillAfter(true);
						startAnimation(translate);
					} else {
						endx = 0;
						selectedIndex = getSelectedItemPosition();
					}
				}
			}
		} else if (action == MotionEvent.ACTION_UP) {
			if ((selectedIndex == 0 || selectedIndex == getCount() - 1)) {
				int index = getSelectedItemPosition();
				if (index == 0 || index == getCount() - 1) {
					if (endx != 0) {
						Animation translate = new TranslateAnimation(endx
								* damping, 0, 0, 0);
						translate.setDuration(300);
						translate.setFillAfter(true);
						startAnimation(translate);

					}
				}
			}
		}
		return super.onTouchEvent(event);
	}

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		System.out.println("onFling");
		int kEvent;
		if (e2.getX() > e1.getX()) {
			kEvent = KeyEvent.KEYCODE_DPAD_LEFT;
		} else {
			kEvent = KeyEvent.KEYCODE_DPAD_RIGHT;
		}
		onKeyDown(kEvent, null);
		return true;
	}

}
