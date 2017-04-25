package cn.sjj.widget.text;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class MarqueeTextView extends AbsMarqueeTextView {

	private int currX;
	private int targetX;

	public int getCurrX() {
		return currX;
	}
	
	public MarqueeTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public MarqueeTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MarqueeTextView(Context context) {
		super(context);
	}

	@Override
	public void startMarquee(int mode) {
		targetX = (int) getPaint().measureText(getText().toString());
//		System.out.println(targetX);
		switch (mode) {
		case MODE_SCROLL:
			runner = new RollRunner();
			currX = -getWidth();
			break;
		case MODE_LAYOUT:
			runner = new RollByLayoutRunner();
			currX = -windowWidth;
			break;
		}
		mHandler.postDelayed(runner, rate);
	}

	@Override
	public void stopMarquee() {
		mHandler.removeCallbacks(runner);
	}

	private class RollRunner implements Runnable {
		@Override
		public void run() {
//			System.out.println(currX);
			scrollTo(currX, 0);
			if (currX < targetX) {
				mHandler.postDelayed(runner, rate);
				currX += speed;
			} else {
				currX = -getWidth();
				mHandler.postDelayed(runner, rate);
			}
		}
	}

	private class RollByLayoutRunner implements Runnable {
		@Override
		public void run() {
//			System.out.println(currX);
			RelativeLayout.LayoutParams params = (LayoutParams) getLayoutParams();
			params.leftMargin = -currX;
			setLayoutParams(params);
			if (currX < targetX) {
				mHandler.postDelayed(runner, rate);
				currX += speed;
			} else {
				currX = -windowWidth;
				mHandler.postDelayed(runner, rate);
			}
		}
	}
	
	public void fixCurrX(int update) {
		currX += update;
	}


}
