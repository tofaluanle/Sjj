package cn.sjj.policy;

import cn.sjj.Logger;

/**
 * 用来记录APP的Activity的总数量，并按照策略进行处理
 *
 * @author 宋疆疆
 * @date 2016/7/19.
 */
public class ActivityCounter {

    private int mUIEnableCount;

    public int getUIEnableCount() {
        return mUIEnableCount;
    }

    private ActivityCounter() {
        init();
    }

    public static ActivityCounter getInstance() {
        return ActivityCounterHolder.ourInstance;
    }

    private void init() {

    }

    public void increase(String activity) {
        if (mUIEnableCount == 0) {
        }
        mUIEnableCount++;
        Logger.i("mUIEnableCount increase: " + mUIEnableCount + ", " + activity);
    }

    public void decrease(String activity) {
        mUIEnableCount--;
        Logger.i("mUIEnableCount decrease: " + mUIEnableCount + ", " + activity);
        if (mUIEnableCount == 0) {
        } else if (mUIEnableCount < 0) {
            String msg = "mUIEnableCount < 0, why !? " + ActivityCounterReceiver.INTENT_ACTION + ", " + activity;
            Logger.e(msg);
            mUIEnableCount = 0;
        }
    }

    private static class ActivityCounterHolder {
        private static ActivityCounter ourInstance = new ActivityCounter();
    }
}
