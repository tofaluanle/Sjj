package cn.manjuu.searchproject.util;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import cn.manjuu.searchproject.R;

/**
 * 统一管理显示内容
 * 
 * @author Administrator
 * 
 */
public class PromptUtil {

	/**
	 * 控制Toast显示
	 */
	private static boolean isShow = true;
	private static Context mContext;

	public static void setContext(Context context) {
		PromptUtil.mContext = context;
	}

	/**
	 * 设置简单的进度框
	 * 
	 * @param progressDialog
	 *            需要展示的进度框
	 * @param msg
	 *            需要展示内容
	 */
	public static void showSimpleProgressDialog(ProgressDialog progressDialog,
			String msg) {
		if (progressDialog != null && !progressDialog.isShowing()) {
			progressDialog.setIcon(R.drawable.ic_launcher);
			progressDialog.setTitle(R.string.app_name);

			progressDialog.setMessage(msg);
			progressDialog.setCancelable(false);
			progressDialog.show();
		}
	}

	/**
	 * 关闭当前进度条
	 * 
	 * @param progressDialog
	 */
	public static void closeProgressDialog(ProgressDialog progressDialog) {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
	}

	/**
	 * 显示退出对话框
	 * 
	 * @param context
	 */
	public static void showExitDialog(Context context) {
		new AlertDialog.Builder(context)//
				.setIcon(R.drawable.ic_launcher)//
				.setTitle(R.string.app_name)//
				.setMessage(R.string.is_exit)//
				.setPositiveButton(R.string.is_positive,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								// 对缓存数据进行管理
								android.os.Process
										.killProcess(android.os.Process.myPid());
							}
						})//
				.setNegativeButton(R.string.is_negative, null)//
				.show();

	}

	public static void showNoNetWork(final Context context) {
		new AlertDialog.Builder(context)//
				.setIcon(R.drawable.ic_launcher)//
				.setTitle(R.string.app_name)//
				.setMessage("当前无网络")//
				.setPositiveButton("网络设置",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								// 跳转到系统的网络设置界面
								Intent intent = new Intent();
								intent.setClassName("com.android.settings",
										"com.android.settings.WirelessSettings");
								context.startActivity(intent);
							}
						})//
				.setNegativeButton("我知道了", null)//
				.show();
	}

	/**
	 * 显示错误提示框
	 * 
	 * @param context
	 * @param msg
	 */
	public static void showErrorDialog(Context context, String msg) {
		new AlertDialog.Builder(context)//
				.setIcon(R.drawable.ic_launcher)//
				.setTitle(R.string.app_name)//
				.setMessage(msg)//
				.setNegativeButton(context.getString(R.string.is_positive),
						null)//
				.show();
	}

	/**
	 * 显示错误提示框
	 * 
	 * @param context
	 * @param msg
	 */
	public static void showErrorDialog(Context context, String title, String msg) {
		new AlertDialog.Builder(context)
				.setIcon(R.drawable.ic_launcher)
				.setTitle(title)
				.setMessage(msg)
				.setNegativeButton(context.getString(R.string.is_positive),
						null).show();
	}

	/**
	 * 显示一个居中的Toast
	 * 
	 * @param context
	 * @param msg
	 */
	public static void showToastAtCenter(Context context, String msg) {
		Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	public static void makeNotify(Context context, Intent intent, int nid,
			String title, String message) {
		NotificationManager nm = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(R.drawable.ic_launcher,
				message, System.currentTimeMillis());
		PendingIntent contentIntent = null;
		if (null != intent) {
			contentIntent = PendingIntent.getActivity(context, nid, intent, 0);
		}
		notification.setLatestEventInfo(context, title, message, contentIntent);
		// nm.cancel(nid);
		nm.notify(nid, notification);
	}
}
