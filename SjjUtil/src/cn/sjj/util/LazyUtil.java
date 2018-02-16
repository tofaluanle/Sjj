package cn.sjj.util;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import cn.sjj.Logger;
import cn.sjj.base.BaseUtil;

/**
 * 一些不好分类的方法放在这里
 *
 * @auther 宋疆疆
 * @since 2017/5/23.
 */
public class LazyUtil extends BaseUtil {

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

}
