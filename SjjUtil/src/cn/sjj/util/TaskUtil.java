package cn.sjj.util;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import cn.sjj.R;
import cn.sjj.bean.TaskInfo;

public class TaskUtil {

	/**
	 * 判断当前应用程序是否处于前台
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isForeground(Context context) {
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> appProcesses = activityManager
				.getRunningAppProcesses();
		for (RunningAppProcessInfo appProcess : appProcesses) {
			if (appProcess.processName.equals(context.getPackageName())) {
				if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
					return true;
				} else {
					return false;
				}
			}
		}
		return false;
	}

	/**
	 * 判断当前应用程序是否处于后台
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isBackground(Context context) {

		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> appProcesses = activityManager
				.getRunningAppProcesses();
		for (RunningAppProcessInfo appProcess : appProcesses) {
			if (appProcess.processName.equals(context.getPackageName())) {
				if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
					return true;
				} else {
					return false;
				}
			}
		}
		return false;
	}

	/**
	 * 判断当前应用程序处于前台还是后台，需要权限<uses-permission
	 * android:name="android.permission.GET_TASKS" />
	 * 
	 * @param context
	 * 
	 * @return
	 */
	public static boolean isApplicationBroughtToBackground(final Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> tasks = am.getRunningTasks(1);
		if (!tasks.isEmpty()) {
			ComponentName topActivity = tasks.get(0).topActivity;
			if (!topActivity.getPackageName().equals(context.getPackageName())) {
				return true;
			}
		}
		return false;

	}

	public static int getRuninngAppProcessInfoSize(Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		return am.getRunningAppProcesses().size();
	}

	public static long getAvailMem(Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo outInfo = new MemoryInfo();
		am.getMemoryInfo(outInfo);
		return outInfo.availMem;
	}

	public static List<TaskInfo> getTaskInfos(Context context) {
		List<TaskInfo> taskInfos = new ArrayList<TaskInfo>();
		PackageManager pm = context.getPackageManager();
		TaskInfo taskInfo = null;
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> runningAppProcesses = am
				.getRunningAppProcesses();
		for (RunningAppProcessInfo info : runningAppProcesses) {
			taskInfo = new TaskInfo();
			int pid = info.pid;
			taskInfo.setPid(pid);
			String packageName = info.processName;
			taskInfo.setPackageName(packageName);
			ApplicationInfo applicationInfo = null;
			try {
				applicationInfo = pm.getApplicationInfo(packageName, 0);
				Drawable loadIcon = applicationInfo.loadIcon(pm);
				if (null == loadIcon) {
					taskInfo.setTask_icon(context.getResources().getDrawable(
							R.drawable.ic_launcher));
				} else {
					taskInfo.setTask_icon(loadIcon);
				}
				String loadLabel = applicationInfo.loadLabel(pm).toString();
				taskInfo.setTask_name(loadLabel);
				boolean filterApp = filterApp(applicationInfo);
				taskInfo.setUserTask(filterApp);
			} catch (NameNotFoundException e) {
				e.printStackTrace();
				taskInfo.setTask_icon(context.getResources().getDrawable(
						R.drawable.ic_launcher));
				taskInfo.setTask_name(packageName);
			}

			int[] pids = new int[] { pid };
			android.os.Debug.MemoryInfo[] processMemoryInfo = am
					.getProcessMemoryInfo(pids);
			android.os.Debug.MemoryInfo memoryInfo = processMemoryInfo[0];
			int dirty = memoryInfo.getTotalPrivateDirty();
			taskInfo.setTask_memory(dirty);

			taskInfos.add(taskInfo);
			taskInfo = null;
		}

		return taskInfos;
	}

	// 判断应用程序是否是用户程序
	public static boolean filterApp(ApplicationInfo info) {
		// 原来是系统应用，用户手动升级
		if ((info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
			return true;
			// 用户自己安装的应用程序
		} else if ((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
			return true;
		}
		return false;
	}

	public static void killProcess(Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> runningAppProcesses = am
				.getRunningAppProcesses();
		for (RunningAppProcessInfo info : runningAppProcesses) {
			am.killBackgroundProcesses(info.processName);
		}
	}
}
