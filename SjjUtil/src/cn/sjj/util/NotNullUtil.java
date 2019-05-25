package cn.sjj.util;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * @author 宋疆疆
 * @since 2019/5/25.
 */
public class NotNullUtil {

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
            if (o instanceof INotNull) {
                ((INotNull) o).makeNotNull();
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
            if (o instanceof INotNull) {
                ((INotNull) o).makeNotNull();
            }
        }
        return list;
    }

    public interface INotNull {
        void makeNotNull();
    }
}
