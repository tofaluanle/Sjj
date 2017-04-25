package cn.manjuu.searchproject.util;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.widget.TextView;
import cn.manjuu.searchproject.R;

public class NetUtil {

	/**
	 * 判断网络情况
	 * 
	 * @param context
	 *            上下文
	 * @return false 表示没有网络 true 表示有网络
	 */
	public static boolean isNetworkAvalible(Context context) {
		// 获得网络状态管理器
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (connectivityManager == null) {
			return false;
		} else {
			// 建立网络数组
			NetworkInfo[] net_info = connectivityManager.getAllNetworkInfo();

			if (net_info != null) {
				for (int i = 0; i < net_info.length; i++) {
					// 判断获得的网络状态是否是处于连接状态
					if (net_info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	// 如果没有网络，则弹出网络设置对话框
	public static void checkNetwork(final Activity activity) {
		if (!NetUtil.isNetworkAvalible(activity)) {
			TextView msg = new TextView(activity);
			msg.setText("当前没有可以使用的网络，请设置网络！");
			new AlertDialog.Builder(activity)
					.setIcon(R.drawable.ic_launcher)
					.setTitle("网络状态提示")
					.setView(msg)
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int whichButton) {
									// 跳转到设置界面
									activity.startActivityForResult(new Intent(
											Settings.ACTION_WIRELESS_SETTINGS),
											0);
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

	public static void setApnParame(Context context) {
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

	public static String getIp() throws IOException {
		String localIp = null;
		for (Enumeration<NetworkInterface> en = NetworkInterface
				.getNetworkInterfaces(); en.hasMoreElements();) {
			NetworkInterface intf = en.nextElement();
			for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr
					.hasMoreElements();) {
				InetAddress inetAddress = enumIpAddr.nextElement();
				if (!inetAddress.isLoopbackAddress()) {
					if (inetAddress.isReachable(1000)) {
						InetAddress localInetAddress = inetAddress;
						localIp = inetAddress.getHostAddress().toString();
						byte[] localIpBytes = inetAddress.getAddress();
						System.arraycopy(localIpBytes, 0, new byte[255], 44, 4);
					}
				}
			}
		}
		return localIp;
	}

	public static String getWifiMac(Context context) {
		WifiManager wifi = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);

		WifiInfo info = wifi.getConnectionInfo();

		String macAddress = info.getMacAddress();

		return macAddress;
	}

	public static String getNetTypeName(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		return info.getSubtypeName();
	}

}
