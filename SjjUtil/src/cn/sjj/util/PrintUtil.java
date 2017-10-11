package cn.sjj.util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 打印对象信息的工具类
 *
 * @auther 宋疆疆
 * @since 2017/8/28.
 */
public class PrintUtil {

    public static String print(Object[] objArray) {
        return print(objArray, ", ");
    }

    public static String print(Object[] objArray, String split) {
        if (objArray == null) {
            return "null";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("size: ");
        sb.append(objArray.length);
        sb.append(" { ");
        for (int i = 0; i < objArray.length; i++) {
            if (i > 0) {
                sb.append(split);
            }
            Object obj = objArray[i];
            sb.append(obj);
        }
        sb.append(" }");
        return sb.toString();
    }

    public static String print(List list) {
        return print(list, ", ");
    }

    public static String print(List list, String split) {
        if (list == null) {
            return "null";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("size: ");
        sb.append(list.size());
        sb.append(" { ");
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) {
                sb.append(split);
            }
            Object obj = list.get(i);
            sb.append(obj);
        }
        sb.append(" }");
        return sb.toString();
    }

    public static String print(Map map) {
        return print(map, ", ");
    }

    public static String print(Map map, String split) {
        if (map == null) {
            return "null";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("size: ");
        sb.append(map.size());
        sb.append(" [ ");
        Iterator iterator = map.keySet().iterator();
        for (int i = 0; iterator.hasNext(); i++) {
            if (i > 0) {
                sb.append(split);
            }
            Object key = iterator.next();
            sb.append("{ ");
            sb.append(key);
            sb.append(" = ");
            sb.append(map.get(key));
            sb.append(" }");
        }
        sb.append(" ]");
        return sb.toString();
    }

}
