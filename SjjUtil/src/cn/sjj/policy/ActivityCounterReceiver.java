package cn.sjj.policy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 用来接收Activity的数量变化的广播
 *
 * @auther 宋疆疆
 * @date 2016/7/19.
 */
public class ActivityCounterReceiver extends BroadcastReceiver {

    public static final String INTENT_ACTION = "icom.intent.action.ACTIVITY_COUNT_CHANGE";
    public static final String EXTRA_ACTION = "EXTRA_ACTION";
    public static final String EXTRA_ACTIVITY = "EXTRA_ACTIVITY";
    public static final int EXTRA_INCREASE = 1;
    public static final int EXTRA_DECREASE = 2;

    @Override
    public void onReceive(Context context, Intent intent) {
        int what = intent.getIntExtra(EXTRA_ACTION, 0);
        String activity = intent.getStringExtra(EXTRA_ACTIVITY);
        switch (what) {
            case EXTRA_INCREASE:
                ActivityCounter.getInstance().increase(activity);
                break;
            case EXTRA_DECREASE:
                ActivityCounter.getInstance().decrease(activity);
                break;
        }
    }

}
