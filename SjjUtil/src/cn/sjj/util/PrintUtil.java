package cn.sjj.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 打印对象信息的工具类
 *
 * @author 宋疆疆
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

    public static String print(Set set) {
        return print(set, ", ");
    }

    public static String print(Set set, String split) {
        if (set == null) {
            return "null";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("size: ");
        sb.append(set.size());
        sb.append(" { ");
        Iterator iterator = set.iterator();
        for (int i = 0; iterator.hasNext(); i++) {
            if (i > 0) {
                sb.append(split);
            }
            Object obj = iterator.next();
            sb.append(obj);
        }
        sb.append(" }");
        return sb.toString();
    }

    public static String printJson(String json) {
        String message;
        try {
            if (json.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(json);
                message = jsonObject.toString(4);//最重要的方法，就一行，返回格式化的json字符串，其中的数字4是缩进字符数
            } else if (json.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(json);
                message = jsonArray.toString(4);
            } else {
                message = json;
            }
        } catch (JSONException e) {
            message = json;
        }
        return message;
    }
}
