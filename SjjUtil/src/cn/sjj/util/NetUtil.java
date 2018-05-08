package cn.sjj.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.widget.TextView;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.sjj.Logger;
import cn.sjj.R;
import cn.sjj.base.ContextHolder;

public class NetUtil extends ContextHolder {

    private final static ArrayList<Integer> channelsFrequency = new ArrayList<Integer>(
            Arrays.asList(0, 2412, 2417, 2422, 2427, 2432, 2437, 2442, 2447,
                    2452, 2457, 2462, 2467, 2472, 2484));

    public final static ArrayList<Integer> NET_MASK = new ArrayList<Integer>(
            Arrays.asList(0, 128, 192, 224, 240, 248, 252, 254, 255));

    /**
     * 根据扫描到的wifi的Frequency计算信道值
     *
     * @param frequency
     * @return
     * @author 宋疆疆
     * @date 2014-5-13 上午11:03:02
     */
    public static int getChannelFromFrequency(int frequency) {
        return channelsFrequency.indexOf(Integer.valueOf(frequency));
    }

    /**
     * 根据信道计算Frequency
     *
     * @param channel
     * @return
     * @author 宋疆疆
     * @date 2014-5-13 上午11:04:03
     */
    public static Integer getFrequencyFromChannel(int channel) {
        return channelsFrequency.get(channel);
    }

    // 如果没有网络，则弹出网络设置对话框
    public static void checkNetwork(final Activity activity) {
        if (!NetUtil.isNetworkConnected()) {
            TextView msg = new TextView(activity);
            msg.setText("当前没有可以使用的网络，请设置网络！");
            new AlertDialog.Builder(activity)
                    .setIcon(R.drawable.ic_launcher)
                    .setTitle("网络状态提示")
                    .setView(msg)
                    .setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    // 跳转到设置界面
                                    activity.startActivityForResult(new Intent(Settings.ACTION_WIRELESS_SETTINGS), 0);
                                }
                            }).create().show();
        }
        return;
    }

    public static boolean isWIFIConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        boolean connected = networkInfo.isConnected();
        return connected;
    }

    public static boolean isMobileConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        boolean connected = networkInfo.isConnected();
        return connected;
    }

    public static void setApnParam(Context context) {
        Uri uri = Uri.parse("content://telephony/carriers/preferapn");

        ContentResolver resolver = context.getContentResolver();
        Cursor query = resolver.query(uri, null, null, null, null);
        if (query.moveToNext()) {
            // GloableParams.PROXY_IP =
            // query.getString(query.getColumnIndex("proxy"));
            // GloableParams.PROXY_PORT =
            // query.getInt(query.getColumnIndex("port"));
        }
        query.close();

    }

    public static String getIp() {
        String localIp = "null";
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        if (inetAddress.isReachable(1000)) {
                            localIp = inetAddress.getHostAddress().toString();
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return localIp;
    }

    public static byte[] getIpByte() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                if (networkInterface.getDisplayName().contains("Virtual")) {
                    continue;
                }
                if (networkInterface.isVirtual()) continue;
                if (networkInterface.isLoopback()) continue;
                if (!networkInterface.isUp()) continue;
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                InetAddress address = null;
                while (inetAddresses.hasMoreElements()) {
                    address = inetAddresses.nextElement();
                }
                if (address != null) {
                    return address.getAddress();
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getWifiMac() {
        WifiManager wifi = (WifiManager) sContext.getSystemService(Context.WIFI_SERVICE);
        if (wifi == null) {
            return "";
        }
        WifiInfo info = wifi.getConnectionInfo();
        if (info == null) {
            return "";
        }
        String macAddress = info.getMacAddress();
        if (macAddress.equals("02:00:00:00:00:00")) {
            macAddress = getMacAddr();
        }
        if (macAddress.equals("02:00:00:00:00:00")) {
            return "";
        }
        return macAddress;
    }

    private static String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;
                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }
                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }
                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public static String getNetTypeName(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info.getSubtypeName();
    }

    /*
     * 匹配IP
     */
    public static boolean matchIP(String ip) {
        if (null == ip)
            return false;
        String regex = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(ip);
        return matcher.matches();
    }

    /**
     * 匹配端口
     *
     * @param port
     * @return
     */
    public static boolean matchPort(String port) {
        if (null == port)
            return false;
        String regex = "([1-9][0-9]{0,4})";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(port);
        return matcher.matches();
    }

    public static boolean isNetMask(String netMask) {
        if (!matchIP(netMask)) {
            return false;
        }
        String[] split = netMask.split("\\.");
        for (int i = 0; i < split.length; i++) {
            if (i != 0 && !split[i].equals("0") && !split[i - 1].equals("255")) {
                return false;
            }
        }
        return true;
    }

    public static int netMask2PrefixLength(String netMask) {
        if (!matchIP(netMask)) {
            return -1;
        }
        int prefix = 0;
        String[] split = netMask.split("\\.");
        for (int i = 0; i < split.length; i++) {
            int netmask = Integer.parseInt(split[i]);
            if (i != 0 && !split[i].equals("0") && !split[i - 1].equals("255")) {
                return -1;
            }
            if (NetUtil.NET_MASK.contains(netmask)) {
                prefix += Integer.bitCount(netmask);
            } else {
                return -1;
            }
        }
        return prefix;
    }

    /**
     * 检测网络是否可用
     *
     * @return
     */
    public static boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) sContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean connect = false;
        StringBuilder sb = new StringBuilder();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Network[] allNetworks = cm.getAllNetworks();
            if (allNetworks == null || allNetworks.length <= 0) {
                sb.append("[ no network ]");
            } else {
                for (Network network : allNetworks) {
                    NetworkInfo info = cm.getNetworkInfo(network);
                    if (info == null) {
                        continue;
                    }
                    sb.append("[ network: ");
                    sb.append(info.getTypeName());
                    sb.append(", state: ");
                    sb.append(info.getState());
                    sb.append(" ]");
                    if (info.getType() == ConnectivityManager.TYPE_MOBILE || info.getType() == ConnectivityManager.TYPE_WIFI) {
                        connect |= info.isConnected();
                    }
                }
            }
        } else {
            NetworkInfo[] allNetworkInfo = cm.getAllNetworkInfo();
            if (allNetworkInfo == null || allNetworkInfo.length <= 0) {
                sb.append("[ no network ]");
            } else {
                for (NetworkInfo info : allNetworkInfo) {
                    sb.append("[ network: ");
                    sb.append(info.getTypeName());
                    sb.append(", state: ");
                    sb.append(info.getState());
                    sb.append(" ]");
                    if (info.getType() == ConnectivityManager.TYPE_MOBILE || info.getType() == ConnectivityManager.TYPE_WIFI) {
                        connect |= info.isConnected();
                    }
                }
            }
        }
        Logger.d(sb.toString());
        return connect;
    }

    public static final int TYPE_WIFI  = 0x01;
    public static final int TYPE_CMWAP = 0x02;
    public static final int TYPE_CMNET = 0x03;

    /**
     * 获取当前网络类型
     * 没有使用过，不能确认是否好使
     *
     * @return 0：没有网络   1：WIFI网络   2：WAP网络    3：NET网络
     */
    public static int getNetworkType2(Context context) {
        int netType = 0;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            String extraInfo = networkInfo.getExtraInfo();
            if (!TextUtils.isEmpty(extraInfo)) {
                if (extraInfo.toLowerCase(Locale.CHINA).equals("cmnet")) {
                    netType = TYPE_CMNET;
                } else {
                    netType = TYPE_CMWAP;
                }
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = TYPE_WIFI;
        }
        return netType;
    }

    //↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
    //http://blog.csdn.net/nanzhiwen666/article/details/8288433#
    /**
     * 没有网络
     */
    public static final int NETWORK_TYPE_INVALID = 0;
    /**
     * wap网络
     */
    public static final int NETWORK_TYPE_WAP     = 1;
    /**
     * 2G网络
     */
    public static final int NETWORK_TYPE_2G      = 2;
    /**
     * 3G和3G以上网络，或统称为快速网络
     */
    public static final int NETWORK_TYPE_3G      = 3;
    /**
     * wifi网络
     */
    public static final int NETWORK_TYPE_WIFI    = 4;

    /**
     * 获取网络状态，wifi,wap,2g,3g.
     *
     * @param context 上下文
     * @return int 网络状态 {@link #NETWORK_TYPE_2G},{@link #NETWORK_TYPE_3G},*{@link #NETWORK_TYPE_INVALID},{@link #NETWORK_TYPE_WAP}* <p>{@link #NETWORK_TYPE_WIFI}
     */
    public static int getNetWorkType(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        int mNetWorkType = -1;
        if (networkInfo != null && networkInfo.isConnected()) {
            int type = networkInfo.getType();
            if (type == ConnectivityManager.TYPE_WIFI) {
                mNetWorkType = NETWORK_TYPE_WIFI;
            } else if (type == ConnectivityManager.TYPE_MOBILE) {
                String proxyHost = android.net.Proxy.getDefaultHost();
                mNetWorkType = TextUtils.isEmpty(proxyHost) ? (isFastMobileNetwork(context) ? NETWORK_TYPE_3G : NETWORK_TYPE_2G) : NETWORK_TYPE_WAP;
            }
        } else {
            mNetWorkType = NETWORK_TYPE_INVALID;
        }
        return mNetWorkType;
    }

    private static boolean isFastMobileNetwork(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        switch (telephonyManager.getNetworkType()) {
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                return false; // ~ 50-100 kbps
            case TelephonyManager.NETWORK_TYPE_CDMA:
                return false; // ~ 14-64 kbps
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return false; // ~ 50-100 kbps
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                return true; // ~ 400-1000 kbps
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                return true; // ~ 600-1400 kbps
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return false; // ~ 100 kbps
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return true; // ~ 2-14 Mbps
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return true; // ~ 700-1700 kbps
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return true; // ~ 1-23 Mbps
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return true; // ~ 400-7000 kbps
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                return true; // ~ 1-2 Mbps
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                return true; // ~ 5 Mbps
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return true; // ~ 10-20 Mbps
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return false; // ~25 kbps
            case TelephonyManager.NETWORK_TYPE_LTE:
                return true; // ~ 10+ Mbps
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                return false;
            default:
                return false;
        }
    }

    //http://blog.csdn.net/nanzhiwen666/article/details/8288433#
    //↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    public static String getNetworkType2017() {
        String strNetworkType = "";
        @SuppressLint("MissingPermission")
        NetworkInfo networkInfo = ((ConnectivityManager) sContext.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                strNetworkType = "WIFI";
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                String _strSubTypeName = networkInfo.getSubtypeName();
                // TD-SCDMA   networkType is 17
                int networkType = networkInfo.getSubtype();
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
                        strNetworkType = "2G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
                    case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
                    case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                        strNetworkType = "3G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                        strNetworkType = "4G";
                        break;
                    default:
                        // http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
                        if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA") || _strSubTypeName.equalsIgnoreCase("WCDMA") || _strSubTypeName.equalsIgnoreCase("CDMA2000")) {
                            strNetworkType = "3G";
                        } else {
                            strNetworkType = _strSubTypeName;
                        }

                        break;
                }
            }
        }
        return strNetworkType;
    }

    public static String getNetworkTypeStr2017() {
        String strNetworkType = "";
        @SuppressLint("MissingPermission")
        NetworkInfo networkInfo = ((ConnectivityManager) sContext.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                strNetworkType = "";
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                strNetworkType = networkInfo.getSubtypeName();
            }
        }
        return strNetworkType;
    }

    public static String getIPAddress2017() {
        @SuppressLint("MissingPermission")
        NetworkInfo info = ((ConnectivityManager) sContext.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {//当前使用2G/3G/4G网络
                try {
                    //Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }

            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
                WifiManager wifiManager = (WifiManager) sContext.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址
                return ipAddress;
            }
        } else {
            //当前无网络连接,请在设置中打开网络
        }
        return "";
    }

    /**
     * 将得到的int类型的IP转换为String类型
     *
     * @param ip
     * @return
     */
    public static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }
}
