package cn.sjj.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import cn.sjj.Logger;

/**
 * 用于在目标View位置上画一个边框,或图片的控件
 * 
 * @author 宋疆疆
 * @date 2014-2-26 下午5:23:35
 */
public class FocusBoxView extends View {

	private static final boolean DEBUG = false;
	private static final int DURATION = 160;
	private static final int FREQUENCY = 20;
	private static final int MOVE_END = 0;
	private int time;
	private Drawable bg;
	private int bgLeft;
	private int bgTop;
	private int bgRight;
	private int bgBottom;
	private int paddingLeft;
	private int paddingTop;
	private int paddingRight;
	private int paddingBottom;
	private boolean isFinish;
	private View target;
	private int vLeft;
	private int vTop;
	private int vRight;
	private int vBottom;
	private int dL;
	private int dT;
	private int dR;
	private int dB;
	private OnMoveEndListener onMoveEndListener;
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MOVE_END:
				moveEnd();
				break;
			}
		};
	};

	public interface OnMoveEndListener {
		void onMoveEnd(View target);
	}

	public void setOnMoveEndListener(OnMoveEndListener listener) {
		this.onMoveEndListener = listener;
	}

	public FocusBoxView(Context context) {
		super(context);
		init(context);
	}

	public FocusBoxView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public FocusBoxView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
	}

	public void setBox(int id) {
		bg = getResources().getDrawable(id);
	}

	public void setBox(Drawable drawable) {
		bg = drawable;
	}

	private void setTargetViewPadding() {
		paddingLeft = ((ViewGroup) getParent()).getPaddingLeft();
		paddingRight = ((ViewGroup) getParent()).getPaddingRight();
		Rect rect = new Rect();
		getWindowVisibleDisplayFrame(rect);
		paddingTop = rect.height() - getHeight() + rect.top
				- ((ViewGroup) getParent()).getPaddingTop();
		paddingBottom = rect.height() - getHeight() + rect.top
				- ((ViewGroup) getParent()).getPaddingBottom();
	}

	public void goToTargetView(final View v, final int vLeft, final int vTop) {
		isFinish = true;
		if (null == v) {
			return;
		}
		target = v;
		new Thread() {

			@Override
			public void run() {
				time = 0;
				setTargetViewPadding();
				int vRight = vLeft + v.getWidth();
				int vBottom = vTop + v.getHeight();
				int dL = (vLeft - bgLeft + 5) * 1000 / (DURATION / FREQUENCY);
				int dT = (vTop - bgTop + 5) * 1000 / (DURATION / FREQUENCY);
				int dR = (vRight - bgRight + 5) * 1000 / (DURATION / FREQUENCY);
				int dB = (vBottom - bgBottom + 5) * 1000
						/ (DURATION / FREQUENCY);
				isFinish = false;
				while (DURATION >= time && !isFinish) {
					if (bg != null) {
						if (dL > 0) {
							if ((bgLeft = (bgLeft * 1000 + dL) / 1000) > vLeft) {
								bgLeft = vLeft;
							}
						} else {
							if ((bgLeft = (bgLeft * 1000 + dL) / 1000) < vLeft) {
								bgLeft = vLeft;
							}
						}
						if (dT > 0) {
							if ((bgTop = (bgTop * 1000 + dT) / 1000) > vTop) {
								bgTop = vTop;
							}
						} else {
							if ((bgTop = (bgTop * 1000 + dT) / 1000) < vTop) {
								bgTop = vTop;
							}
						}
						if (dR > 0) {
							if ((bgRight = (bgRight * 1000 + dR) / 1000) > vRight) {
								bgRight = vRight;
							}
						} else {
							if ((bgRight = (bgRight * 1000 + dR) / 1000) < vRight) {
								bgRight = vRight;
							}
						}
						if (dB > 0) {
							if ((bgBottom = (bgBottom * 1000 + dB) / 1000) > vBottom) {
								bgBottom = vBottom;
							}
						} else {
							if ((bgBottom = (bgBottom * 1000 + dB) / 1000) < vBottom) {
								bgBottom = vBottom;
							}
						}
					}
					postInvalidate();
					SystemClock.sleep(FREQUENCY);
					time += FREQUENCY;
				}
				mHandler.sendEmptyMessage(MOVE_END);
			}

		}.start();
	}

	public void goToTargetView(View v) {
		isFinish = true;
		if (null == v) {
			return;
		}
		target = v;
		int[] location = new int[2];
		v.getLocationOnScreen(location);
		vLeft = location[0];
		vTop = location[1];
		vRight = vLeft + v.getWidth();
		vBottom = vTop + v.getHeight();
		if (DEBUG) {
			Logger.d("vLeft: " + vLeft + ", vTop: " + vTop + ", vRight: "
					+ vRight + ", vBottom: " + vBottom);
			Logger.d("bgLeft: " + bgLeft + ", bgTop: " + bgTop + ", bgRight: "
					+ bgRight + ", bgBottom: " + bgBottom);
		}
		new Thread() {
			@Override
			public void run() {
				time = 0;
				setTargetViewPadding();

				dL = (vLeft - bgLeft + 5) * 1000 / (DURATION / FREQUENCY);
				dT = (vTop - bgTop + 5) * 1000 / (DURATION / FREQUENCY);
				dR = (vRight - bgRight + 5) * 1000 / (DURATION / FREQUENCY);
				dB = (vBottom - bgBottom + 5) * 1000 / (DURATION / FREQUENCY);
				if (DEBUG) {
					Logger.d("dL: " + (float) dL / 1000 + ", dT: " + (float) dT
							/ 1000 + ", dR: " + (float) dR / 1000 + ", dB: "
							+ (float) dB / 1000);
				}
				isFinish = false;
				while (DURATION >= time && !isFinish) {
					if (bg != null) {
						if (dL > 0) {
							if ((bgLeft = (bgLeft * 1000 + dL) / 1000) > vLeft) {
								bgLeft = vLeft;
							}
						} else {
							if ((bgLeft = (bgLeft * 1000 + dL) / 1000) < vLeft) {
								bgLeft = vLeft;
							}
						}
						if (dT > 0) {
							if ((bgTop = (bgTop * 1000 + dT) / 1000) > vTop) {
								if (DEBUG) {
									Logger.i("bgTop > vTop, force make bgTop = vTop, bgTop: "
											+ bgTop + ", vTop: " + vTop);
								}
								bgTop = vTop;
							}
						} else {
							if ((bgTop = (bgTop * 1000 + dT) / 1000) < vTop) {
								bgTop = vTop;
							}
						}
						if (dR > 0) {
							if ((bgRight = (bgRight * 1000 + dR) / 1000) > vRight) {
								bgRight = vRight;
							}
						} else {
							if ((bgRight = (bgRight * 1000 + dR) / 1000) < vRight) {
								bgRight = vRight;
							}
						}
						if (dB > 0) {
							if ((bgBottom = (bgBottom * 1000 + dB) / 1000) > vBottom) {
								bgBottom = vBottom;
							}
						} else {
							if ((bgBottom = (bgBottom * 1000 + dB) / 1000) < vBottom) {
								bgBottom = vBottom;
							}
						}
					}
					if (DEBUG) {
						// Logger.v("thread id: " +
						// Thread.currentThread().getId()
						// + ", time: " + time + ", isFinish: " + isFinish);
						Logger.v("bgLeft: " + bgLeft + ", bgTop: " + bgTop
								+ ", bgRight: " + bgRight + ", bgBottom: "
								+ bgBottom);
					}
					postInvalidate();
					SystemClock.sleep(FREQUENCY);
					time += FREQUENCY;
				}
				mHandler.sendEmptyMessage(MOVE_END);
			}

		}.start();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (null != bg) {
			bg.setBounds(bgLeft - paddingLeft, bgTop - paddingTop, bgRight
					- paddingRight, bgBottom - paddingBottom);
			bg.draw(canvas);
		}
	}

	private void moveEnd() {
		if (onMoveEndListener != null) {
			onMoveEndListener.onMoveEnd(target);
		}
	}
}
