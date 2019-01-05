package cn.sjj.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * 仿微信侧滑删除的控件
 *
 * @author https://github.com/xytyl/SwipeView
 * @since 2018/12/28.
 */
public class SwipeView extends ViewGroup {

    private static final int MAX_BOUNDARY = 10;

    private static SwipeView sView;

    private int downX, moveX;
    private boolean  isShowRight;
    private int      deleteWidth;
    private boolean  mShowRightEnable;
    private Scroller scroller;

    public SwipeView(Context context) {
        super(context);
        init();
    }

    public SwipeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SwipeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        scroller = new Scroller(getContext());
        mShowRightEnable = true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        View child = getChildAt(0);
        int margin = ((MarginLayoutParams) child.getLayoutParams()).topMargin
                + ((MarginLayoutParams) child.getLayoutParams()).bottomMargin;
        setMeasuredDimension(width, getChildAt(0).getMeasuredHeight() + margin);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        getChildAt(0).layout(l, t, r, b);

        View view = getChildAt(1);
        view.layout(r, t, r + view.getMeasuredWidth(), b);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!scroller.isFinished() && !mShowRightEnable) {
            return false;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                moveX = (int) event.getRawX();
                int moved = moveX - downX;
                if (isShowRight) {
                    moved -= deleteWidth;
                }
                if (-moved <= deleteWidth) {
                    scrollTo(-moved, 0);
                } else {
//                    int delta = -moved - deleteWidth;
//                    int percent = (int) ((deleteWidth - delta / 5) / (float) deleteWidth * delta);
                    int percent = 0;
                    View view = getChildAt(1);
                    LayoutParams layoutParams = view.getLayoutParams();
                    layoutParams.width = deleteWidth + percent;
                    view.setLayoutParams(layoutParams);
                    scrollTo(deleteWidth + percent, 0);
                }
                if (getScrollX() <= 0) {
                    scrollTo(0, 0);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (getScrollX() >= deleteWidth / 2) {
                    isShowRight = true;
                    sView = this;
                    recoverAnimation();
                    smoothScrollToPosition(deleteWidth);
                } else {
                    isShowRight = false;
                    smoothScrollToPosition(0);
                }
                break;
            default:
                break;
        }
        return true;
    }

    private void smoothScrollToPosition(int destX) {
        int width = getScrollX();
        int delta = destX - width;
        scroller.startScroll(width, 0, delta, 0, 150);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            postInvalidate();
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (sView != null) {
            sView.close();
        }
    }

    private void close() {
        smoothScrollToPosition(0);
        isShowRight = false;
    }

    public void setShowRightEnable(boolean enable) {
        this.mShowRightEnable = enable;
    }

    public static boolean closeMenu(View v) {
        boolean flag = false;
        if (sView != null) {
            sView.close();
            flag = sView.getChildAt(0) == v || sView.getChildAt(1) == v;
            sView = null;
        }
        return flag;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!mShowRightEnable) {
            return super.onInterceptTouchEvent(ev);
        }

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) ev.getRawX();
                if (sView != null && sView != this) {
                    sView.close();
                }
                deleteWidth = getChildAt(1).getWidth();
                break;
            case MotionEvent.ACTION_MOVE:
                moveX = (int) ev.getRawX();
                if (Math.abs(moveX - downX) > MAX_BOUNDARY) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                    return true;
                }
                break;
            default:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    private void recoverAnimation() {
        final View view = getChildAt(1);
        final SwipeLayoutParams swipeLayoutParams = new SwipeLayoutParams(view.getLayoutParams());
        ObjectAnimator animator = ObjectAnimator.ofInt(swipeLayoutParams, "width", deleteWidth).setDuration(500);
        animator.setInterpolator(new ViscousFluidInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                view.setLayoutParams(swipeLayoutParams);
            }
        });
        animator.start();
    }

    static class ViscousFluidInterpolator implements Interpolator {
        /**
         * Controls the viscous fluid effect (how much of it).
         */
        private static final float VISCOUS_FLUID_SCALE = 8.0f;

        private static final float VISCOUS_FLUID_NORMALIZE;
        private static final float VISCOUS_FLUID_OFFSET;

        static {

            // must be set to 1.0 (used in viscousFluid())
            VISCOUS_FLUID_NORMALIZE = 1.0f / viscousFluid(1.0f);
            // account for very small floating-point error
            VISCOUS_FLUID_OFFSET = 1.0f - VISCOUS_FLUID_NORMALIZE * viscousFluid(1.0f);
        }

        private static float viscousFluid(float x) {
            x *= VISCOUS_FLUID_SCALE;
            if (x < 1.0f) {
                x -= (1.0f - (float) Math.exp(-x));
            } else {
                float start = 0.36787944117f;   // 1/e == exp(-1)
                x = 1.0f - (float) Math.exp(1.0f - x);
                x = start + x * (1.0f - start);
            }
            return x;
        }

        @Override
        public float getInterpolation(float input) {
            final float interpolated = VISCOUS_FLUID_NORMALIZE * viscousFluid(input);
            if (interpolated > 0) {
                return interpolated + VISCOUS_FLUID_OFFSET;
            }
            return interpolated;
        }
    }

    private static class SwipeLayoutParams extends ViewGroup.LayoutParams {
        public SwipeLayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public void setWidth(int newWidth) {
            width = newWidth;
        }

        public int getWidth() {
            return width;
        }
    }
}