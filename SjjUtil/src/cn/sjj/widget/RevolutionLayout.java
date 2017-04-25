package cn.sjj.widget;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsoluteLayout;

public class RevolutionLayout extends AbsoluteLayout {

	private boolean animationing;
	private int centerIndex;
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			View v = (View) msg.obj;
			AbsoluteLayout.LayoutParams params = (LayoutParams) v
					.getLayoutParams();
			params.x = msg.arg1;
			params.y = msg.arg2;
			Bundle data = msg.getData();
			float toX = data.getFloat("toX");
			int type = data.getInt("type");
			switch (type) {
			case 1:
				params.width = 70;
				params.height = 70;
				break;
			case 2:
				params.width = 35;
				params.height = 35;
				break;
			case 3:
				params.width = 35;
				params.height = 35;
				break;
			case 4:
				params.width = 70;
				params.height = 70;
				break;
			case 5:
				params.width = 140;
				params.height = 140;
				break;
			case 6:
				params.width = 35;
				params.height = 35;
				break;
			case 7:
				params.width = 70;
				params.height = 70;
				break;
			case 8:
				params.width = 35;
				params.height = 35;
				break;
			case 9:
				params.width = 140;
				params.height = 140;
				break;
			case 10:
				params.width = 70;
				params.height = 70;
				break;
			}
			v.setLayoutParams(params);
			System.out.println(v.getTag() + " x: " + msg.arg1 + ", y: "
					+ msg.arg2 + ", toX: " + toX + ", type: " + type);
		};
	};
	
	public int getCenterIndex() {
		return centerIndex;
	}

	public void setCenterIndex(int centerIndex) {
		this.centerIndex = centerIndex;
	}

	public RevolutionLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

	}

	public RevolutionLayout(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	public RevolutionLayout(Context context) {
		super(context);

	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (!animationing) {
			mHandler.postDelayed((new Runnable() {
				@Override
				public void run() {
					animationing = false;
				}
			}), 480);
			switch (keyCode) {
			case KeyEvent.KEYCODE_DPAD_LEFT:
				if (++centerIndex > getChildCount()) {
					centerIndex = 1;
				}
				for (int i = 0; i < getChildCount(); i++) {
					if (0 == i) {
						goToTargetView(getChildAt(0),
								getChildAt(getChildCount() - 1), true);
					} else {
						goToTargetView(getChildAt(i), getChildAt(i - 1), true);
					}
				}
				break;
			case KeyEvent.KEYCODE_DPAD_RIGHT:
				if (--centerIndex < 1) {
					centerIndex = getChildCount();
				}
				for (int i = 0; i < getChildCount(); i++) {
					if (getChildCount() - 1 == i) {
						goToTargetView(getChildAt(getChildCount() - 1),
								getChildAt(0), false);
					} else {
						goToTargetView(getChildAt(i), getChildAt(i + 1), false);
					}
				}
				break;

			default:
				break;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	private void goToTargetView(View src, View target, boolean isLeft) {
		float x = src.getX();
		float x2 = target.getX();
		float y = src.getY();
		float y2 = target.getY();
		float toX = 1.5f;
		float toY = 1.5f;
		float toXValue = (x2 - x);
		float toYValue = (y2 - y);
		int type = 0;
		if (x > x2 && y > y2) {
			toX = 0.5f;
			toY = 0.5f;
			toXValue = (x2 - x - src.getWidth() / 4) / toX;
			toYValue = (y2 - y - src.getHeight() / 4) / toY;
			if (isLeft) {
				type = 1;
			} else {
				type = 6;
			}
		} else if (x < x2 && y > y2) {
			toX = 0.5f;
			toY = 0.5f;
			toXValue = (x2 - x - src.getWidth() / 4) / toX;
			toYValue = (y2 - y - src.getHeight() / 4) / toY;
			if (isLeft) {
				type = 2;
			} else {
				type = 7;
			}
		} else if (x < x2 && y == y2) {
			toX = 1f;
			toY = 1f;
			toXValue = (x2 - x) / toX;
			toYValue = (y2 - y) / toY;
			type = 3;
		} else if (x > x2 && y == y2) {
			toX = 1f;
			toY = 1f;
			toXValue = (x2 - x) / toX;
			toYValue = (y2 - y) / toY;
			type = 8;
		} else if (x < x2 && y < y2) {
			toX = 2f;
			toY = 2f;
			toXValue = (x2 - x + src.getWidth() / 2) / toX;
			toYValue = (y2 - y + src.getHeight() / 2) / toY;
			if (isLeft) {
				type = 4;
			} else {
				type = 9;
			}
		} else if (x > x2 && y < y2) {
			toX = 2f;
			toY = 2f;
			toXValue = (x2 - x + src.getWidth() / 2) / toX;
			toYValue = (y2 - y + src.getHeight() / 2) / toY;
			if (isLeft) {
				type = 5;
			} else {
				type = 10;
			}
		}

		System.out.println("x: " + x + ", x2: " + x2 + ", y: " + y + ", y2: "
				+ y2 + ", toXValue: " + toXValue + ", toYValue: " + toYValue);
		TranslateAnimation ta = new TranslateAnimation(Animation.ABSOLUTE, 0,
				Animation.ABSOLUTE, toXValue, Animation.ABSOLUTE, 0,
				Animation.ABSOLUTE, toYValue);
		ScaleAnimation sa = new ScaleAnimation(1, toX, 1f, toY,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		sa.setInterpolator(new AccelerateDecelerateInterpolator());
		AnimationSet as = new AnimationSet(false);
		as.addAnimation(ta);
		as.addAnimation(sa);
		as.setDuration(400);
		// as.setFillAfter(true);
		as.setAnimationListener(new MyAnimationListener(src, (int) (x2),
				(int) (y2), toX, type));
		src.startAnimation(as);
	}

	private final class MyAnimationListener implements AnimationListener {

		private View v;
		private int l;
		private int t;
		private float toX;
		private int type;

		public MyAnimationListener(View v, int l, int t, float r, int b) {
			super();
			this.v = v;
			this.l = l;
			this.t = t;
			this.toX = r;
			this.type = b;
		}

		@Override
		public void onAnimationStart(Animation animation) {
			animationing = true;
		}

		@Override
		public void onAnimationEnd(Animation animation) {
			// v.clearAnimation();
			Message msg = new Message();
			msg.arg1 = l;
			msg.arg2 = t;
			Bundle data = new Bundle();
			data.putFloat("toX", toX);
			data.putInt("type", type);
			msg.setData(data);
			msg.obj = v;
			mHandler.sendMessageDelayed(msg, 10);

			System.out.println(v.getTag() + " x: " + l + ", y: " + t
					+ ", toX: " + toX + ", type: " + type);
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
		}

	}

}
