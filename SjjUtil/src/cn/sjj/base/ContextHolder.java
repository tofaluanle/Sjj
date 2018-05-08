package cn.sjj.base;

import android.content.Context;

/**
 * 工具类的基类，用来提供Context
 *
 * @auther 宋疆疆
 * @date 2016/8/16.
 */
public class ContextHolder {

    protected static Context sContext;

    public static void setContext(Context context) {
        sContext = context.getApplicationContext();
    }

}
