package cn.sjj.util;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * 确保实参是安全的
 *
 * @author 宋疆疆
 * @since 2019/5/25.
 */
public class MakeSafeUtil {

    public static String make(String str) {
        if (str == null) {
            return "";
        }
        return str;
    }

    public static String make(String str, String replace) {
        if (str == null) {
            return replace;
        }
        return str;
    }

    public static CharSequence make(CharSequence str) {
        if (str == null) {
            return "";
        }
        return str;
    }

    public static CharSequence make(CharSequence str, CharSequence replace) {
        if (str == null) {
            return replace;
        }
        return str;
    }

    public static ArrayList make(ArrayList list) {
        if (list == null) {
            list = new ArrayList();
        }
        while (list.contains(null)) {
            list.remove(null);
        }
        for (Object o : list) {
            if (o instanceof ISafe) {
                ((ISafe) o).makeSafe();
            }
        }
        return list;
    }

    public static LinkedList make(LinkedList list) {
        if (list == null) {
            list = new LinkedList();
        }
        while (list.contains(null)) {
            list.remove(null);
        }
        for (Object o : list) {
            if (o instanceof ISafe) {
                ((ISafe) o).makeSafe();
            }
        }
        return list;
    }


    public static int makeMin(int origin, int min) {
        origin = Math.max(min, origin);
        return origin;
    }

    public static int makeMax(int origin, int max) {
        origin = Math.min(max, origin);
        return origin;
    }

    public static int make(int origin, int min, int max) {
        origin = Math.min(max, origin);
        origin = Math.max(min, origin);
        return origin;
    }

    public static long makeMin(long origin, long min) {
        origin = Math.max(min, origin);
        return origin;
    }

    public static long makeMax(long origin, long max) {
        origin = Math.min(max, origin);
        return origin;
    }

    public static long make(long origin, long min, long max) {
        origin = Math.min(max, origin);
        origin = Math.max(min, origin);
        return origin;
    }

    public static float makeMin(float origin, float min) {
        origin = Math.max(min, origin);
        return origin;
    }

    public static float makeMax(float origin, float max) {
        origin = Math.min(max, origin);
        return origin;
    }

    public static float make(float origin, float min, float max) {
        origin = Math.min(max, origin);
        origin = Math.max(min, origin);
        return origin;
    }

    public static double makeMin(double origin, double min) {
        origin = Math.max(min, origin);
        return origin;
    }

    public static double makeMax(double origin, double max) {
        origin = Math.min(max, origin);
        return origin;
    }

    public static double make(double origin, double min, double max) {
        origin = Math.min(max, origin);
        origin = Math.max(min, origin);
        return origin;
    }

    public interface ISafe {
        void makeSafe();
    }
}
