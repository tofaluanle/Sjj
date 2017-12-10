package cn.sjj.policy;

import android.app.Activity;

import java.util.LinkedList;

import cn.sjj.BuildConfig;
import cn.sjj.Logger;

/**
 * 保存所有activity实例的单例类
 *
 * @author Sjj
 * @version 2015-06-26
 */
public class ActivityHolder {

    private static ActivityHolder ourInstance = new ActivityHolder();
    private LinkedList<Activity> activities;
    private int mUIEnableCount;

    public int getUIEnableCount() {
        return mUIEnableCount;
    }

    public static ActivityHolder getInstance() {
        return ourInstance;
    }

    private ActivityHolder() {
        activities = new LinkedList<>();
    }

    public boolean contains(String className) {
        synchronized (activities) {
            for (Activity activity : activities) {
                if (className.equals(activity.getClass().getName())) {
                    return true;
                }
            }
            return false;
        }
    }

    public void add(Activity activity) {
        synchronized (activities) {
            activities.add(activity);
        }
    }

    public Activity getLast() {
        synchronized (activities) {
            return activities.getLast();
        }
    }

    public Activity get(Class type) {
        synchronized (activities) {
            for (Activity activity : activities) {
                if (activity.getClass().equals(type)) {
                    return activity;
                }
            }
        }
        return null;
    }

    public void remove(Activity activity) {
        synchronized (activities) {
            activities.remove(activity);
        }
    }

    public void onStart(Activity activity) {
        if (mUIEnableCount == 0) {
            makeLogFlash();
        }
        mUIEnableCount++;
//        Logger.i("mUIEnableCount: " + mUIEnableCount + ", " + activity);
    }

    public void onStop(Activity activity) {
        mUIEnableCount--;
//        Logger.i("mUIEnableCount: " + mUIEnableCount + ", " + activity);
        if (mUIEnableCount < 0) {
            Logger.e("mUIEnableCount < 0, why !?");
            mUIEnableCount = 0;
        }
    }

    public void finishAll() {
        synchronized (activities) {
            for (Activity activity : activities) {
                activity.finish();
            }
        }
    }

    public void finishAllExcept(Activity except) {
        synchronized (activities) {
            for (Activity activity : activities) {
                if (activity == except) {
                    continue;
                }
                activity.finish();
            }
        }
    }

    private void makeLogFlash() {
        Logger.i("\n#######################################"
                + "\n#                                     #"
                + "\n#                                     #"
                + "\n#          APP GO Foreground          #"
                + "\n#          "
                + BuildConfig.VERSION_NAME + "." + BuildConfig.BUILD_NO + "." + BuildConfig.GIT_REVISION
                + "          #"
                + "\n#                                     #"
                + "\n#                                     #"
                + "\n#######################################");
    }

}
