package cn.sjj.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * @author coolszy
 * @date 2011-6-14
 * @blog http://blog.csdn.net/coolszy
 */

public class ShutdownReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		System.out.println("启动关闭中...");
	}
}
