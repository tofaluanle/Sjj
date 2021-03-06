package cn.sjj.util;

/**
 * 提供捕捉异常的功能，让调用者的代码更简洁
 *
 * @author 宋疆疆
 * @since 2017/10/24.
 */
public class SilenceUtil {

    public static int parseInt(String number) {
        return parseInt(number, 0);
    }

    public static int parseInt(String number, int defaultValue) {
        try {
            return Integer.parseInt(number);
        } catch (Exception e) {
        }
        return defaultValue;
    }

    public static long parseLong(String number) {
        return parseLong(number, 0);
    }

    public static long parseLong(String number, long defaultValue) {
        try {
            return Long.parseLong(number);
        } catch (Exception e) {
        }
        return defaultValue;
    }

    public static float parseFloat(String number) {
        return parseFloat(number, 0);
    }

    public static float parseFloat(String number, float defaultValue) {
        try {
            return Float.parseFloat(number);
        } catch (Exception e) {
        }
        return defaultValue;
    }

    public static double parseDouble(String number) {
        return parseDouble(number, 0);
    }

    public static double parseDouble(String number, double defaultValue) {
        try {
            return Double.parseDouble(number);
        } catch (Exception e) {
        }
        return defaultValue;
    }
}
