package cn.sjj.util;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;

import cn.sjj.base.BaseUtil;

/**
 * 一些不好分类的方法放在这里
 *
 * @auther 宋疆疆
 * @since 2017/5/23.
 */
public class LazyUtil extends BaseUtil {

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

}
