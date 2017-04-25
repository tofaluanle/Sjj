package cn.sjj.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * A widget which switchs between the {@code Camera} and the {@code VideoCamera}
 * activities.
 * @author http://blog.csdn.net/dany1202/article/details/6240031
 */
public class OthersetSeekBar extends ImageView implements View.OnTouchListener {
	@SuppressWarnings("unused")
	private static final String TAG = "Switcher";

	/** A callback to be called when the user wants to switch activity. */
	public interface OnSwitchListener {
		// Returns true if the listener agrees that the switch can be changed.
		public boolean onSwitchChanged(OthersetSeekBar source, boolean onOff);
	}

	private static final int ANIMATION_SPEED = 200;
	private static final long NO_ANIMATION = -1;
	private boolean mSwitch = false;
	private int mPosition = 0;
	private long mAnimationStartTime = NO_ANIMATION;
	private int mAnimationStartPosition;
	private OnSwitchListener mListener;

	private int mTargetPosition = 0;
	private boolean mMoveFlag = true;
	int drawableHeight = 0;
	int available = 0;
	int temp = 0;

	int progressIndex = 0;

	public OthersetSeekBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		Drawable drawable = getDrawable();
		drawableHeight = drawable.getIntrinsicHeight();
		available = 130/* getHeight() */- getPaddingTop() - getPaddingBottom()
				- drawableHeight;
		Log.i("sjj", "===================     available = " + available);
		temp = available / 8;
	}

	/*
	 * protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec) {
	 * int height = View.MeasureSpec.getSize(heightMeasureSpec); int viewHeight
	 * = height-this.getPaddingBottom()-this.getPaddingTop();
	 * super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	 * Log.i("sjj","<<<<<<<<<height = "+height); }
	 */

	public void setPosition(int pos) {

		mPosition = available * pos / 4;
		Log.i("sjj", "===========   mPosition = " + mPosition);
		tryToSetSwitch(mPosition);

	}

	public int getPosition() {

		return (mTargetPosition / (temp * 2));
	}

	// Try to change the switch position. (The client can veto it.)
	// private void tryToSetSwitch(boolean onOff)
	private void tryToSetSwitch(int position) {
		try {

			if (mPosition < temp) {
				mTargetPosition = 0;
				mMoveFlag = false;

				mSwitch = false;
			} else if (mPosition < temp * 2) {
				mTargetPosition = temp * 2;
				mMoveFlag = true;
			} else if (mPosition < temp * 3) {
				mTargetPosition = temp * 2;
				mMoveFlag = false;
			} else if (mPosition < temp * 4) {
				mTargetPosition = temp * 4;
				mMoveFlag = true;
			} else if (mPosition < temp * 5) {
				mTargetPosition = temp * 4;
				mMoveFlag = false;
			} else if (mPosition < temp * 6) {
				mTargetPosition = temp * 6;
				mMoveFlag = true;
			} else if (mPosition < temp * 7) {
				mTargetPosition = temp * 6;
				mMoveFlag = false;
				// }else if(mPosition < temp *8){
				// mTargetPosition = temp*8;
				// mMoveFlag = true;
				// }else if(mPosition <temp *9){
				// mTargetPosition = temp*8;
				// mMoveFlag = false;
			} else {
				mTargetPosition = available;
				mMoveFlag = true;

				mSwitch = true;
			}

			if (mListener != null) {
				if (!mListener.onSwitchChanged(this, mSwitch)) {
					return;
				}
			}
			// mSwitch = onOff;
		} finally {
			startParkingAnimation();
		}

		Log.i("sjj", "progressIndex = " + progressIndex + "     getPosition = "
				+ (mTargetPosition / (temp * 2)));

	}

	public void setOnSwitchListener(OnSwitchListener listener) {
		mListener = listener;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (!isEnabled())
			return false;
		final int available = getHeight() - getPaddingTop()
				- getPaddingBottom() - getDrawable().getIntrinsicHeight();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mAnimationStartTime = NO_ANIMATION;
			setPressed(true);
			trackTouchEvent(event);
			break;
		case MotionEvent.ACTION_MOVE:
			trackTouchEvent(event);
			break;
		case MotionEvent.ACTION_UP:
			trackTouchEvent(event);
			tryToSetSwitch(mPosition);
			setPressed(false);
			break;
		case MotionEvent.ACTION_CANCEL:
			tryToSetSwitch(mPosition);
			setPressed(false);
			break;
		}
		return true;
	}

	private void startParkingAnimation() {
		mAnimationStartTime = AnimationUtils.currentAnimationTimeMillis();
		mAnimationStartPosition = mPosition;

	}

	private void trackTouchEvent(MotionEvent event) {
		Drawable drawable = getDrawable();
		int drawableHeight = drawable.getIntrinsicHeight();
		final int height = getHeight();
		final int available = height - getPaddingTop() - getPaddingBottom()
				- drawableHeight;
		int x = (int) event.getY();
		mPosition = x - getPaddingTop() - drawableHeight / 2;
		if (mPosition < 0)
			mPosition = 0;
		if (mPosition > available)
			mPosition = available;

		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Drawable drawable = getDrawable();
		int drawableHeight = drawable.getIntrinsicHeight();
		int drawableWidth = drawable.getIntrinsicWidth();
		if (drawableWidth == 0 || drawableHeight == 0) {
			return; // nothing to draw (empty bounds)
		}
		final int available = getHeight() - getPaddingTop()
				- getPaddingBottom() - drawableHeight;

		if (mAnimationStartTime != NO_ANIMATION) {
			long time = AnimationUtils.currentAnimationTimeMillis();
			int deltaTime = (int) (time - mAnimationStartTime);
			/*
			 * mPosition = mAnimationStartPosition + ANIMATION_SPEED * (mSwitch
			 * ? deltaTime : -deltaTime) / 1000;
			 */

			mPosition = mAnimationStartPosition + ANIMATION_SPEED
					* (mMoveFlag ? deltaTime : -deltaTime) / 1000;

			/*
			 * if (mPosition < 0) mPosition = 0; if (mPosition > available)
			 * mPosition = available;
			 */

			if (mPosition < 0)
				mPosition = 0;

			if (mMoveFlag) {
				if (mPosition > mTargetPosition)
					mPosition = mTargetPosition;
			} else {
				if (mPosition < mTargetPosition)
					mPosition = mTargetPosition;
			}

			boolean done = (mPosition == mTargetPosition);

			if (!done) {
				invalidate();
			} else {
				mAnimationStartTime = NO_ANIMATION;
			}
		} else if (!isPressed()) {
			// mPosition = mSwitch ? available : 0;
			mPosition = mTargetPosition;
		}

		int offsetTop = getPaddingTop() + mPosition;
		int offsetLeft = (getWidth() - drawableWidth - getPaddingLeft() - getPaddingRight()) / 2;

		int saveCount = canvas.getSaveCount();
		canvas.save();
		canvas.translate(offsetLeft, offsetTop);
		drawable.draw(canvas);
		canvas.restoreToCount(saveCount);
	}

	// Consume the touch events for the specified view.
	public void addTouchView(View v) {
		v.setOnTouchListener(this);
	}

	// This implements View.OnTouchListener so we intercept the touch events
	// and pass them to ourselves.
	public boolean onTouch(View v, MotionEvent event) {
		onTouchEvent(event);
		return true;
	}

	public void setParentActivity(int index) {
		progressIndex = index;
	}
}
