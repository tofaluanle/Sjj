package cn.sjj.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import cn.sjj.Logger;
import cn.sjj.base.ContextHolder;

/**
 * 一些不好分类的方法放在这里
 *
 * @author 宋疆疆
 * @since 2017/5/23.
 */
public class LazyUtil extends ContextHolder {

    private static Handler mUIHandler = new Handler(Looper.getMainLooper());

    public static void sendUnExpect(Throwable t) {
        ToastUtil.showDevelopToast("程序调用了LazyUtil.sendUnExpect，快看日志！" + Logger.getLineNumber(t));
        t.printStackTrace();
    }

    /**
     * @param object     必须传this
     * @param lineNumber 必须传new Exception()
     */
    public static void sendUnExpect(Object object, Throwable lineNumber) {
        sendUnExpect(object, lineNumber, "");
    }

    /**
     * @param object     必须传this
     * @param lineNumber 必须传new Exception()
     * @param msg        自己想要收集的信息的使用此参数传入
     */
    public static void sendUnExpect(Object object, Throwable lineNumber, String msg) {
        String callLine = object.getClass().getSimpleName() + Logger.getLineNumber(lineNumber);
        String toastMsg = "程序调用了LazyUtil.sendUnExpect，快看日志！" + callLine;
        ToastUtil.showDevelopToast(toastMsg);
        msg = TextUtils.isEmpty(msg) ? Logger.UN_EXPECT_LOG : msg;
        msg = "msg: " + msg + " " + callLine;
        Logger.e(toastMsg);
        Logger.e(msg);
        lineNumber.printStackTrace();
    }

    public static void close(Closeable c) {
        if (c == null) {
            return;
        }
        if (c instanceof Flushable) {
            Flushable f = (Flushable) c;
            try {
                f.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            c.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void runOnUI(Runnable run) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            run.run();
        } else {
            mUIHandler.post(run);
        }
    }

    public static boolean isEmpty(List list) {
        return list == null || list.isEmpty();
    }

    public static boolean isEmpty(Map map) {
        return map == null || map.isEmpty();
    }

    /**
     * dp转px
     *
     * @param dp
     * @return
     */
    public static int dip2px(int dp) {
        float density = sContext.getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5);
    }

    /**
     * px转换dip
     */
    public static int px2dip(int px) {
        final float scale = sContext.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    /**
     * px转换sp
     */
    public static int px2sp(int pxValue) {
        final float fontScale = sContext.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * sp转换px
     */
    public static int sp2px(int spValue) {
        final float fontScale = sContext.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static boolean checkPermission(Context context, String permission) {
        boolean result = false;
        if (Build.VERSION.SDK_INT >= 23) {
            if (context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
                result = true;
            }
        } else {
            PackageManager pm = context.getPackageManager();
            if (pm.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
                result = true;
            }
        }
        return result;
    }
}
