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

    public static String print(List list) {
        StringBuilder sb = new StringBuilder();
        sb.append("size: ");
        sb.append(list.size());
        sb.append(" { ");
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            Object obj = list.get(i);
            sb.append(obj);
        }
        sb.append(" }");
        return sb.toString();
    }

    public static String print(Map map) {
        StringBuilder sb = new StringBuilder();
        sb.append("size: ");
        sb.append(map.size());
        sb.append(" [ ");
        Iterator iterator = map.keySet().iterator();
        for (int i = 0; iterator.hasNext(); i++) {
            if (i > 0) {
                sb.append(", ");
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
