package cn.sjj.util;

import android.os.Handler;
import android.os.Looper;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import cn.sjj.BuildConfig;
import cn.sjj.base.ContextHolder;

/**
 * 统一管理显示内容
 *
 * @author sjj
 */
public class ToastUtil extends ContextHolder {

    private static Toast sToast = null;
    private static int sGravity;
    private static int sXOff;
    private static int sYOff;
    private static float sTextSize;
    private static TextView sTv;
    private static Handler mHandler = new Handler(Looper.getMainLooper());

    /**
     * 显示一个Toast
     *
     * @param msg
     */
    public static void showToast(String msg) {
        showToast(msg, false);
    }

    /**
     * 显示的Toast
     *
     * @param msg
     * @param textSize
     * @author 宋疆疆
     * @date 2014年6月20日 上午10:18:19
     */
    public static void showToast(String msg, int textSize) {
        showToast(msg, textSize, false);
    }

    /**
     * 显示一个Toast，专门为了开发人员开发时使用
     *
     * @param msg
     */
    public static void showDevelopToast(String msg) {
        if (!BuildConfig.DEBUG || !SystemTool.isAppOnForeground()) {
            return;
        }
        showToast(msg, -1, false, Toast.LENGTH_LONG);
    }

    /**
     * 显示一个Toast，专门为了开发人员开发时使用
     *
     * @param msg
     * @param textSize
     * @author 宋疆疆
     * @date 2014年6月20日 上午10:18:19
     */
    public static void showDevelopToast(String msg, int textSize) {
        if (!BuildConfig.DEBUG || !SystemTool.isAppOnForeground()) {
            return;
        }
        showToast(msg, textSize, false, Toast.LENGTH_LONG);
    }

    public static void showToast(final String msg, final boolean layoutCenter) {
        showToast(msg, -1, layoutCenter);
    }


    public static void showToast(final String msg, final int textSize, final boolean layoutCenter) {
        showToast(msg, textSize, layoutCenter, Toast.LENGTH_SHORT);
    }

    /**
     * 显示Toast
     *
     * @param msg
     * @param textSize
     * @author 宋疆疆
     * @date 2014年6月20日 上午10:18:19
     */
    public static void showToast(final String msg, final int textSize, final boolean layoutCenter, final int duration) {
        if (!SystemTool.isOnMainThread()) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    showToast(msg, textSize, layoutCenter, duration);
                }
            });
            return;
        }
        if (sToast == null) {
            sToast = Toast.makeText(sContext, msg, duration);
            sGravity = sToast.getGravity();
            sXOff = sToast.getXOffset();
            sYOff = sToast.getYOffset();
            ViewGroup view = (ViewGroup) sToast.getView();
            sTv = (TextView) view.getChildAt(0);
            sTextSize = sTv.getTextSize();
        } else {
            sToast.setText(msg);
        }
        if (layoutCenter) {
            sToast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            sToast.setGravity(sGravity, sXOff, sYOff);
        }
        if (textSize > 0) {
            sTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        } else {
            sTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, sTextSize);
        }
        sToast.show();
    }

}

