package cn.sjj.util;

/**
 * 提供捕捉异常的功能，让调用者的代码更简洁
 *
 * @auther 宋疆疆
 * @since 2017/10/24.
 */
public class SilenceUtil {

    public static int parseInt(String number) {
        return parseInt(number, 0);
    }

    public static int parseInt(String number, int defaultValue) {
        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return defaultValue;
    }

    public static long parseLong(String number) {
        return parseLong(number, 0);
    }

    public static long parseLong(String number, int defaultValue) {
        try {
            return Long.parseLong(number);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return defaultValue;
    }
}
