package cn.sjj.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.UUID;

import cn.sjj.base.ContextHolder;

/**
 * 跟硬件设备有关的工具类
 *
 * @auther 宋疆疆
 * @date 2016/3/21.
 */
public class DeviceUtil extends ContextHolder {

    /**
     * Role:获取当前设置的电话号码 <BR>
     * Date:2012-3-12 <BR>
     *
     * @author CODYY)peijiangping
     */
    @SuppressLint("MissingPermission")
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
    @SuppressLint("MissingPermission")
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

    //获取手机识唯一设备识别码UUID并转为MD5
    @SuppressLint("MissingPermission")
    public static String getDeviceUUID(Context activity) {
        TelephonyManager tm = (TelephonyManager) sContext.getSystemService(Context.TELEPHONY_SERVICE);
        boolean isGranted = LazyUtil.checkPermission(activity, Manifest.permission.READ_PHONE_STATE);
        String deviceId = "";
        if (tm != null && isGranted) {
            deviceId = tm.getDeviceId();
        }
        String wifiMac = "";
        try {
            wifiMac = NetUtil.getWifiMac();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String androidId = Settings.Secure.getString(sContext.getContentResolver(), Settings.Secure.ANDROID_ID);
        if ("9774d56d682e549c".equals(androidId)) {
            androidId = "";
        }
        String serial = Build.SERIAL;
        String uuid = StringUtil.makeNotNull(deviceId) + StringUtil.makeNotNull(wifiMac) + StringUtil.makeNotNull(androidId) + StringUtil.makeNotNull(serial);

        if (TextUtils.isEmpty(uuid)) {
            uuid = Installation.id(sContext);
        }
        if (TextUtils.isEmpty(uuid)) {
            LazyUtil.sendUnExpect(new DeviceUtil(), new Exception());
            uuid = UUID.randomUUID().toString();
        }
        return MD5.get(uuid);
    }

    public static class Installation {
        private static       String sID          = null;
        private static final String INSTALLATION = "INSTALLATION";

        public synchronized static String id(Context context) {
            if (sID == null) {
                File installation = new File(context.getFilesDir(), INSTALLATION);
                try {
                    if (!installation.exists()) {
                        writeInstallationFile(installation);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    sID = readInstallationFile(installation);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return sID;
        }

        private static String readInstallationFile(File installation) throws IOException {
            RandomAccessFile f = new RandomAccessFile(installation, "r");
            byte[] bytes = new byte[(int) f.length()];
            f.readFully(bytes);
            LazyUtil.close(f);
            return new String(bytes);
        }

        private static void writeInstallationFile(File installation) throws IOException {
            FileOutputStream out = new FileOutputStream(installation);
            String id = UUID.randomUUID().toString();
            out.write(id.getBytes());
            LazyUtil.close(out);
        }
    }
}
