package cn.sjj.util;

import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import java.lang.reflect.Method;

/**
 * 跟硬件设备有关的工具类
 *
 * @auther 宋疆疆
 * @date 2016/3/21.
 */
public class DeviceUtil {

    public static void test(Context context) {
        StringBuilder sb = new StringBuilder();
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        sb.append("IMEI No : " + telephonyManager.getDeviceId() + "\n");
        sb.append("IMSI No : " + telephonyManager.getSubscriberId() + "\n");
        String hwID = android.os.Build.SERIAL;
        sb.append("hwID : " + hwID + "\n");
        String serialnum = null;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class, String.class);
            serialnum = (String) (get.invoke(c, "ro.serialno", "unknown"));
            sb.append("serial : " + serialnum + "\n");
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
        String serialnum2 = null;
        try {
            Class myclass = Class.forName("android.os.SystemProperties");
            Method[] methods = myclass.getMethods();
            Object[] params = new Object[]{new String("ro.serialno"), new String("Unknown")};
            serialnum2 = (String) (methods[2].invoke(myclass, params));
            sb.append("serial2 : " + serialnum2 + "\n");
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }

        String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        sb.append("AndroidID : " + androidId + "\n");
        System.out.println(sb.toString());
    }

    /**
     * Role:获取当前设置的电话号码 <BR>
     * Date:2012-3-12 <BR>
     *
     * @author CODYY)peijiangping
     */
    public String getNativePhoneNumber(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String NativePhoneNumber = null;
        NativePhoneNumber = telephonyManager.getLine1Number();
        return NativePhoneNumber;
    }

    /**
     * Role:Telecom service providers获取手机服务商信息 <BR>
     * 需要加入权限<uses-permission
     * android:name="android.permission.READ_PHONE_STATE"/> <BR>
     * Date:2012-3-12 <BR>
     *
     * @author CODYY)peijiangping
     */
    public String getProvidersName(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String ProvidersName = null;
        // 返回唯一的用户ID;就是这张卡的编号神马的
        String IMSI = telephonyManager.getSubscriberId();
        // IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
        System.out.println(IMSI);
        if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {
            ProvidersName = "中国移动";
        } else if (IMSI.startsWith("46001")) {
            ProvidersName = "中国联通";
        } else if (IMSI.startsWith("46003")) {
            ProvidersName = "中国电信";
        }
        return ProvidersName;
    }
}
