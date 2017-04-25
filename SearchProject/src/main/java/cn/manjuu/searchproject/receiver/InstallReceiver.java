package cn.manjuu.searchproject.receiver;

import java.io.File;

import cn.manjuu.searchproject.util.Logger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

public class InstallReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Logger.v("InstallReceiver onReceive");

		String path = intent.getStringExtra("path");
		Logger.d("path: " + path);
		if (TextUtils.isEmpty(path)) {
			return;
		}
		File apkfile = new File(path);
		if (!apkfile.exists()) {
			return;
		}
		// 通过Intent安装APK文件
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
				"application/vnd.android.package-archive");
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(i);
	}

}
