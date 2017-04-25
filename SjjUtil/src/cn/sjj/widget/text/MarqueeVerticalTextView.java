package cn.sjj.widget.text;

import android.content.Context;
import android.os.Message;
import android.util.AttributeSet;

public class MarqueeVerticalTextView extends AbsMarqueeTextView {

    private int currY;
    private int targetY;
    private int rate = 30;

    public int getCurrY() {
        return currY;
    }

    public MarqueeVerticalTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    public MarqueeVerticalTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public MarqueeVerticalTextView(Context context) {
        super(context);

    }

    public boolean isNeedMarquee() {
        return getLayout().getHeight() > getHeight();
    }

    @Override
    public void startMarquee(int mode) {
        targetY = getLayout().getHeight();
        if (null == runner) {
            runner = new ScrollRunnable();
        }
        mHandler.removeMessages(0);
        Message msg = Message.obtain(mHandler, runner);
        msg.what = 0;
        mHandler.sendMessageDelayed(msg, rate);
//		mHandler.postDelayed(runner, rate);
    }

    @Override
    public void stopMarquee() {
        mHandler.removeCallbacks(runner);
    }

    public void reset() {
        stopMarquee();
        currY = 0;
        scrollTo(0, 0);
    }

    private final class ScrollRunnable implements Runnable {

        @Override
        public void run() {
            scrollTo(0, currY);
            if (currY < targetY) {
                currY += speed;
                mHandler.postDelayed(runner, rate);
            } else {
                currY = -getHeight();
                mHandler.postDelayed(runner, rate);
            }
        }

    }

}
