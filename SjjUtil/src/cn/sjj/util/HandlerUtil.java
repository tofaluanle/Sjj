package cn.sjj.util;

import android.os.Handler;
import android.os.Message;

/**
 * 方便使用Handler发送消息或Runnable的工具类
 *
 * @author 宋疆疆
 * @since 2016/12/17.
 */
public class HandlerUtil {

    private static Handler mHandler = new Handler();

    /**
     * 发送一个任务，使用token作为标记，保证拥有相同token的任务只有一个在队列里
     *
     * @param run
     * @param token
     */
    public static void post(Runnable run, Object token) {
        postDelayed(mHandler, run, 0, token);
    }

    /**
     * 发送一个延时任务，使用token作为标记，保证拥有相同token的任务只有一个在队列里
     *
     * @param run
     * @param delay
     * @param token
     */
    public static void postDelayed(Runnable run, int delay, Object token) {
        postDelayed(mHandler, run, delay, token);
    }

    /**
     * 发送一个延时任务，使用token作为标记，保证拥有相同token的任务只有一个在队列里
     *
     * @param handler
     * @param run
     * @param token
     */
    public static void post(Handler handler, Runnable run, Object token) {
        postDelayed(handler, run, 0, token);
    }

    /**
     * 发送一个延时任务，使用token作为标记，保证拥有相同token的任务只有一个在队列里
     *
     * @param handler
     * @param run
     * @param delay
     * @param token
     */
    public static void postDelayed(Handler handler, Runnable run, int delay, Object token) {
        handler.removeCallbacksAndMessages(token);
        Message msg = Message.obtain(handler, run);
        msg.obj = token;
        handler.sendMessageDelayed(msg, delay);
    }

    public static void removeCallbacksAndMessages(Object token) {
        removeCallbacksAndMessages(mHandler, token);
    }

    public static void removeCallbacksAndMessages(Handler handler, Object token) {
        handler.removeCallbacksAndMessages(token);
    }

}
