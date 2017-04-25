package cn.sjj.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;
import android.graphics.Region.Op;
import android.graphics.PorterDuffXfermode;
import android.graphics.Region;
import android.os.Handler;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 利用canvas的clip*方法实现图片逐渐消失的功能
 * 
 * @author 宋疆疆
 * @date 2014-3-4 下午3:47:35
 */
public class AnimaImageView extends ImageView {

	private static final int FREQUECY = 5;
	private int index;
	private int duration = 300;
	private int now;
	private AnimationType type = AnimationType.None;
	private Handler mHandler = new Handler();
	private boolean isAppear;

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public AnimaImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public AnimaImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();

	}

	public AnimaImageView(Context context) {
		super(context);
		init();

	}

	private void init() {
	}

	@Override
	protected void onDraw(Canvas canvas) {
		executeAnimation(canvas);
		super.onDraw(canvas);
	}

	public void appear(AnimationType t) {
		isAppear = true;
		runAnimation(t);
	}

	public void disappear(AnimationType t) {
		isAppear = false;
		runAnimation(t);
	}

	private void runAnimation(AnimationType t) {
		System.out.println("width: " + getWidth() + ", height: " + getHeight());
		now = 0;
		type = t;
		switch (type) {
		case AntiCenterCircle:
			index = (int) Math.sqrt(getHeight() / 2 * getHeight() / 2
					+ getWidth() / 2 * getWidth() / 2);
			break;
		default:
			index = 0;
			break;
		}

		mHandler.post(new RefreshRunnable());
	}

	private void executeAnimation(Canvas canvas) {
		switch (type) {
		case TopToBottom:
			topToButtom(canvas);
			break;
		case BottomToTop:
			bottomToTop(canvas);
			break;
		case LeftToRight:
			leftToRight(canvas);
			break;
		case RightToLeft:
			rightToLeft(canvas);
			break;
		case CenterCircle:
			centerCircle(canvas);
			break;
		case AntiCenterCircle:
			antiCenterCircle(canvas);
			break;

		default:
			break;
		}
		System.out.println("index: " + index);
	}

	private void topToButtom(Canvas canvas) {
		index += getHeight() * 1000 * 1000 / (duration * 1000 / FREQUECY)
				/ 1000;
		if (isAppear) {
			canvas.clipRect(0, index, getWidth(), getHeight(), Op.DIFFERENCE);
		} else {
			canvas.clipRect(0, index, getWidth(), getHeight());
		}
	}

	private void bottomToTop(Canvas canvas) {
		index += getHeight() * 1000 * 1000 / (duration * 1000 / FREQUECY)
				/ 1000;
		if (isAppear) {
			canvas.clipRect(0, 0, getWidth(), getHeight() - index,
					Op.DIFFERENCE);
		} else {
			canvas.clipRect(0, 0, getWidth(), getHeight() - index);
		}
	}

	private void leftToRight(Canvas canvas) {
		index += getHeight() * 1000 * 1000 / (duration * 1000 / FREQUECY)
				/ 1000;
		if (isAppear) {
			canvas.clipRect(index, 0, getWidth(), getHeight(), Op.DIFFERENCE);
		} else {
			canvas.clipRect(index, 0, getWidth(), getHeight());
		}
	}

	private void rightToLeft(Canvas canvas) {
		index += getHeight() * 1000 * 1000 / (duration * 1000 / FREQUECY)
				/ 1000;
		if (isAppear) {
			canvas.clipRect(0, 0, getWidth() - index, getHeight(),
					Op.DIFFERENCE);
		} else {
			canvas.clipRect(0, 0, getWidth() - index, getHeight());
		}
	}

	private void antiCenterCircle(Canvas canvas) {
		index -= Math.sqrt(getHeight() / 2 * getHeight() / 2 + getWidth() / 2
				* getWidth() / 2)
				* 1000 * 1000 / (duration * 1000 / FREQUECY) / 1000;
		Path mPath = new Path();
		mPath.addCircle(getWidth() / 2, getHeight() / 2, index,
				Path.Direction.CCW);
		if (isAppear) {
			canvas.clipPath(mPath, Region.Op.DIFFERENCE);
		} else {
			canvas.clipPath(mPath, Region.Op.REPLACE);
		}
	}

	private void centerCircle(Canvas canvas) {
		index += Math.sqrt(getHeight() / 2 * getHeight() / 2 + getWidth() / 2
				* getWidth() / 2)
				* 1000 * 1000 / (duration * 1000 / FREQUECY) / 1000;
		Path mPath = new Path();
		mPath.addCircle(getWidth() / 2, getHeight() / 2, index,
				Path.Direction.CCW);
		if (isAppear) {
			canvas.clipPath(mPath, Region.Op.REPLACE);
		} else {
			canvas.clipPath(mPath, Region.Op.DIFFERENCE);
		}
	}

	public enum AnimationType {
		None, //
		TopToBottom, // 从上到下逐步消失
		BottomToTop, // 从下到上逐步消失
		LeftToRight, // 从左到右逐步消失
		RightToLeft, // 从右到左逐步消失
		CenterCircle, // 从中心逐步消失
		AntiCenterCircle // 从外围到中心逐步消失
	}

	private class RefreshRunnable implements Runnable {

		@Override
		public void run() {
			invalidate();
			System.out.println("now: " + now);
			now += FREQUECY;
			if (now > duration) {
				return;
			}
			mHandler.postDelayed(this, FREQUECY);
		}

	}
}
